package game;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;

public class ForgotPasswordForm {

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    public void start(Stage stage) {
        Label title = new Label("🔐 Şifre Sıfırlama");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #4a148c; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.3, 0, 5);");

        TextField emailField = new TextField();
        emailField.setPromptText("E-posta adresi");
        styleTextField(emailField);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Yeni şifre");
        styleTextField(newPassField);

        TextField visibleNewPassField = new TextField();
        visibleNewPassField.setPromptText("Yeni şifre");
        styleTextField(visibleNewPassField);
        visibleNewPassField.setManaged(false);
        visibleNewPassField.setVisible(false);

        Button showNewPassButton = new Button("👁️");
        showNewPassButton.setStyle("-fx-font-size: 14px; -fx-background-color: transparent;");
        showNewPassButton.setFocusTraversable(false);

        showNewPassButton.setOnAction(e -> {
            if (visibleNewPassField.isVisible()) {
                newPassField.setText(visibleNewPassField.getText());
                visibleNewPassField.setVisible(false);
                visibleNewPassField.setManaged(false);
                newPassField.setVisible(true);
                newPassField.setManaged(true);
                showNewPassButton.setText("👁️");
            } else {
                visibleNewPassField.setText(newPassField.getText());
                newPassField.setVisible(false);
                newPassField.setManaged(false);
                visibleNewPassField.setVisible(true);
                visibleNewPassField.setManaged(true);
                showNewPassButton.setText("🔒");
            }
        });

        HBox newPassBox = new HBox(visibleNewPassField, newPassField, showNewPassButton);
        newPassBox.setAlignment(Pos.CENTER_LEFT);
        newPassBox.setSpacing(0);
        HBox.setHgrow(newPassField, Priority.ALWAYS);
        HBox.setHgrow(visibleNewPassField, Priority.ALWAYS);

        PasswordField repeatPassField = new PasswordField();
        repeatPassField.setPromptText("Yeni şifre (tekrar)");
        styleTextField(repeatPassField);

        TextField visibleRepeatPassField = new TextField();
        visibleRepeatPassField.setPromptText("Yeni şifre (tekrar)");
        styleTextField(visibleRepeatPassField);
        visibleRepeatPassField.setManaged(false);
        visibleRepeatPassField.setVisible(false);

        Button showRepeatPassButton = new Button("👁️");
        showRepeatPassButton.setStyle("-fx-font-size: 14px; -fx-background-color: transparent;");
        showRepeatPassButton.setFocusTraversable(false);

        showRepeatPassButton.setOnAction(e -> {
            if (visibleRepeatPassField.isVisible()) {
                repeatPassField.setText(visibleRepeatPassField.getText());
                visibleRepeatPassField.setVisible(false);
                visibleRepeatPassField.setManaged(false);
                repeatPassField.setVisible(true);
                repeatPassField.setManaged(true);
                showRepeatPassButton.setText("👁️");
            } else {
                visibleRepeatPassField.setText(repeatPassField.getText());
                repeatPassField.setVisible(false);
                repeatPassField.setManaged(false);
                visibleRepeatPassField.setVisible(true);
                visibleRepeatPassField.setManaged(true);
                showRepeatPassButton.setText("🔒");
            }
        });

        HBox repeatPassBox = new HBox(visibleRepeatPassField, repeatPassField, showRepeatPassButton);
        repeatPassBox.setAlignment(Pos.CENTER_LEFT);
        repeatPassBox.setSpacing(0);
        HBox.setHgrow(repeatPassField, Priority.ALWAYS);
        HBox.setHgrow(visibleRepeatPassField, Priority.ALWAYS);

        Button resetButton = new Button("Şifreyi Sıfırla");
        resetButton.setStyle("-fx-background-color: #4a148c; -fx-text-fill: white; -fx-background-radius: 6;");
        resetButton.setPrefWidth(140);

        // **Geri Dön Butonu**
        Button backButton = new Button("← Geri Dön");
        backButton.setStyle("-fx-background-color: #888888; -fx-text-fill: white; -fx-background-radius: 6;");
        backButton.setPrefWidth(100);

        Label resultLabel = new Label();
        resultLabel.setTextFill(Color.DARKRED);
        resultLabel.setStyle("-fx-font-size: 13px;");

        formGrid.add(new Label("E-posta:"), 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(new Label("Yeni Şifre:"), 0, 1);
        formGrid.add(newPassBox, 1, 1);
        formGrid.add(new Label("Tekrar Şifre:"), 0, 2);
        formGrid.add(repeatPassBox, 1, 2);

      
        HBox buttonsBox = new HBox(15, backButton, resetButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, title, formGrid, buttonsBox, resultLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #ffe6e6;");

        resetButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String newPassword = newPassField.isVisible() ? newPassField.getText().trim() : visibleNewPassField.getText().trim();
            String repeatPassword = repeatPassField.isVisible() ? repeatPassField.getText().trim() : visibleRepeatPassField.getText().trim();

            if (email.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                resultLabel.setText("Lütfen tüm alanları doldurun.");
            } else if (!newPassword.equals(repeatPassword)) {
                resultLabel.setText("Şifreler eşleşmiyor.");
            } else if (resetPassword(email, newPassword)) {
                resultLabel.setText("✅ Şifre başarıyla güncellendi.");
            } else {
                resultLabel.setText("❌ E-posta bulunamadı.");
            }
        });

        backButton.setOnAction(e -> {
            // LoginForm
            LoginForm loginForm = new LoginForm();
            loginForm.start(stage);
        });

        // Ekran boyutları
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.8); 
        // Pencere boyutu ekranı
        stage.setScene(scene);
        stage.setTitle("Şifre Sıfırlama");

        // Pencereyi ekranın ortasına konumlandır
        stage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);

        stage.show();
    }

    private void styleTextField(TextField field) {
        field.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 6;");
    }

    private boolean resetPassword(String email, String newPassword) {
        String sql = "UPDATE players SET password = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
