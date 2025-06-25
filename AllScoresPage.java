package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class AllScoresPage {

    private final String DB_URL = "jdbc:mysql://localhost:3306/gamescoretracking";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private String currentUser;

    private Label title;
    private TableView<ScoreRecord> tableView;
    private Button backButton;
    private ComboBox<String> languageComboBox;
    private VBox root;

    public AllScoresPage(String currentUser) {
        this.currentUser = currentUser;
    }

    public void start(Stage stage) {
        stage.setTitle("Tüm Oyuncuların Skorları");

        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c003e;");
        root.setAlignment(Pos.CENTER);

        title = new Label();
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        root.getChildren().add(title);

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle(
            "-fx-control-inner-background: #3a0f66;" +  
            "-fx-table-cell-border-color: transparent;" +
            "-fx-text-fill: white;"
        );

       
        tableView.setRowFactory(tv -> {
            TableRow<ScoreRecord> row = new TableRow<>();
            row.setStyle("-fx-text-fill: white;");
            return row;
        });

        root.getChildren().add(tableView);

       
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Türkçe", "English");
        languageComboBox.setValue("Türkçe");
        languageComboBox.setStyle(
            "-fx-background-color: #5a2a6a; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        languageComboBox.setOnAction(e -> updateTexts());
        root.getChildren().add(languageComboBox);
        VBox.setMargin(languageComboBox, new Insets(15, 0, 0, 0));

        backButton = new Button();
        backButton.setPrefWidth(150);
        backButton.setStyle(
            "-fx-background-color: #6a0dad; " + 
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> {
            AnaSayfa anaSayfa = new AnaSayfa(currentUser);
            try {
                anaSayfa.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox bottomBox = new HBox(backButton);
        bottomBox.setPadding(new Insets(15, 0, 0, 0));
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setStyle("-fx-background-color: transparent;");
        root.getChildren().add(bottomBox);

        // İlk metin
        updateTexts();
        loadData();

        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.75;
        double height = screenBounds.getHeight() * 0.75;

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

        // Ekran ortası
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        stage.show();
    }

    private void updateTexts() {
        boolean isEnglish = languageComboBox.getValue().equals("English");

        title.setText(isEnglish ? "🎯 All Players' Scores" : "🎯 Tüm Oyuncuların Skorları");

        // Tablo sütunlarını temizle
        tableView.getColumns().clear();

        TableColumn<ScoreRecord, String> usernameCol = new TableColumn<>(isEnglish ? "Username" : "Kullanıcı");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<ScoreRecord, String> gameCol = new TableColumn<>(isEnglish ? "Game" : "Oyun");
        gameCol.setCellValueFactory(new PropertyValueFactory<>("game"));

        TableColumn<ScoreRecord, Integer> scoreCol = new TableColumn<>(isEnglish ? "Score" : "Skor");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<ScoreRecord, String> dateCol = new TableColumn<>(isEnglish ? "Date" : "Tarih");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

       
        usernameCol.setStyle("-fx-text-fill: white;");
        gameCol.setStyle("-fx-text-fill: white;");
        scoreCol.setStyle("-fx-text-fill: white;");
        dateCol.setStyle("-fx-text-fill: white;");

        tableView.getColumns().addAll(usernameCol, gameCol, scoreCol, dateCol);

        backButton.setText(isEnglish ? "🔙 Back" : "🔙 Geri Dön");
    }

    private void loadData() {
        tableView.getItems().clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT username, game, score, created_at FROM scores ORDER BY score DESC, created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String game = rs.getString("game");
                int score = rs.getInt("score");
                Timestamp timestamp = rs.getTimestamp("created_at");

                String formattedDate = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                ScoreRecord record = new ScoreRecord(username, game, score, formattedDate);
                tableView.getItems().add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            boolean isEnglish = languageComboBox.getValue().equals("English");
            Label errorLabel = new Label(isEnglish ? "Failed to load scores!" : "Skorlar alınamadı!");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            root.getChildren().add(errorLabel);
        }
    }

    public static class ScoreRecord {
        private final String username;
        private final String game;
        private final int score;
        private final String createdAt;

        public ScoreRecord(String username, String game, int score, String createdAt) {
            this.username = username;
            this.game = game;
            this.score = score;
            this.createdAt = createdAt;
        }

        public String getUsername() {
            return username;
        }

        public String getGame() {
            return game;
        }

        public int getScore() {
            return score;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
