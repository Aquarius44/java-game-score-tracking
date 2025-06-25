package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfilSayfasi {

    private String username;

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private Label profileLabel;
    private Label usernameLabel, emailLabel, passwordLabel, photoPathLabel;
    private TextField usernameField, emailField, photoPathField;
    private PasswordField passwordField;
    private Button editButton, saveButton, cancelButton, backButton;
    private ComboBox<String> languageComboBox;
    private VBox root;

    public ProfilSayfasi(String username) {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        root = new VBox(8);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ffe6e6;");

        profileLabel = new Label();
        profileLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #800000;");

        UserInfo userInfo = getUserInfoFromDB(username);

        usernameLabel = new Label();
        emailLabel = new Label();
        passwordLabel = new Label();
        photoPathLabel = new Label();

        usernameField = new TextField(username);
        emailField = new TextField(userInfo.email != null ? userInfo.email : "");
        passwordField = new PasswordField();
        passwordField.setText(userInfo.password != null ? userInfo.password : "");
        photoPathField = new TextField(userInfo.photoPath != null ? userInfo.photoPath : "");

        usernameField.setMaxWidth(250);
        emailField.setMaxWidth(250);
        passwordField.setMaxWidth(250);
        photoPathField.setMaxWidth(250);

        usernameField.setVisible(false);
        emailField.setVisible(false);
        passwordField.setVisible(false);
        photoPathField.setVisible(false);

        String softShadowAndRadius =
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);";

        usernameField.setStyle(softShadowAndRadius);
        emailField.setStyle(softShadowAndRadius);
        passwordField.setStyle(softShadowAndRadius);
        photoPathField.setStyle(softShadowAndRadius);

        editButton = new Button();
        saveButton = new Button();
        cancelButton = new Button();
        backButton = new Button();

        editButton.setStyle(
            "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;" + softShadowAndRadius);
        saveButton.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;" + softShadowAndRadius);
        cancelButton.setStyle(
            "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;" + softShadowAndRadius);
        backButton.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;" + softShadowAndRadius);

        editButton.setPrefWidth(200);
        saveButton.setPrefWidth(200);
        cancelButton.setPrefWidth(200);
        backButton.setPrefWidth(200);

        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        ImageView profileImageView = new ImageView();
        profileImageView.setFitHeight(150);
        profileImageView.setFitWidth(150);
        profileImageView.setPreserveRatio(true);
        profileImageView.setSmooth(true);

        if (userInfo.photoPath != null && !userInfo.photoPath.isEmpty()) {
            try {
                FileInputStream fis = new FileInputStream(userInfo.photoPath);
                Image profileImage = new Image(fis);
                profileImageView.setImage(profileImage);
                fis.close();
            } catch (Exception e) {
                System.out.println("Profil fotoğrafı yüklenemedi: " + e.getMessage());
            }
        }

        photoPathField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                FileInputStream fis = new FileInputStream(newVal);
                Image img = new Image(fis);
                profileImageView.setImage(img);
                fis.close();
            } catch (Exception e) {
                profileImageView.setImage(null);
            }
        });

        editButton.setOnAction(e -> {
            toggleFields(true);
        });

        saveButton.setOnAction(e -> {
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = passwordField.getText().trim();
            String newPhotoPath = photoPathField.getText().trim();

            if (newUsername.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, isEnglish() ? "Error" : "Hata",
                        isEnglish() ? "Username, email and password cannot be empty." : "Kullanıcı adı, e-posta ve şifre boş bırakılamaz.");
                return;
            }

            boolean success = updateUserInfoInDB(username, newUsername, newEmail, newPassword, newPhotoPath);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, isEnglish() ? "Success" : "Başarılı",
                        isEnglish() ? "Your information has been updated." : "Bilgileriniz güncellendi.");
                this.username = newUsername;
                updateLabels(newUsername, newEmail, newPassword, newPhotoPath);
                toggleFields(false);
            } else {
                showAlert(Alert.AlertType.ERROR, isEnglish() ? "Error" : "Hata",
                        isEnglish() ? "Update failed." : "Güncelleme başarısız oldu.");
            }
        });

        cancelButton.setOnAction(e -> {
            usernameField.setText(usernameLabel.getText().replace(isEnglish() ? "Username: " : "Kullanıcı Adı: ", ""));
            emailField.setText(emailLabel.getText().replace(isEnglish() ? "Email: " : "E-posta: ", ""));
            passwordField.setText(passwordLabel.getText().replace(isEnglish() ? "Password: " : "Şifre: ", ""));
            photoPathField.setText(photoPathLabel.getText().replace(isEnglish() ? "Photo Path: " : "Fotoğraf Yolu: ", ""));
            toggleFields(false);
        });

        backButton.setOnAction(e -> {
            AnaSayfa anaSayfa = new AnaSayfa(username);
            try {
                anaSayfa.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue("Türkçe");
        languageComboBox.setOnAction(e -> updateLanguageTexts());

        // Görsel yapı
        VBox profileCard = new VBox(10);
        profileCard.setAlignment(Pos.CENTER);
        profileCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);");

        Label nameDisplayLabel = new Label(username);
        nameDisplayLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox imageBox = new VBox(5, profileImageView, nameDisplayLabel);
        imageBox.setAlignment(Pos.CENTER);

        VBox usernameBox = new VBox(2, usernameLabel, usernameField);
        VBox emailBox = new VBox(2, emailLabel, emailField);
        VBox passwordBox = new VBox(2, passwordLabel, passwordField);
        VBox photoPathBox = new VBox(2, photoPathLabel, photoPathField);

        usernameBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        photoPathBox.setAlignment(Pos.CENTER_LEFT);

        profileCard.getChildren().addAll(profileLabel, imageBox, usernameBox, emailBox, passwordBox, photoPathBox);

        HBox buttonsBox = new HBox(10, editButton, saveButton, cancelButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, profileCard, buttonsBox, backButton, languageComboBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        root.getChildren().add(mainLayout);
        updateLanguageTexts();

        Scene scene = new Scene(root, 650, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Profil Sayfası");
        primaryStage.setMaximized(true);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = scene.getWidth();
        double height = scene.getHeight();
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    private void toggleFields(boolean editing) {
        usernameLabel.setVisible(!editing);
        emailLabel.setVisible(!editing);
        passwordLabel.setVisible(!editing);
        photoPathLabel.setVisible(!editing);

        usernameField.setVisible(editing);
        emailField.setVisible(editing);
        passwordField.setVisible(editing);
        photoPathField.setVisible(editing);

        editButton.setVisible(!editing);
        saveButton.setVisible(editing);
        cancelButton.setVisible(editing);
    }

    private void updateLanguageTexts() {
        boolean english = isEnglish();
        profileLabel.setText(english ? "👤 Profile Information" : "👤 Profil Bilgileri");

        if (usernameLabel.isVisible() || usernameField.isVisible()) {
            String usernameText = english ? "Username: " : "Kullanıcı Adı: ";
            String emailText = english ? "Email: " : "E-posta: ";
            String passwordText = english ? "Password: " : "Şifre: ";
            String photoPathText = english ? "Photo Path: " : "Fotoğraf Yolu: ";

            if (usernameLabel.isVisible())
                usernameLabel.setText(usernameText + usernameField.getText());
            if (emailLabel.isVisible())
                emailLabel.setText(emailText + emailField.getText());
            if (passwordLabel.isVisible())
                passwordLabel.setText(passwordText + passwordField.getText());
            if (photoPathLabel.isVisible())
                photoPathLabel.setText(photoPathText + photoPathField.getText());
        }

        editButton.setText(english ? "✏️ Edit" : "✏️ Düzenle");
        saveButton.setText(english ? "💾 Save" : "💾 Kaydet");
        cancelButton.setText(english ? "❌ Cancel" : "❌ İptal");
        backButton.setText(english ? "🔙 Back" : "🔙 Geri Dön");
    }

    private boolean isEnglish() {
        return languageComboBox != null && "English".equals(languageComboBox.getValue());
    }

    private void updateLabels(String username, String email, String password, String photoPath) {
        if (isEnglish()) {
            usernameLabel.setText("Username: " + username);
            emailLabel.setText("Email: " + email);
            passwordLabel.setText("Password: " + password);
            photoPathLabel.setText("Photo Path: " + photoPath);
        } else {
            usernameLabel.setText("Kullanıcı Adı: " + username);
            emailLabel.setText("E-posta: " + email);
            passwordLabel.setText("Şifre: " + password);
            photoPathLabel.setText("Fotoğraf Yolu: " + photoPath);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean updateUserInfoInDB(String oldUsername, String newUsername, String email, String password, String photoPath) {
        String sql = "UPDATE players SET username = ?, email = ?, password = ?, photo_path = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, photoPath);
            stmt.setString(5, oldUsername);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class UserInfo {
        String email;
        String password;
        String photoPath;
    }

    private UserInfo getUserInfoFromDB(String username) {
        UserInfo userInfo = new UserInfo();
        String sql = "SELECT email, password, photo_path FROM players WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userInfo.email = rs.getString("email");
                    userInfo.password = rs.getString("password");
                    userInfo.photoPath = rs.getString("photo_path");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
