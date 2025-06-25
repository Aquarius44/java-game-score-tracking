package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArkadasSayfasi {

    private final String username;
    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking?useSSL=false&serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private VBox root;
    private Scene scene;

    private VBox arkadasListesiBox;
    private VBox userList;
    private VBox engellenenlerBox;

    public ArkadasSayfasi(String username) {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f9f9f9;");
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("👥 Arkadaşlar");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Gelen istekler
        Label gelenIstekBaslik = new Label("📩 Gelen Arkadaşlık İstekleri");
        gelenIstekBaslik.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox gelenIstekListesi = new VBox(10);
        gelenIstekListesi.setPadding(new Insets(10));
        gelenIstekListesi.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10;");

        List<String> gelenIstekler = getIncomingRequests();

        if (gelenIstekler.isEmpty()) {
            gelenIstekListesi.getChildren().add(new Label("Yeni arkadaşlık isteğiniz yok."));
        } else {
            for (String gonderen : gelenIstekler) {
                HBox istekBox = new HBox(15);
                istekBox.setAlignment(Pos.CENTER_LEFT);

                Label kullaniciLabel = new Label("👤 " + gonderen);
                kullaniciLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Button kabulEtButton = new Button("✅ Kabul Et");
                kabulEtButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                kabulEtButton.setOnAction(e -> {
                    kabulEt(gonderen);
                    gelenIstekListesi.getChildren().remove(istekBox);
                    refreshFriendList();
                    refreshUserList();
                    refreshBlockedUsersList();
                });

                Button reddetButton = new Button("❌ Reddet");
                reddetButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                reddetButton.setOnAction(e -> {
                    reddet(gonderen);
                    gelenIstekListesi.getChildren().remove(istekBox);
                });

                istekBox.getChildren().addAll(kullaniciLabel, kabulEtButton, reddetButton);
                gelenIstekListesi.getChildren().add(istekBox);
            }
        }

        ScrollPane gelenIstekScroll = new ScrollPane(gelenIstekListesi);
        gelenIstekScroll.setFitToWidth(true);
        gelenIstekScroll.setPrefHeight(150);

        // Arkadaşlar listesi
        Label arkadasBaslik = new Label("🤝 Arkadaşlarınız");
        arkadasBaslik.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        arkadasListesiBox = new VBox(10);
        arkadasListesiBox.setPadding(new Insets(10));
        arkadasListesiBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10;");
        ScrollPane arkadasScroll = new ScrollPane(arkadasListesiBox);
        arkadasScroll.setFitToWidth(true);
        arkadasScroll.setPrefHeight(150);

        refreshFriendList();

        // Tüm kullanıcılar listesi
        Label tumKullanicilarBaslik = new Label("🌐 Tüm Kullanıcılar");
        tumKullanicilarBaslik.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ScrollPane userListPane = new ScrollPane();
        userListPane.setFitToWidth(true);
        userList = new VBox(15);
        userList.setPadding(new Insets(10));
        userList.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10;");
        userListPane.setContent(userList);

        refreshUserList();

        // Engellenen kullanıcılar listesi
        Label engellenenBaslik = new Label("🚫 Engellenen Kullanıcılar");
        engellenenBaslik.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        engellenenlerBox = new VBox(10);
        engellenenlerBox.setPadding(new Insets(10));
        engellenenlerBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10;");
        ScrollPane engellenenScroll = new ScrollPane(engellenenlerBox);
        engellenenScroll.setFitToWidth(true);
        engellenenScroll.setPrefHeight(150);

        refreshBlockedUsersList();

        Button geriButton = new Button("⬅ Geri");
        geriButton.setStyle("-fx-background-color: #888; -fx-text-fill: white;");
        geriButton.setOnAction(e -> {
            AnaSayfa anaSayfa = new AnaSayfa(username);
            anaSayfa.start(primaryStage);
        });

        root.getChildren().addAll(title, gelenIstekBaslik, gelenIstekScroll, arkadasBaslik, arkadasScroll, tumKullanicilarBaslik, userListPane, engellenenBaslik, engellenenScroll, geriButton);

        scene = new Scene(root, 800, 1000);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Arkadaşlar");

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - 800) / 2);
        primaryStage.setY((screenBounds.getHeight() - 1000) / 2);

        primaryStage.show();
    }

    private void refreshUserList() {
        userList.getChildren().clear();

        List<String> players = getRegisteredUsers();
        List<String> sentRequests = getSentRequests();
        List<String> friends = getFriends();
        List<String> blockedUsers = getBlockedUsers();

        for (String kullanici : players) {
            if (kullanici.equals(username)) continue;
            if (friends.contains(kullanici)) continue;
            if (blockedUsers.contains(kullanici)) continue;

            HBox userBox = new HBox(15);
            userBox.setPadding(new Insets(15));
            userBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 12;");
            userBox.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label("👤 " + kullanici);
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Button istekButton = new Button();
            if (sentRequests.contains(kullanici)) {
                istekButton.setText("↩ Geri Çek");
                istekButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                istekButton.setOnAction(e -> {
                    istekGeriCek(kullanici);
                    refreshUserList();
                });
            } else {
                istekButton.setText("➕ İstek Yolla");
                istekButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                istekButton.setOnAction(e -> {
                    istekYolla(kullanici);
                    refreshUserList();
                });
            }

            Button engelleButton = new Button("🚫 Engelle");
            engelleButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
            engelleButton.setOnAction(e -> {
                engelle(kullanici);
                refreshUserList();
                refreshFriendList();
                refreshBlockedUsersList();
            });

            userBox.getChildren().addAll(nameLabel, istekButton, engelleButton);
            userList.getChildren().add(userBox);
        }
    }

    private void refreshFriendList() {
        arkadasListesiBox.getChildren().clear();
        List<String> friends = getFriends();
        if (friends.isEmpty()) {
            arkadasListesiBox.getChildren().add(new Label("Henüz arkadaşınız yok."));
        } else {
            for (String friend : friends) {
                HBox friendBox = new HBox(10);
                friendBox.setAlignment(Pos.CENTER_LEFT);

                Label friendLabel = new Label("👤 " + friend);
                friendLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Button engelleFriendButton = new Button("🚫 Engelle");
                engelleFriendButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                engelleFriendButton.setOnAction(e -> {
                    engelle(friend);
                    refreshFriendList();
                    refreshUserList();
                    refreshBlockedUsersList();
                });

                friendBox.getChildren().addAll(friendLabel, engelleFriendButton);
                arkadasListesiBox.getChildren().add(friendBox);
            }
        }
    }

    private void refreshBlockedUsersList() {
        engellenenlerBox.getChildren().clear();

        List<String> blockedUsers = getBlockedUsers();
        if (blockedUsers.isEmpty()) {
            engellenenlerBox.getChildren().add(new Label("Engellenen kullanıcı yok."));
        } else {
            for (String blockedUser : blockedUsers) {
                HBox blockedBox = new HBox(15);
                blockedBox.setAlignment(Pos.CENTER_LEFT);

                Label blockedLabel = new Label("👤 " + blockedUser);
                blockedLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Button kaldirButton = new Button("❌ Engeli Kaldır");
                kaldirButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                kaldirButton.setOnAction(e -> {
                    engelleKaldir(blockedUser);
                    refreshBlockedUsersList();
                    refreshUserList();
                    refreshFriendList();
                });

                blockedBox.getChildren().addAll(blockedLabel, kaldirButton);
                engellenenlerBox.getChildren().add(blockedBox);
            }
        }
    }

    //  gelen istekleri al
    private List<String> getIncomingRequests() {
        List<String> incomingRequests = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT sender_username FROM friend_requests WHERE receiver_username = ? AND status = 'pending'")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                incomingRequests.add(rs.getString("sender_username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomingRequests;
    }

    //  kayıtlı kullanıcıları al
    private List<String> getRegisteredUsers() {
        List<String> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM players")) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Gönderilen istekler
    private List<String> getSentRequests() {
        List<String> sentRequests = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT receiver_username FROM friend_requests WHERE sender_username = ? AND status = 'pending'")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sentRequests.add(rs.getString("receiver_username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sentRequests;
    }

    // Arkadaşlar
    private List<String> getFriends() {
        List<String> friends = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT sender_username FROM friend_requests WHERE receiver_username = ? AND status = 'accepted' " +
                             "UNION " +
                             "SELECT receiver_username FROM friend_requests WHERE sender_username = ? AND status = 'accepted'")) {
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                friends.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    // Engellenen kullanıcılar
    private List<String> getBlockedUsers() {
        List<String> blockedUsers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT blocked_username FROM blocked_users WHERE blocker_username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                blockedUsers.add(rs.getString("blocked_username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blockedUsers;
    }

    // İstek gönder
    private void istekYolla(String hedefKullanici) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO friend_requests (sender_username, receiver_username, status) VALUES (?, ?, 'pending')")) {
            ps.setString(1, username);
            ps.setString(2, hedefKullanici);
            ps.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Arkadaşlık İsteği", hedefKullanici + " kullanıcısına istek gönderildi.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Arkadaşlık isteği gönderilemedi!");
        }
    }

    // İstek geri çek
    private void istekGeriCek(String hedefKullanici) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM friend_requests WHERE sender_username = ? AND receiver_username = ? AND status = 'pending'")) {
            ps.setString(1, username);
            ps.setString(2, hedefKullanici);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "İstek Geri Çekildi", hedefKullanici + " kullanıcısına gönderilen istek geri çekildi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "İstek geri çekilemedi!");
        }
    }

    // İstek kabul et
    private void kabulEt(String gonderen) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE friend_requests SET status = 'accepted' WHERE sender_username = ? AND receiver_username = ? AND status = 'pending'")) {
            ps.setString(1, gonderen);
            ps.setString(2, username);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "İstek Kabul Edildi", gonderen + " kullanıcısının isteği kabul edildi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "İstek kabul edilirken hata oluştu!");
        }
    }

    // İstek reddet
    private void reddet(String gonderen) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM friend_requests WHERE sender_username = ? AND receiver_username = ? AND status = 'pending'")) {
            ps.setString(1, gonderen);
            ps.setString(2, username);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "İstek Reddedildi", gonderen + " kullanıcısının isteği reddedildi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "İstek reddedilirken hata oluştu!");
        }
    }

    // Engelle
    private void engelle(String hedefKullanici) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Önce arkadaşlık varsa sil
            PreparedStatement psSilArkadaslik = conn.prepareStatement(
                    "DELETE FROM friend_requests WHERE (sender_username = ? AND receiver_username = ?) OR (sender_username = ? AND receiver_username = ?)");
            psSilArkadaslik.setString(1, username);
            psSilArkadaslik.setString(2, hedefKullanici);
            psSilArkadaslik.setString(3, hedefKullanici);
            psSilArkadaslik.setString(4, username);
            psSilArkadaslik.executeUpdate();

            // Sonra engelleme kaydı ekle (önceden varsa hata verir, bunu engellemek için kontrol yapabiliriz)
            PreparedStatement psKontrol = conn.prepareStatement(
                    "SELECT * FROM blocked_users WHERE blocker_username = ? AND blocked_username = ?");
            psKontrol.setString(1, username);
            psKontrol.setString(2, hedefKullanici);
            ResultSet rs = psKontrol.executeQuery();

            if (!rs.next()) {
                PreparedStatement psEngelle = conn.prepareStatement(
                        "INSERT INTO blocked_users (blocker_username, blocked_username) VALUES (?, ?)");
                psEngelle.setString(1, username);
                psEngelle.setString(2, hedefKullanici);
                psEngelle.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Kullanıcı Engellendi", hedefKullanici + " engellendi.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Zaten Engellenmiş", hedefKullanici + " zaten engellenmiş.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Kullanıcı engellenirken hata oluştu!");
        }
    }

    // Engelle kaldır
    private void engelleKaldir(String hedefKullanici) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM blocked_users WHERE blocker_username = ? AND blocked_username = ?")) {
            ps.setString(1, username);
            ps.setString(2, hedefKullanici);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Engel Kaldırıldı", hedefKullanici + " engeli kaldırıldı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Engel kaldırılırken hata oluştu!");
        }
    }

    private void showAlert(Alert.AlertType type, String baslik, String mesaj) {
        Alert alert = new Alert(type);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

}
