package helper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class DecryptPassword {
    public static void main(String[] args) {
        // The hardcoded key from your snippet
        String secretKey = "w0Rkl04Dh@$h_keY";
        
        // The ciphertext, with the missing '=' padding appended
        String cipherText = "2sfI4QAqGRhUDM92ZND1x3EI+WMW/XxqYHC626rKrp8=";
        
        try {
            // Rebuild the AES key
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            
            // Recreate the exact cipher instance used for encryption
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            // Decode the Base64 string back into raw encrypted bytes
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            
            // Decrypt the bytes
            byte[] decrypted = cipher.doFinal(decoded);
            
            // Print the original password
            System.out.println("Decrypted Text: " + new String(decrypted, StandardCharsets.UTF_8));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}