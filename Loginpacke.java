package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class Loginpacke extends Application {
 @Override
 public void start(Stage primaryStage) {
 Button btn = new Button("Click Me");
 btn.setOnAction(e -> System.out.println("Button clicked"));
 StackPane root = new StackPane();
 root.getChildren().add(btn);
 Scene scene = new Scene(root, 300, 250);
 primaryStage.setTitle("JavaFX Application");
 primaryStage.setScene(scene);
 primaryStage.show();
 }
 public static void main(String[] args) {
 launch(args);
 }
}