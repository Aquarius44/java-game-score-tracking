package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;

public class AnaSayfa {

    private String username;
    private String dil = "Türkçe";

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Label titleLabel;
    private Label welcomeLabel;

    private Button newScoreButton;
    private Button viewScoresButton;
    private Button allScoresButton;
    private Button friendsButton;
    private Button messageButton;
    private Button profileButton;
    private Button logoutButton;

    private ComboBox<String> languageComboBox;
    private ToggleButton themeToggle;

    private VBox root;
    private Scene scene;

    public AnaSayfa(String username) {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.75;
        double height = screenBounds.getHeight() * 0.75;

        scene = new Scene(root, width, height);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ana Sayfa");

        // Pencere ekran ortası
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);

        initializeUI();
        updateTextsAndStyles();
        addEventHandlers(primaryStage);

        primaryStage.show();
    }

    private void initializeUI() {
        titleLabel = new Label();
        welcomeLabel = new Label();

        newScoreButton = new Button();
        viewScoresButton = new Button();
        allScoresButton = new Button();
        friendsButton = new Button();
        messageButton = new Button();
        profileButton = new Button();
        logoutButton = new Button();

        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue(dil);

        themeToggle = new ToggleButton();
        themeToggle.setSelected(ThemeManager.darkMode);

        int buttonWidth = 250;
        newScoreButton.setPrefWidth(buttonWidth);
        viewScoresButton.setPrefWidth(buttonWidth);
        allScoresButton.setPrefWidth(buttonWidth);
        friendsButton.setPrefWidth(buttonWidth);
        messageButton.setPrefWidth(buttonWidth);
        profileButton.setPrefWidth(buttonWidth);
        logoutButton.setPrefWidth(buttonWidth);
        themeToggle.setPrefWidth(buttonWidth);

        VBox.setMargin(languageComboBox, new Insets(30, 0, 0, 0));
        VBox.setMargin(themeToggle, new Insets(10, 0, 0, 0));

        root.getChildren().setAll(
                titleLabel, welcomeLabel,
                newScoreButton,
                viewScoresButton,
                allScoresButton,
                friendsButton,
                messageButton,
                profileButton,
                logoutButton,
                languageComboBox,
                themeToggle
        );
    }

    private void updateTextsAndStyles() {
        int unreadCount = getUnreadMessageCount(username);
        String badge = unreadCount > 0 ? " 🔔(" + unreadCount + ")" : "";

        boolean isEnglish = languageComboBox.getValue().equals("English");
        dil = isEnglish ? "English" : "Türkçe";

        // Metinler
        titleLabel.setText(isEnglish ? "🎮 Game Score Tracking System 🎮" : "🎮 Oyun Skor Takip Sistemi 🎮");
        welcomeLabel.setText(isEnglish ? "Welcome, " + username + "!" : "Hoşgeldin, " + username + "!");
        newScoreButton.setText(isEnglish ? "🎯 Add New Score" : "🎯 Yeni Skor Ekle");
        viewScoresButton.setText(isEnglish ? "📊 View My Scores" : "📊 Skorlarımı Görüntüle");
        allScoresButton.setText(isEnglish ? "🌍 View All Scores" : "🌍 Tüm Skorları Gör");
        friendsButton.setText(isEnglish ? "👥 Friends" : "👥 Arkadaşlar");
        messageButton.setText((isEnglish ? "💬 Messages" : "💬 Mesajlar") + badge);
        profileButton.setText(isEnglish ? "👤 My Profile" : "👤 Profilim");
        logoutButton.setText(isEnglish ? "❌ Logout" : "❌ Çıkış Yap");
        updateToggleText(themeToggle);

        // Stil ayarları
        String baseButtonStyle = "-fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 4,0,0,1 );";
        String greenLight = "-fx-background-color: #4CAF50; -fx-text-fill: white;";
        String greenDark = "-fx-background-color: #2E7D32; -fx-text-fill: white;";
        String redLight = "-fx-background-color: #f44336; -fx-text-fill: white;";
        String redDark = "-fx-background-color: #b71c1c; -fx-text-fill: white;";
        String messageDarkAlert = "-fx-background-color: #d32f2f; -fx-text-fill: white;";
        String messageLightAlert = "-fx-background-color: #ff4444; -fx-text-fill: white;";

        if (ThemeManager.darkMode) {
            root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
            titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px;");
            welcomeLabel.setStyle("-fx-text-fill: white;");
            newScoreButton.setStyle(greenDark + baseButtonStyle);
            viewScoresButton.setStyle(greenDark + baseButtonStyle);
            allScoresButton.setStyle(greenDark + baseButtonStyle);
            friendsButton.setStyle(greenDark + baseButtonStyle);
            messageButton.setStyle(unreadCount > 0 ? messageDarkAlert + baseButtonStyle : greenDark + baseButtonStyle);
            profileButton.setStyle(greenDark + baseButtonStyle);
            logoutButton.setStyle(redDark + baseButtonStyle);
        } else {
            root.setStyle("-fx-background-color: #ffe6e6; -fx-text-fill: black;");
            titleLabel.setStyle("-fx-text-fill: #d45d79; -fx-font-weight: bold; -fx-font-size: 24px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.15) , 3,0,0,1 );");
            welcomeLabel.setStyle("-fx-text-fill: black;");
            newScoreButton.setStyle(greenLight + baseButtonStyle);
            viewScoresButton.setStyle(greenLight + baseButtonStyle);
            allScoresButton.setStyle(greenLight + baseButtonStyle);
            friendsButton.setStyle(greenLight + baseButtonStyle);
            messageButton.setStyle(unreadCount > 0 ? messageLightAlert + baseButtonStyle : greenLight + baseButtonStyle);
            profileButton.setStyle(greenLight + baseButtonStyle);
            logoutButton.setStyle(redLight + baseButtonStyle);
        }
    }

    private void addEventHandlers(Stage primaryStage) {
        languageComboBox.setOnAction(e -> updateTextsAndStyles());

        newScoreButton.setOnAction(e -> {
            int playerId = getPlayerIdFromUsername(username);
            if (playerId == -1) {
                showAlert(Alert.AlertType.ERROR, dil.equals("English") ? "User ID not found!" : "Kullanıcı ID'si bulunamadı!");
                return;
            }
            SkorEkle skorEkle = new SkorEkle(username, playerId);
            try {
                skorEkle.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        viewScoresButton.setOnAction(e -> {
            Skorlarim skorlarim = new Skorlarim(username);
            try {
                skorlarim.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        allScoresButton.setOnAction(e -> {
            AllScoresPage allScoresPage = new AllScoresPage(username);
            allScoresPage.start(primaryStage);
        });
        
        friendsButton.setOnAction(e -> {
            ArkadasSayfasi arkadasSayfasi = new ArkadasSayfasi(username);
            try {
                arkadasSayfasi.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        messageButton.setOnAction(e -> {
            MesajSayfasi mesajSayfasi = new MesajSayfasi(username);
            mesajSayfasi.start(primaryStage);
        });

        profileButton.setOnAction(e -> {
            ProfilSayfasi profilSayfasi = new ProfilSayfasi(username);
            try {
                profilSayfasi.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> {
            LoginForm loginForm = new LoginForm();
            try {
                loginForm.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        themeToggle.setOnAction(e -> {
            ThemeManager.darkMode = themeToggle.isSelected();
            updateToggleText(themeToggle);
            updateTextsAndStyles();
        });
    }

    private void updateToggleText(ToggleButton toggle) {
        if (toggle.isSelected()) {
            toggle.setText(dil.equals("English") ? "Dark Mode" : "Koyu Tema");
        } else {
            toggle.setText(dil.equals("English") ? "Light Mode" : "Açık Tema");
        }
    }

    private int getPlayerIdFromUsername(String username) {
        int playerId = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT player_id FROM players WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                playerId = rs.getInt("player_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerId;
    }

    private int getUnreadMessageCount(String username) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_username = ? AND is_read = FALSE";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(dil.equals("English") ? "Error" : "Hata");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
