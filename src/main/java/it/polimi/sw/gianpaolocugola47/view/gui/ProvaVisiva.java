package it.polimi.sw.gianpaolocugola47.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProvaVisiva extends Application {
    //controllo visivo lobby
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
        stage.show();
    }



    //controllo visivo endGame
//    @Override
//    public void start(Stage Stage) throws Exception {
//        Parent root;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml"));
//        root = loader.load();
//        Stage.setTitle("prova");
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
//        Stage.setScene(scene);
//        Stage.setMinWidth(1280);
//        Stage.setMinHeight(760);
//        Stage.show();
//
//    }
    public static void main(String[] args) {
        launch(args);
    }
}
