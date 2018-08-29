package encryption_decryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSACryptography {

    private Cipher cipher;
    PrivateKey priv;
    PublicKey pub;

    public RSACryptography() {
        try {
            this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrivate(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.priv = kf.generatePrivate(spec);
        } catch (IOException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setPublic(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.pub = kf.generatePublic(spec);
        } catch (IOException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void encryptFile(String input, String output) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.pub);
            
            FileInputStream in = new FileInputStream(input);
            FileOutputStream out = new FileOutputStream(output);
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = cipher.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            in.close();
            byte[] obuf = cipher.doFinal();
            if ( obuf != null ) {
                out.write(obuf);
                out.flush();
                out.close();
            }
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decryptFile(String input, String output) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.priv);
            
            FileInputStream in = new FileInputStream(input);
            FileOutputStream out = new FileOutputStream(output);
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = cipher.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            in.close();
            byte[] obuf = cipher.doFinal();
            if ( obuf != null ) {
                out.write(obuf);
                out.flush();
                out.close();
            }
            
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RSACryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
