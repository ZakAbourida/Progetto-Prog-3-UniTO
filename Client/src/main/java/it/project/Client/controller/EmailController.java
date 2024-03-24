package it.project.Client.controller;

import it.project.Client.model.Client;
import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;


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
            showAlert("Error", "Unable to send the email", "All fields must be filled out before sending the email.", Alert.AlertType.ERROR);
            return;
        }

        List<String> recipients = Arrays.asList(field_email.getText().split("[;,]\\s*"));

        for (String recipient : recipients) {
            if (!Client.isValidEmail(recipient)) {
                showAlert("Error", "Invalid email address", "One or more email addresses are invalid. Please ensure they are correct and separated by ';' or ','.", Alert.AlertType.ERROR);
                return;
            }
        }

        Email email = new Email();
        email.setRecipients(recipients);
        email.setSubject(field_subject.getText());
        email.setText(field_text.getText());

        boolean isSent = model.sendEmail(email);

        if (isSent) {
            showAlert("Email Sent!", null, "Email successfully sent!", Alert.AlertType.INFORMATION);
            ((Stage) field_email.getScene().getWindow()).close();
        } else {
            showAlert("Email Sending Error", "Unable to send the email", "An error occurred while sending the email. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
