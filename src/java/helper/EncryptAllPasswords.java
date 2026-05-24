package helper;

import java.sql.*;

//
//
//
//
//
//DO NOT USE IF PASSWORDS ARE ENCRYPTED ALREADY!!!!!!!!!
//
//
//
//
//
//

public class EncryptAllPasswords {
    public static void main(String[] args) {
        String secretKey = "w0Rkl04Dh@$h_keY";
        String instance = "AES/ECB/PKCS5Padding";
        
        try (Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/identity_db", "app", "app")) {
            String selectSql = "SELECT USERNAME, PASSWORD_HASH "
                + "FROM DERBY_USERS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSql);

            String updateSql = "UPDATE DERBY_USERS SET PASSWORD_HASH = ? WHERE USERNAME = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);

            while (rs.next()) {
                String user = rs.getString("USERNAME");
                String plainPass = rs.getString("PASSWORD_HASH");

                String encryptedPass = Encrypter.encrypt(plainPass, secretKey, instance );

                pstmt.setString(1, encryptedPass);
                pstmt.setString(2, user);
                pstmt.executeUpdate();
                System.out.println("Encrypted password for: " + user);
            }
            System.out.println("Encryption complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
