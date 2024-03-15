package it.project.Client.controller;

import it.project.Client.ApplicationClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ListEmailController {
    @FXML
    private Button btn_newemail;
    @FXML
    private Button btn_forward;
    @FXML
    private Button btn_reply;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_refresh;
    @FXML
    private ListView listview_email;


    @FXML
    public void initialize() {
        // Qui puoi inizializzare la lista con alcuni dati o configurare comportamenti aggiuntivi
        // Es: listview_email.getItems().addAll("Email 1", "Email 2", "Email 3");
        btn_newemail.setOnAction(actionEvent -> {
            try {
                writeEmail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Configura il listener per la selezione nella ListView
        /*
        listview_email.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            email_field.setText(newValue); // Mostra l'email selezionata in email_field
        });
        */
    }



    public void writeEmail() throws IOException {
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Crea un nuovo stage (finestra) per questa scena
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("New Email");

        // Mostra il nuovo stage
        newStage.show();
    }

}
