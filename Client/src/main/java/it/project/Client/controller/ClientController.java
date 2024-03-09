package it.project.Client.controller;

import it.project.Client.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private Button btn_login;
    @FXML
    private TextField email_field;
    @FXML
    private Label lbl_error;

    // Inizializza il  controller
    public void initialize() {
        btn_login.setOnAction(event -> login());
    }

    // Metodo per gestire il login
    private void login() {
        String email = email_field.getText().trim();
        if (email.isEmpty()) {
            lbl_error.setText("");
            lbl_error.setText("Inserisci un'email.");
            return;
        }

        // Implementa la logica di connessione al server
        try {
            lbl_error.setText("");
            Client client = new Client("localhost", 12345);
            client.sendMessage("Hello from " + email);
            client.close();


        } catch (Exception e) {
            lbl_error.setText("");
            lbl_error.setText("Errore di connessione: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
