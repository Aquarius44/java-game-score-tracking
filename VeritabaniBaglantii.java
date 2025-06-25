package veritabani;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class VeritabaniBaglantii {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gamescoretracking"; 
        String kullanici = "root"; 
        String sifre = ""; 

        try {
            Connection conn = DriverManager.getConnection(url, kullanici, sifre);
            System.out.println("Bağlantı başarılı!");

            
            Statement stmt = conn.createStatement();
            String sqlEkle = "INSERT INTO players (username, email) VALUES ('ahmet123', 'ahmet@example.com')";
            stmt.executeUpdate(sqlEkle);
            System.out.println("Kullanıcı eklendi.");

            
            String sqlOku = "SELECT * FROM players";
            ResultSet rs = stmt.executeQuery(sqlOku);

            while (rs.next()) {
                int id = rs.getInt("player_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String createdAt = rs.getString("created_at");

                System.out.println(id + " - " + username + " (" + email + ") - Kayıt tarihi: " + createdAt);
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Hata oluştu!");
            e.printStackTrace();
        }
    }
}
