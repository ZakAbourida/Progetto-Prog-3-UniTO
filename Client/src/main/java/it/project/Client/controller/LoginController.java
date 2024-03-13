package it.project.Client.controller;

import it.project.Client.ApplicationClient;
import it.project.Client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private Button btn_login;
    @FXML
    private TextField email_field;
    @FXML
    private Label lbl_error;

    public void initialize() {
        btn_login.setOnAction(event -> login());
    }

    private void login() {
        String email = email_field.getText().trim();
        if (email.isEmpty()) {
            lbl_error.setText("Inserisci un'email.");
            return; // Evita di proseguire se il campo è vuoto
        }

        // Utilizza il metodo isValidEmail per verificare la correttezza dell'email
        if (!Client.isValidEmail(email)) {
            lbl_error.setText("Formato email non valido.");
            return; // Evita di proseguire se l'email non è valida
        }

        // Pulisci l'etichetta dell'errore prima di procedere
        lbl_error.setText("");
        // Avvia un nuovo thread per connettersi al server
        new Thread(() -> connectClient(email)).start();
    }

    private void connectClient(String email) {
        try {
            Client client = new Client("localhost", 4040, email);
            client.sendMessage("Hello from " + email);
            client.close();

            // Dopo la connessione riuscita, cambia la vista.
            Platform.runLater(() -> {
                try {
                    switchToEmailListView();
                } catch (IOException e) {
                    lbl_error.setText("Impossibile caricare listemail-view.fxml");
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            Platform.runLater(() -> lbl_error.setText("Errore di connessione al server: offline"));
            e.printStackTrace();
        }
    }

    private void switchToEmailListView() throws IOException {
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("listemail-view.fxml"));

        // Ottiene lo stage corrente (dalla finestra attuale) e imposta la nuova scena
        Stage stage = (Stage) btn_login.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle(email_field.getText().trim());
        stage.show();
    }
}