package it.project.server.controller;

import it.project.server.model.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ServerController {
    private Server model;

    @FXML
    private ListView listserver_view;

    @FXML
    private Button btn_start;


    public void setServer(Server s){
        this.model = s;
    }

    /*
    @FXML
    protected void StartAndStop() {
        if (server.isRunning()) {
            try {
                server.stopServer();
                listserver_view.getItems().add("Server fermato");
                btn_start.setText("Start");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new Thread(() -> {
                try {
                    server.startServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            listserver_view.getItems().add("Server avviato sulla porta 12345");
            btn_start.setText("Stop");
        }
    }*/

    public void logMessages(String message) {
        Platform.runLater(() -> listserver_view.getItems().add(message));
    }


}
