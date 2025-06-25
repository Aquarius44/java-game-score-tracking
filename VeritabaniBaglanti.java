package veritabani;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class VeritabaniBaglanti {
 public static void main(String[] args) {
 String url = "jdbc:mysql://localhost:3306/gamescoretracking";
 String kullanici = "root";
 String sifre = ""; 
 try {
 Connection conn = DriverManager.getConnection(url, kullanici, sifre);
 System.out.println("Bağlantı başarılı!");
 conn.close();
 } catch (SQLException e) {
 System.out.println("Bağlantı başarısız!");
 e.printStackTrace();
 }
 }
}
