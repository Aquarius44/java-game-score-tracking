package veritabani;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class VeritabaniEkleme {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gamescoretracking"; 
        String username = "root"; 
        String password = ""; 

        Scanner scanner = new Scanner(System.in);

        System.out.print("Kullanıcı adınızı girin: ");
        String inputUsername = scanner.nextLine();

        System.out.print("E-posta adresinizi girin: ");
        String inputEmail = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO players (username, email) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, inputUsername);
            statement.setString(2, inputEmail);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Kullanıcı başarıyla eklendi!");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }

        scanner.close();
    }
}
