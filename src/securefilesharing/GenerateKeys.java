package encryption_decryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys() {
        try {
            this.keyGen = KeyPairGenerator.getInstance("RSA");
            this.keyGen.initialize(2048);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GenerateKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public byte[] getPrivateKey() {
        return this.privateKey.getEncoded();
    }

    public byte[] getPublicKey() {
        return this.publicKey.getEncoded();
    }

    private void writeToFile(String path, byte[] key){
        try {
            File f = new File(path);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(key);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerateKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
