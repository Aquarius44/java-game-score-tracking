package game;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class RegisterForm extends Application {

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    @Override
    public void start(Stage primaryStage) {
        // Ekran boyutlarını al
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Pencere boyutları
        double windowWidth = screenWidth * 1.5;
        double windowHeight = screenHeight * 0.7;

        // Minimum ve maksimum 
        windowWidth = Math.max(400, Math.min(windowWidth, 800));
        windowHeight = Math.max(500, Math.min(windowHeight, 900));
        primaryStage.setMaximized(true);
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        grid.setBackground(new Background(new BackgroundFill(
                Color.web("#121212"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label("🎮 Kayıt Ol 🎮");
        titleLabel.setFont(Font.font("System", 26));
        titleLabel.setTextFill(Color.web("#80cbc4")); 

        Label userLabel = new Label("Kullanıcı Adı:");
        userLabel.setTextFill(Color.LIGHTGRAY);
        TextField userField = new TextField();
        userField.setPromptText("Kullanıcı adınızı girin");

        Label emailLabel = new Label("E-posta:");
        emailLabel.setTextFill(Color.LIGHTGRAY);
        TextField emailField = new TextField();
        emailField.setPromptText("E-posta adresinizi girin");

        Label passLabel = new Label("Şifre:");
        passLabel.setTextFill(Color.LIGHTGRAY);
        PasswordField passField = new PasswordField();
        passField.setPromptText("Şifrenizi girin");

        Label passConfirmLabel = new Label("Şifre Tekrar:");
        passConfirmLabel.setTextFill(Color.LIGHTGRAY);
        PasswordField passConfirmField = new PasswordField();
        passConfirmField.setPromptText("Şifrenizi tekrar girin");

        Label photoLabel = new Label("Profil Fotoğrafı:");
        photoLabel.setTextFill(Color.LIGHTGRAY);
        Button photoButton = new Button("Fotoğraf Seç");
        styleButton(photoButton);
        Label photoPathLabel = new Label();
        photoPathLabel.setFont(Font.font("System", 12));
        photoPathLabel.setTextFill(Color.GRAY);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        VBox photoBox = new VBox(5, photoButton, photoPathLabel, imageView);
        photoBox.setAlignment(Pos.CENTER_LEFT);

        Button registerButton = new Button("Kayıt Ol");
        styleButton(registerButton);

        Button backButton = new Button("Geri Dön");
        styleButton(backButton);

        HBox buttonBox = new HBox(15, backButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("System", 12));
        messageLabel.setTextFill(Color.web("#ff6e6e")); 

        Label langLabel = new Label("Dil / Language:");
        langLabel.setTextFill(Color.LIGHTGRAY);
        ComboBox<String> languageBox = new ComboBox<>();
        languageBox.getItems().addAll("Türkçe", "English");
        languageBox.setValue("Türkçe");

        HBox langBox = new HBox(10, langLabel, languageBox);
        langBox.setAlignment(Pos.CENTER_LEFT);
        langBox.setPadding(new Insets(15, 0, 0, 0));

        final String[] selectedPhotoPath = {null};

        photoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Profil Fotoğrafı Seç");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Resim Dosyaları", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                selectedPhotoPath[0] = selectedFile.getAbsolutePath();
                photoPathLabel.setText(selectedFile.getName());
                try {
                    Image image = new Image(new FileInputStream(selectedFile));
                    imageView.setImage(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.add(userLabel, 0, 1);
        grid.add(userField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(passLabel, 0, 3);
        grid.add(passField, 1, 3);
        grid.add(passConfirmLabel, 0, 4);
        grid.add(passConfirmField, 1, 4);
        grid.add(photoLabel, 0, 5);
        grid.add(photoBox, 1, 5);
        grid.add(buttonBox, 1, 6);
        grid.add(messageLabel, 1, 7);
        grid.add(langBox, 1, 8);

        Scene scene = new Scene(grid, windowWidth, windowHeight);

        Runnable updateLanguage = () -> {
            String lang = languageBox.getValue();
            if ("English".equals(lang)) {
                titleLabel.setText("🎮 Register 🎮");
                userLabel.setText("Username:");
                userField.setPromptText("Enter your username");
                emailLabel.setText("Email:");
                emailField.setPromptText("Enter your email");
                passLabel.setText("Password:");
                passField.setPromptText("Enter your password");
                passConfirmLabel.setText("Confirm Password:");
                passConfirmField.setPromptText("Re-enter your password");
                registerButton.setText("Register");
                backButton.setText("Back");
                photoLabel.setText("Profile Photo:");
                langLabel.setText("Language:");
            } else {
                titleLabel.setText("🎮 Kayıt Ol 🎮");
                userLabel.setText("Kullanıcı Adı:");
                userField.setPromptText("Kullanıcı adınızı girin");
                emailLabel.setText("E-posta:");
                emailField.setPromptText("E-posta adresinizi girin");
                passLabel.setText("Şifre:");
                passField.setPromptText("Şifrenizi girin");
                passConfirmLabel.setText("Şifre Tekrar:");
                passConfirmField.setPromptText("Şifrenizi tekrar girin");
                registerButton.setText("Kayıt Ol");
                backButton.setText("Geri Dön");
                photoLabel.setText("Profil Fotoğrafı:");
                langLabel.setText("Dil / Language:");
            }
        };
        languageBox.setOnAction(e -> updateLanguage.run());
        updateLanguage.run();

        registerButton.setOnAction(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = passField.getText();
            String confirmPassword = passConfirmField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Please fill all fields." : "Lütfen tüm alanları doldurun!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Passwords do not match." : "Şifreler uyuşmuyor!");
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Invalid email address." : "Geçersiz e-posta adresi.");
                return;
            }

            if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")) {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Password must be at least 6 characters and include letters and numbers." :
                        "Şifre en az 6 karakter olmalı ve harf ile rakam içermelidir.");
                return;
            }

            if (registerUser(username, email, password, selectedPhotoPath[0])) {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Registration successful! Redirecting..." : "Kayıt başarılı! Yönlendiriliyorsunuz...");

                FadeTransition fadeOut = new FadeTransition(Duration.millis(800), grid);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event -> {
                    AnaSayfa anaSayfa = new AnaSayfa(username);
                    try {
                        anaSayfa.start(primaryStage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                fadeOut.play();

            } else {
                messageLabel.setText(languageBox.getValue().equals("English") ?
                        "Registration failed! Username may be taken." : "Kayıt başarısız! Kullanıcı adı alınmış olabilir.");
            }
        });

        backButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(800), grid);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                LoginForm loginForm = new LoginForm();
                try {
                    loginForm.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Kayıt Ol");
        primaryStage.show();
    }

    private void styleButton(Button btn) {
        btn.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #26a69a, #004d40);" + 
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 8 15 8 15;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #004d40, #26a69a);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 8 15 8 15;"
        ));
        btn.setOnMouseExited(e -> styleButton(btn));
    }

    private boolean registerUser(String username, String email, String password, String photoPath) {
        String sql = "INSERT INTO players (username, email, password, photo_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            String savedPath = null;
            if (photoPath != null) {
                File source = new File(photoPath);
                File destDir = new File("profile_photos");
                destDir.mkdir();
                File dest = new File(destDir, username + "_" + source.getName());
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                savedPath = dest.getPath();
            }

            stmt.setString(4, savedPath);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
