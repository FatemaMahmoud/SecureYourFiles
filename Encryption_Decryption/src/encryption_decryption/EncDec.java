package encryption_decryption;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class EncDec {

    private final String ivFile = "iv";
    private final String enkeyFile = "enKey";
    private final String dekeyFile = "deKey";
        
    
    public void EncryptFile(String plainTxtFile) {
        FileOutputStream out = null;
        try {
            byte[] iv = new byte[128 / 8];
            SecureRandom srandom = new SecureRandom();
            srandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            out = new FileOutputStream(ivFile);
            out.write(iv);
            out.flush();
            out.close();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKey skey = kgen.generateKey();
            out = new FileOutputStream(enkeyFile);
            byte[] keyb = skey.getEncoded();
            out.write(keyb);
            out.flush();
            out.close();
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
            FileInputStream in = new FileInputStream(plainTxtFile);
            out = new FileOutputStream(plainTxtFile+".enc");
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            in.close();
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) {
                out.write(obuf);
                out.flush();
                out.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void DecryptFile(String cipherTxtFile) {
        try {
            byte[] iv = Files.readAllBytes(Paths.get(ivFile));
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            byte[] keyb = Files.readAllBytes(Paths.get(dekeyFile));
            SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
            FileInputStream in = new FileInputStream(cipherTxtFile);
            FileOutputStream out = new FileOutputStream(cipherTxtFile.substring(0, cipherTxtFile.length() - 4)+".dec");
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            in.close();
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) {
                out.write(obuf);
                out.flush();
                out.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(EncDec.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getKeyFile() {
        return enkeyFile;
    }


}
