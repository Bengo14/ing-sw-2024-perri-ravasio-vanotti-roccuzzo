package it.polimi.sw.gianpaolocugola47.view.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;

public class ProvaVisiva extends Application {
    private Stage stage;
    private final HashMap<String, String> scenes;
    private Scene scene;

    public ProvaVisiva() {
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("Login", "/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
    }

    public void run() {
        setScene("Login");
    }

    public void setScene(String sceneName) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(scenes.get(sceneName)));
                Parent root = fxmlLoader.load();
                if (scene == null) {
                    scene = new Scene(root);
                    stage.setScene(scene);
                } else {
                    scene.setRoot(root);
                }
                // Add CSS
                //scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/style.css").toExternalForm());
                stage.setResizable(true);
                stage.setMaximized(true);
                stage.setTitle("Codex Naturalis");
                stage.setOnCloseRequest(event -> {
                    event.consume();
                    logOut(stage);
                });
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading scene " + sceneName);
            }
        });
    }



    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        setScene("PreGame");
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> setScene("Game"));
        delay.play();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void logOut(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Logout from Codex Naturalis");
        alert.setContentText("Are you sure you want to logout?");

        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));

        // Aggiungi lo stile del tooltip dal tuo file CSS
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("tooltip");

        // Applica uno stile specifico per il testo "Are you sure you want to logout?"
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black;");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Logged out successfully");
            primaryStage.close();
        }
    }
}
