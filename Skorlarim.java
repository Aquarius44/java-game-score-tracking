package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Skorlarim {

    private String username;
    private ComboBox<String> languageComboBox;

    private Label titleLabel;
    private Button backButton;
    private Button resetButton;
    private ListView<String> listView;

    public Skorlarim(String username) {
        this.username = username;
    }

    public void start(Stage stage) {
        SkorlarimData.skorlariVeritabanindanYukle();

        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        listView = new ListView<>();
        listView.setStyle(
            "-fx-control-inner-background: #3a0f66;" +  
            "-fx-text-fill: white;"                      
        );
        updateListView();

        backButton = new Button();
        backButton.setPrefWidth(150);
        backButton.setStyle(
            "-fx-background-color: #6a0dad; " +  
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> {
            AnaSayfa anaSayfa = new AnaSayfa(username);
            try {
                anaSayfa.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        resetButton = new Button();
        resetButton.setPrefWidth(150);
        resetButton.setStyle(
            "-fx-background-color: #d81b60; " +  
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        resetButton.setOnAction(e -> {
            sifirlaSkorlar();
            SkorlarimData.skorlariVeritabanindanYukle();
            updateListView();
        });

        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue("Türkçe");
        languageComboBox.setStyle("-fx-background-color: #5a2a6a; -fx-text-fill: white;");
        languageComboBox.setOnAction(e -> updateTexts());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c003e;"); 

        root.getChildren().addAll(titleLabel, listView, backButton, resetButton, languageComboBox);
        VBox.setMargin(languageComboBox, new Insets(20, 0, 0, 0));
        root.setPrefWidth(480);
        root.setPrefHeight(480);

        updateTexts();

        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double width = screenBounds.getWidth() * 0.75;  
        double height = screenBounds.getHeight() * 0.75; 
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle("Skorlarım");

       
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        stage.show();
    }

    private void updateTexts() {
        boolean isEnglish = languageComboBox.getValue().equals("English");

        titleLabel.setText((isEnglish ? "📊 " + username + " - My Scores" : "📊 " + username + " - Skorlarım"));
        backButton.setText(isEnglish ? "🔙 Back" : "🔙 Geri Dön");
        resetButton.setText(isEnglish ? "🗑️ Reset Scores" : "🗑️ Skorları Sıfırla");
    }

    private void updateListView() {
        listView.getItems().clear();
        for (Skor skor : SkorlarimData.getSkorListesi()) {
            if (skor.getUsername().equals(username)) {
                listView.getItems().add(skor.toString());
            }
        }
    }

    private void sifirlaSkorlar() {
        String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
        String DB_USER = "root";
        String DB_PASSWORD = "";

        String sql = "DELETE FROM scores WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
