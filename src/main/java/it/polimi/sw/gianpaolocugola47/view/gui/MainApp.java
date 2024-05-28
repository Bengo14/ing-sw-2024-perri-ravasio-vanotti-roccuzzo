package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {


    private static Client client;

    @Override
    public void start(Stage primaryStage) {
        try {
            ViewGui viewGui = new ViewGui();
            viewGui.setClient((RMIClient) client);
            viewGui.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void startGame(Client client) {
        MainApp.client = client;
        // Avvia l'applicazione JavaFX
        Platform.runLater(() -> {
            new Thread(() -> launch(MainApp.class)).start();
        });
    }


}
