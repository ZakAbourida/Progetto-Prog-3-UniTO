package it.project.Client.controller;

import it.project.Client.model.Client;
import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to send the email");
            alert.setContentText("All fields must be filled out before sending the email.");
            alert.showAndWait();
            return;
        }


        List<String> recipients = Arrays.asList(field_email.getText().split("[;,]\\s*"));


        for (String recipient : recipients) {
            if (!Client.isValidEmail(recipient)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid email address");
                alert.setContentText("One or more email addresses are invalid. Please ensure they are correct and separated by ';' or ','.");
                alert.showAndWait();
                return;
            }
        }

        Email email = new Email();
        email.setRecipients(recipients); // Imposta i destinatari
        email.setSubject(field_subject.getText()); // Imposta l'oggetto
        email.setText(field_text.getText());       // Imposta il testo dell'email


        boolean isSent = model.sendEmail(email);

        if (isSent) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Sent!");
            alert.setHeaderText(null);
            alert.setContentText("Email successfully sent!");
            alert.showAndWait();
            ((Stage) field_email.getScene().getWindow()).close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Sending Error");
            alert.setHeaderText("Unable to send the email");
            alert.setContentText("An error occurred while sending the email. Please try again.");
            alert.showAndWait();
        }
    }
}
