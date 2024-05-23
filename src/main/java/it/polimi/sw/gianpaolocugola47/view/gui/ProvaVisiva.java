package it.polimi.sw.gianpaolocugola47.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ProvaVisiva extends Application {


    //controllo visivo lobby
    /*
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml"));
        root = loader.load();
        stage.setTitle("prova");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
    stage.setOnCloseRequest(event -> {
        event.consume();
        logOut(stage);
    });
        stage.show();
    }
     */

    //controllo visivo endGame
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml"));
        root = loader.load();
        stage.setTitle("prova");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
        stage.setOnCloseRequest(event -> {
        event.consume();
        logOut(stage);
    });
        stage.show();
    }


    //controllo visivo PreGame
    /*@Override
    public void start(Stage stage) throws Exception {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml"));
        root = loader.load();
        stage.setTitle("prova");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
        stage.show();
        stage.setOnCloseRequest(event -> {
            event.consume();
            logOut(stage);
        });
    }
    */

    public static void main(String[] args) {
        launch(args);
    }
    public void logOut(Stage primaryStage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, Cancel to stay logged in");

        if(alert.showAndWait().get() == ButtonType.OK){
            System.out.println("Logged out successfully");
            primaryStage.close();
        }
    }
}
