package veritabani;

import java.sql.*;
import java.util.Scanner;

public class KullaniciYonetimSistemi {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gamescoretracking"; 
        String username = "root"; 
        String password = ""; 

        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Veritabanı bağlantısı başarılı!");
            int secim;

            do {
                System.out.println("\n--- KULLANICI YÖNETİM SİSTEMİ ---");
                System.out.println("1 - Yeni Kayıt Ekle");
                System.out.println("2 - Kayıtları Listele");
                System.out.println("3 - Kayıt Güncelle");
                System.out.println("4 - Kayıt Sil");
                System.out.println("5 - Çıkış");
                System.out.print("Seçiminiz: ");
                secim = Integer.parseInt(scanner.nextLine());

                switch (secim) {
                    case 1: 
                        System.out.print("Kullanıcı Adı: ");
                        String kullaniciAdi = scanner.nextLine();
                        System.out.print("E-posta: ");
                        String email = scanner.nextLine();
                        System.out.print("Şifre: ");
                        String sifre = scanner.nextLine();

                        String insertSql = "INSERT INTO players (username, email, password) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, kullaniciAdi);
                            insertStmt.setString(2, email);
                            insertStmt.setString(3, sifre);
                            insertStmt.executeUpdate();
                            System.out.println("Kayıt başarıyla eklendi.");
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            System.out.println("Hata: Bu kullanıcı adı veya e-posta zaten mevcut.");
                        }
                        break;

                    case 2: 
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery("SELECT player_id, username, email, created_at FROM players")) {
                            System.out.println("\n--- Kayıtlar ---");
                            while (rs.next()) {
                                System.out.println(
                                    rs.getInt("player_id") + " - " +
                                    rs.getString("username") + " | " +
                                    rs.getString("email") + " | " +
                                    rs.getString("created_at")
                                );
                            }
                        }
                        break;

                    case 3: 
                        System.out.print("Güncellenecek player_id: ");
                        int guncelleId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Yeni Kullanıcı Adı: ");
                        String yeniKullaniciAdi = scanner.nextLine();
                        System.out.print("Yeni E-posta: ");
                        String yeniEmail = scanner.nextLine();
                        System.out.print("Yeni Şifre: ");
                        String yeniSifre = scanner.nextLine();

                        String updateSql = "UPDATE players SET username = ?, email = ?, password = ? WHERE player_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, yeniKullaniciAdi);
                            updateStmt.setString(2, yeniEmail);
                            updateStmt.setString(3, yeniSifre);
                            updateStmt.setInt(4, guncelleId);
                            int affected = updateStmt.executeUpdate();
                            if (affected > 0) {
                                System.out.println("Kayıt başarıyla güncellendi.");
                            } else {
                                System.out.println("Belirtilen ID ile kayıt bulunamadı.");
                            }
                        }
                        break;

                    case 4: 
                        System.out.print("Silinecek player_id: ");
                        int silinecekId = Integer.parseInt(scanner.nextLine());

                        String deleteSql = "DELETE FROM players WHERE player_id = ?";
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                            deleteStmt.setInt(1, silinecekId);
                            int affected = deleteStmt.executeUpdate();
                            if (affected > 0) {
                                System.out.println("Kayıt silindi.");
                            } else {
                                System.out.println("Belirtilen ID ile kayıt bulunamadı.");
                            }
                        }
                        break;

                    case 5:
                        System.out.println("Programdan çıkılıyor...");
                        break;

                    default:
                        System.out.println("Geçersiz seçim!");
                        break;
                }

            } while (secim != 5);

        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }

        scanner.close();
    }
}
