package game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SkorlarimData {

    private static List<Skor> skorListesi = new ArrayList<>();

    
    public static void skorEkle(Skor skor) {
        skorListesi.add(skor);
    }

    // Skor listesini döner
    public static List<Skor> getSkorListesi() {
        return skorListesi;
    }

    // Veritabanından skorları çekip listeyi günceller
    public static void skorlariVeritabanindanYukle() {
        skorListesi.clear();

        String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
        String DB_USER = "root";
        String DB_PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT username, game, score FROM scores";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String oyunAdi = rs.getString("game");
                double skor = rs.getDouble("score");

                skorListesi.add(new Skor(username, oyunAdi, skor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
