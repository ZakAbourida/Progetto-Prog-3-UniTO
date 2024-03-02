package it.project.Client.controller;

import it.project.Client.MailClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button btn_login;
    @FXML
    private TextField email_field;
    @FXML
    private Circle light_server;
    @FXML
    private Button btn_server;

    private Stage serverStage;

    @FXML
    public void initialize() {
        btn_server.setOnAction(event -> {
            try {
                launchServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void launchServer() throws IOException {
        if ("Connetti".equals(btn_server.getText())) {
            openWindowServer();
            updateServerStatus(Color.GREEN, "Disconnetti");
        } else {
            closeWindowServer();
            updateServerStatus(Color.RED, "Connetti");
        }
    }

    private void openWindowServer() throws IOException {
        serverStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MailClient.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        serverStage.setTitle("Server");
        serverStage.setScene(scene);
        serverStage.show();
    }

    private void closeWindowServer() {
        if (serverStage != null) {
            serverStage.close();
        }
    }

    private void updateServerStatus(Color color, String buttonText) {
        light_server.setFill(color);
        btn_server.setText(buttonText);
    }

    private void startConnection() {
        // Codice da eseguire quando il pulsante del server viene premuto
        // Ad esempio, puoi inserire qui la logica per connettersi al server
    }

    private void closeConnection() {

    }

}
