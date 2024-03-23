package it.project.Client.controller;

import it.project.Client.model.Client;
import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.Collections;

/**
 * Controller per la finestra di composizione e invio di nuove email.
 */
public class EmailController {

    @FXML
    private Button btn_send;

    @FXML
    private TextField field_email;

    @FXML
    private TextField field_subject;

    @FXML
    private TextField field_text;

    private Client model;

    /**
     * Imposta il client per questo controller.
     *
     * @param model il modello del client
     */
    public void setModel(Client model) {
        this.model = model;
    }

    /**
     * Imposta il campo di testo per l'indirizzo email con il valore specificato.
     *
     * @param email l'indirizzo email da impostare
     */
    public void setEmailField(String email) {
        this.field_email.setText(email);
    }

    /**
     * Imposta il campo di testo per l'oggetto dell'email con il valore specificato.
     *
     * @param subject l'oggetto dell'email da impostare
     */
    public void setSubjectField(String subject) {
        this.field_subject.setText(subject);
    }

    /**
     * Imposta il campo di testo per il testo dell'email con il valore specificato.
     *
     * @param text il testo dell'email da impostare
     */
    public void setTextField(String text) {
        this.field_text.setText(text);
    }

    /**
     * Metodo per l'inizializzazione del controller e del bottone.
     */
    public void initialize() {
        btn_send.setOnAction(actionEvent -> {
            sendEmail();
        });
    }

    /**
     * Metodo per l'invio dell'email:
     * -    Invia l'email utilizzando i dati inseriti nei campi di testo.
     * -    Mostra un messaggio di errore se uno dei campi è vuoto.
     * -    Visualizza una finestra di dialogo informativa se l'email viene inviata correttamente
     * o una finestra di dialogo di errore se l'invio dell'email fallisce.
     */
    public void sendEmail() {
        if (field_email.getText().isEmpty() || field_subject.getText().isEmpty() || field_text.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to send the email");
            alert.setContentText("All fields must be filled out before sending the email.");
            alert.showAndWait();
            return;
        }

        // Crea un nuovo oggetto Email
        Email email = new Email();
        email.setRecipients(Collections.singletonList(field_email.getText())); // Imposta il destinatario
        email.setSubject(field_subject.getText()); // Imposta l'oggetto
        email.setText(field_text.getText());       // Imposta il testo dell'email

        //Dobbiamo fare in modo che quando inviamo l'email al server, se la riceve correttamente il server deve restituire true al contrario se non la riceve correttamente riceve false ,
        //in modo tale da attivare il pop-up nella maniera corretta.

        boolean isSent = model.sendEmail(email); // Simula l'invio dell'email

        if (isSent) {
            // Mostra un Alert di successo se l'email è stata inviata correttamente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email sent!");
            alert.setHeaderText(null);
            alert.setContentText("Email succefully sent!");
            alert.showAndWait();

            // Chiude lo stage della email dopo che l'alert è stato chiuso
            ((Stage) field_email.getScene().getWindow()).close();
        } else {
            // Mostra un Alert di errore se l'invio dell'email non è riuscito
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Sending Error");
            alert.setHeaderText("Unable to sent the email");
            alert.setContentText("An error occurred while sending the email. Please try again.");
            alert.showAndWait();
        }
    }
}
