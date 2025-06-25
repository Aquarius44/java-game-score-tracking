package game;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.sql.*;

public class LoginForm extends Application {

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    @Override
    public void start(Stage primaryStage) {
        GridPane loginGrid = new GridPane();
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);
        loginGrid.setPadding(new Insets(20));
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setBackground(new Background(new BackgroundFill(Color.web("#f2f2f2"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label();
        titleLabel.setFont(Font.font("System", 26));
        titleLabel.setTextFill(Color.web("#e75480"));

        Label userLabel = new Label();
        userLabel.setFont(Font.font("System", 14));
        userLabel.setTextFill(Color.web("#333333"));

        TextField userField = new TextField();
        userField.setFont(Font.font("System", 14));
        userField.setPromptText("");
        userField.setStyle(
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        PasswordField passField = new PasswordField();
        passField.setFont(Font.font("System", 14));
        passField.setPromptText("");
        passField.setStyle(
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        TextField visiblePassField = new TextField();
        visiblePassField.setFont(Font.font("System", 14));
        visiblePassField.setManaged(false);
        visiblePassField.setVisible(false);
        visiblePassField.setStyle(
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        Button showPassButton = new Button("👁️");
        showPassButton.setFont(Font.font(14));
        showPassButton.setFocusTraversable(false);
        showPassButton.setPrefWidth(40);
        showPassButton.setStyle(
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1); " +
            "-fx-background-color: #f0f0f0;"
        );

        HBox passBox = new HBox(passField, visiblePassField, showPassButton);
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.setSpacing(0);

        passField.prefWidthProperty().bind(passBox.widthProperty().subtract(showPassButton.prefWidthProperty()));
        visiblePassField.prefWidthProperty().bind(passBox.widthProperty().subtract(showPassButton.prefWidthProperty()));

        Label passLabel = new Label();
        passLabel.setFont(Font.font("System", 14));
        passLabel.setTextFill(Color.web("#333333"));

        Button loginButton = new Button();
        loginButton.setFont(Font.font("System", 14));
        loginButton.setStyle(
            "-fx-background-color: #e75480; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(231, 84, 128, 0.6), 10, 0, 0, 0);"
        );

        Button registerButton = new Button();
        registerButton.setFont(Font.font("System", 14));
        registerButton.setStyle(
            "-fx-background-color: #ff6f91; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(255, 111, 145, 0.6), 10, 0, 0, 0);"
        );

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("System", 12));
        messageLabel.setTextFill(Color.web("#b00020"));

        Label langLabel = new Label("Dil / Language:");
        langLabel.setTextFill(Color.web("#555555"));
        ComboBox<String> languageBox = new ComboBox<>();
        languageBox.getItems().addAll("Türkçe", "English");
        languageBox.setValue("Türkçe");

        Hyperlink forgotPasswordLink = new Hyperlink();
        forgotPasswordLink.setFont(Font.font("System", 12));
        forgotPasswordLink.setTextFill(Color.DARKBLUE);
        forgotPasswordLink.setBorder(Border.EMPTY);
        forgotPasswordLink.setPadding(new Insets(0));

        BorderPane root = new BorderPane();
        root.setCenter(loginGrid);

        HBox langBox = new HBox(5, langLabel, languageBox);
        langBox.setPadding(new Insets(10));
        langBox.setAlignment(Pos.CENTER_LEFT);

        BorderPane bottomBox = new BorderPane();
        bottomBox.setLeft(langBox);
        bottomBox.setRight(forgotPasswordLink);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        HBox buttonsBox = new HBox(10, loginButton, registerButton);
        buttonsBox.setAlignment(Pos.CENTER);

        loginGrid.add(titleLabel, 0, 0, 2, 1);
        loginGrid.add(userLabel, 0, 1);
        loginGrid.add(userField, 1, 1);
        loginGrid.add(passLabel, 0, 2);
        loginGrid.add(passBox, 1, 2);
        loginGrid.add(buttonsBox, 0, 3, 2, 1);
        loginGrid.add(messageLabel, 0, 4, 2, 1);

        Runnable updateLanguage = () -> {
            String lang = languageBox.getValue();
            if ("English".equals(lang)) {
                titleLabel.setText("🎮 Game Score Tracking System 🎮");
                userLabel.setText("Username:");
                userField.setPromptText("Enter your username");
                passLabel.setText("Password:");
                passField.setPromptText("Enter your password");
                visiblePassField.setPromptText("Enter your password");
                loginButton.setText("Login");
                registerButton.setText("Register");
                messageLabel.setText("");
                langLabel.setText("Language:");
                forgotPasswordLink.setText("Forgot Password?");
            } else {
                titleLabel.setText("🎮 Oyun Skor Takip Sistemi 🎮");
                userLabel.setText("Kullanıcı Adı:");
                userField.setPromptText("Kullanıcı adınızı girin");
                passLabel.setText("Şifre:");
                passField.setPromptText("Şifrenizi girin");
                visiblePassField.setPromptText("Şifrenizi girin");
                loginButton.setText("Giriş Yap");
                registerButton.setText("Kayıt Ol");
                messageLabel.setText("");
                langLabel.setText("Dil / Language:");
                forgotPasswordLink.setText("Şifremi Unuttum?");
            }
        };

        languageBox.setOnAction(e -> updateLanguage.run());
        updateLanguage.run();

        showPassButton.setOnAction(e -> {
            if (visiblePassField.isVisible()) {
                passField.setText(visiblePassField.getText());
                passField.setManaged(true);
                passField.setVisible(true);
                visiblePassField.setManaged(false);
                visiblePassField.setVisible(false);
                showPassButton.setText("👁️");
            } else {
                visiblePassField.setText(passField.getText());
                visiblePassField.setManaged(true);
                visiblePassField.setVisible(true);
                passField.setManaged(false);
                passField.setVisible(false);
                showPassButton.setText("🔒");
            }
        });

        Runnable attemptLogin = () -> {
            String username = userField.getText().trim();
            String password = passField.isVisible() ? passField.getText() : visiblePassField.getText();
            String lang = languageBox.getValue();

            if (checkUserCredentials(username, password)) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(800), loginGrid);
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
                messageLabel.setText(lang.equals("English") ? "Incorrect username or password!" : "Hatalı kullanıcı adı veya şifre!");
            }
        };

        userField.setOnAction(e -> attemptLogin.run());
        passField.setOnAction(e -> attemptLogin.run());
        visiblePassField.setOnAction(e -> attemptLogin.run());

        loginButton.setOnAction(e -> attemptLogin.run());

        registerButton.setOnAction(e -> {
            RegisterForm registerForm = new RegisterForm();
            try {
                registerForm.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        forgotPasswordLink.setOnAction(e -> {
            ForgotPasswordForm forgotForm = new ForgotPasswordForm();
            forgotForm.start(new Stage());
        });

        // Ekran boyutu
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double width = screenBounds.getWidth() * 0.75;  
        double height = screenBounds.getHeight() * 0.75; 

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");

       
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private boolean checkUserCredentials(String username, String password) {
        String query = "SELECT * FROM players WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
