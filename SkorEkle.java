package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SkorEkle {

    private String username;
    private int playerId;

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private VBox skorGirisKutulari;
    private ComboBox<String> languageComboBox;

    private Label titleLabel;
    private Button backButton;
    private Button resetSelectionsButton;

    public SkorEkle(String username, int playerId) {
        this.username = username;
        this.playerId = playerId;
    }

    public void start(Stage primaryStage) {
        skorGirisKutulari = new VBox(10);
        skorGirisKutulari.setAlignment(Pos.CENTER);

        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue("Türkçe");
        languageComboBox.setStyle("-fx-background-color: #5a2a6a; -fx-text-fill: white;");

        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");  

        backButton = new Button();
        backButton.setPrefWidth(150);
        backButton.setStyle("-fx-background-color: #6a0dad; -fx-text-fill: white; -fx-font-weight: bold;");  

        resetSelectionsButton = new Button();
        resetSelectionsButton.setPrefWidth(150);
        resetSelectionsButton.setStyle("-fx-background-color: #d81b60; -fx-text-fill: white; -fx-font-weight: bold;");  

       
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);  // sayfanın ortasına hizala
        root.setStyle("-fx-background-color: #2c003e;"); 
        root.setPrefSize(480, 480);

        HBox buttonBox = new HBox(20, backButton, resetSelectionsButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, skorGirisKutulari, buttonBox, languageComboBox);
        VBox.setMargin(languageComboBox, new Insets(20, 0, 0, 0));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.75;  
        double height = screenBounds.getHeight() * 0.75;

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Skor Ekleme");

        // Pencereyi ekran ortasına yerleştir
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);

        primaryStage.show();

        // İlk skor giriş satırını ekle
        skorGirisKutulari.getChildren().add(createSkorGirisSatiri());

        languageComboBox.setOnAction(e -> updateTexts());

        backButton.setOnAction(e -> {
            AnaSayfa anaSayfa = new AnaSayfa(username);
            try {
                anaSayfa.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        resetSelectionsButton.setOnAction(e -> {
            skorGirisKutulari.getChildren().clear();
            skorGirisKutulari.getChildren().add(createSkorGirisSatiri());
        });

        updateTexts();
    }

    private void updateTexts() {
        boolean isEnglish = languageComboBox.getValue().equals("English");

        titleLabel.setText(isEnglish ? "🎮 Add Score 🎮" : "🎮 Skor Ekle 🎮");
        backButton.setText(isEnglish ? "🔙 Back" : "🔙 Geri Dön");
        resetSelectionsButton.setText(isEnglish ? "🔄 Reset Selections" : "🔄 Seçimleri Sıfırla");

        
        skorGirisKutulari.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                for (var child : hbox.getChildren()) {
                    if (child instanceof ComboBox<?> oyunCombo) {
                        oyunCombo.setPromptText(isEnglish ? "Select Game" : "Oyun Seç");
                    }
                    if (child instanceof TextField tf) {
                        tf.setPromptText(isEnglish ? "Enter Score" : "Skorunu Gir");
                    }
                    if (child instanceof Button btn) {
                        btn.setText(isEnglish ? "➕ Add Score" : "➕ Skor Ekle");
                    }
                }
            }
        });
    }

    private HBox createSkorGirisSatiri() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);

        ComboBox<String> oyunComboBox = new ComboBox<>();
        oyunComboBox.getItems().addAll(
                "League of Legends",
                "Fortnite",
                "Minecraft",
                "Among Us",
                "Call of Duty",
                "Valorant",
                "FIFA",
                "CS:GO"
        );
        oyunComboBox.setPromptText(languageComboBox.getValue().equals("English") ? "Select Game" : "Oyun Seç");
        oyunComboBox.setStyle("-fx-background-color: #5a2a6a; -fx-text-fill: white;");

        TextField skorField = new TextField();
        skorField.setPromptText(languageComboBox.getValue().equals("English") ? "Enter Score" : "Skorunu Gir");
        skorField.setStyle("-fx-background-color: #5a2a6a; -fx-text-fill: white; -fx-prompt-text-fill: #d3bde0;");

        // Sadece sayı ve virgül nokta girişi için filtre
        skorField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*([\\.,]\\d*)?")) {
                skorField.setText(oldVal);
            }
        });

        Button skorEkleButton = new Button(languageComboBox.getValue().equals("English") ? "➕ Add Score" : "➕ Skor Ekle");
        skorEkleButton.setStyle("-fx-background-color: #6a0dad; -fx-text-fill: white; -fx-font-weight: bold;");
        skorEkleButton.setVisible(false);

        // Buton görünürlüğü 
        skorField.textProperty().addListener((obs, oldVal, newVal) -> {
            skorEkleButton.setVisible(!newVal.trim().isEmpty() && oyunComboBox.getValue() != null);
        });
        oyunComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            skorEkleButton.setVisible(skorField.getText() != null && !skorField.getText().trim().isEmpty() && newVal != null);
        });

        skorEkleButton.setOnAction(e -> {
            if (oyunComboBox.getValue() == null || skorField.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        languageComboBox.getValue().equals("English") ?
                                "Please select a game and enter score first." : "Lütfen önce oyun seçin ve skor girin.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            double skor;
            try {
                skor = Double.parseDouble(skorField.getText().trim().replace(',', '.'));
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        languageComboBox.getValue().equals("English") ?
                                "Please enter a valid number (with or without comma)." : "Lütfen geçerli bir sayı girin (virgüllü veya virgülsüz).", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (skor < 1 || skor > 100) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        languageComboBox.getValue().equals("English") ?
                                "Please enter a number between 1 and 100." : "Lütfen 1 ile 100 arasında bir sayı girin.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO scores (username, player_id, game, score) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setInt(2, playerId);
                ps.setString(3, oyunComboBox.getValue());
                ps.setDouble(4, skor);
                ps.executeUpdate();

                SkorlarimData.skorEkle(new Skor(username, oyunComboBox.getValue(), skor));

                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        languageComboBox.getValue().equals("English") ?
                                "Score added successfully!" : "Skor başarıyla eklendi!", ButtonType.OK);
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        languageComboBox.getValue().equals("English") ?
                                "Error occurred while adding score!" : "Skor eklenirken hata oluştu!", ButtonType.OK);
                alert.showAndWait();
            }

            oyunComboBox.setDisable(true);
            skorField.setDisable(true);
            skorEkleButton.setDisable(true);

            // Yeni giriş satırı 
            HBox yeniSatir = createSkorGirisSatiri();
            skorGirisKutulari.getChildren().add(yeniSatir);
        });

        hbox.getChildren().addAll(oyunComboBox, skorField, skorEkleButton);
        return hbox;
    }
}
