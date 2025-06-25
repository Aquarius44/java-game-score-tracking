package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;

public class MesajSayfasi {

    private final String username;
    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private ComboBox<String> languageComboBox, userList;
    private TextArea chatBox;
    private TextField messageField;
    private Button sendButton, clearButton, backButton;
    private boolean isEnglish = false;
    private String selectedUser;

    public MesajSayfasi(String username) {
        this.username = username;
    }

    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #2c003e;");

        // Dil seçimi
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue("Türkçe");
        languageComboBox.setOnAction(e -> {
            isEnglish = languageComboBox.getValue().equals("English");
            updateTexts();
            loadUsers();
            chatBox.clear();
        });

        Label title = new Label("💬 Mesajlaşma Ekranı");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Kullanıcı listesi
        userList = new ComboBox<>();
        userList.setPrefWidth(200);
        userList.setPromptText(isEnglish ? "Select user" : "Kullanıcı seç");
        userList.setOnAction(e -> {
            selectedUser = userList.getValue();
            loadMessages();
        });

        clearButton = new Button();
        clearButton.setOnAction(e -> {
            if (selectedUser != null) {
                chatBox.clear();
            }
        });

        chatBox = new TextArea();
        chatBox.setEditable(false);
        chatBox.setWrapText(true);
        chatBox.setPrefHeight(300);
        chatBox.setStyle("-fx-control-inner-background: #3a0f66; -fx-text-fill: white;");

        messageField = new TextField();
        messageField.setPrefWidth(400);
        messageField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                sendMessage(messageField.getText());
            }
        });

        sendButton = new Button();
        sendButton.setOnAction(e -> sendMessage(messageField.getText()));

        backButton = new Button("🏠 Ana Sayfa");
        backButton.setOnAction(e -> {
            try {
                new AnaSayfa(username).start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        updateTexts();
        loadUsers();

        HBox topBar = new HBox(8, languageComboBox, userList, clearButton);
        topBar.setAlignment(Pos.CENTER);
        HBox bottomBar = new HBox(8, messageField, sendButton);

        root.getChildren().addAll(title, topBar, chatBox, bottomBar, backButton);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.6;
        double height = screenBounds.getHeight() * 0.6;

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        stage.setTitle("Mesajlaşma");
        stage.show();
    }

    private void updateTexts() {
        sendButton.setText(isEnglish ? "Send" : "Gönder");
        clearButton.setText(isEnglish ? "Clear" : "Temizle");
        messageField.setPromptText(isEnglish ? "Type and press Enter..." : "Mesaj yaz ve Enter'a bas...");
        userList.setPromptText(isEnglish ? "Select user" : "Kullanıcı seç");
    }

    private void loadUsers() {
        userList.getItems().clear();

      
        String sql = "SELECT username FROM players WHERE username <> ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String user = rs.getString("username");
                userList.getItems().add(user);
                System.out.println("Loaded user: " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg) {
        if (selectedUser == null || msg.trim().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO messages(sender_username, receiver_username, message, is_read) VALUES (?, ?, ?, FALSE)"
            );
            ps.setString(1, username);
            ps.setString(2, selectedUser);
            ps.setString(3, msg);
            ps.executeUpdate();

            messageField.clear();
            loadMessages();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {
        chatBox.clear();

        if (selectedUser == null) return;

        String sql = "SELECT sender_username, message FROM messages " +
                     "WHERE (sender_username = ? AND receiver_username = ?) OR (sender_username = ? AND receiver_username = ?) " +
                     "ORDER BY id";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, selectedUser);
            ps.setString(3, selectedUser);
            ps.setString(4, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender_username");
                String msg = rs.getString("message");
                String prefix = sender.equals(username) ? "Sen: " : selectedUser + ": ";
                chatBox.appendText(prefix + msg + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
