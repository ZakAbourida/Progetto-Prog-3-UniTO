package it.project.server.controller;

import it.project.server.model.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ServerController {
    private Server model;

    @FXML
    private ListView listserver_view;

    @FXML
    private Button btn_start;


    public void setServer(Server s) {
        this.model = s;
    }
    public Stage getStage(){return (Stage) this.listserver_view.getScene().getWindow();}

    public void logMessages(String message) {
        Platform.runLater(() -> listserver_view.getItems().add(message));
    }


}
