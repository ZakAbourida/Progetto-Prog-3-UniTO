package it.project.Client.controller;

import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EmailController {

    @FXML
    private Button btn_send;

    @FXML
    private TextField field_email;

    @FXML
    private TextField field_subject;

    @FXML
    private TextField field_text;


    public void initialize(){
        btn_send.setOnAction(actionEvent -> {sendEmail();});
    }

    public void sendEmail() {
        if (field_email.getText().isEmpty() || field_subject.getText().isEmpty() || field_text.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile inviare l'email");
            alert.setContentText("Tutti i campi devono essere compilati prima di inviare l'email.");
            alert.showAndWait();
            return;
        }

        // Crea un nuovo oggetto Email
        Email email = new Email();
        email.setRecipients(Collections.singletonList(field_email.getText())); // Imposta il destinatario
        email.setSubject(field_subject.getText()); // Imposta l'oggetto
        email.setText(field_text.getText());       // Imposta il testo dell'email

        // Qui va il codice per l'invio effettivo dell'email
        /**
         * @todo
         */

        //Dobbiamo fare in modo che quando inviamo l'email al server, se la riceve correttamente il server deve restituire true al contrario se non la riceve correttamente riceve false ,
        //in modo tale da attivare il pop-up nella maniera corretta.
        boolean isSent = true; // Simula l'invio dell'email

        if (isSent) {
            // Mostra un Alert di successo se l'email è stata inviata correttamente
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Email Inviata");
            alert.setHeaderText(null);
            alert.setContentText("L'email è stata inviata con successo!");
            alert.showAndWait();
        } else {
            // Mostra un Alert di errore se l'invio dell'email non è riuscito
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore Invio Email");
            alert.setHeaderText("Impossibile inviare l'email");
            alert.setContentText("Si è verificato un errore durante l'invio dell'email. Riprova.");
            alert.showAndWait();
        }
    }
}
