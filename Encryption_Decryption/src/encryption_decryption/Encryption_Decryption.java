package encryption_decryption;



public class Encryption_Decryption {

    
    
    public static void main(String[] args) {
        GenerateKeys gk = new GenerateKeys();
        gk.createKeys();
        
        EncDec e = new EncDec();
        e.EncryptFile("plainTxt");
                
        RSACryptography rsa = new RSACryptography();
        rsa.setPrivate("myPrivateKey");
        rsa.setPublic("myPublicKey");
        rsa.encryptFile(e.getKeyFile(), "EncryptedKey");
        rsa.decryptFile("EncryptedKey", "deKey");
        
        e.DecryptFile("plainTxt.enc");
    }
    
}
