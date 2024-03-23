package it.project.Client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller per la visualizzazione dei dettagli di una singola email.
 * Gestisce l'apertura di un'email, la risposta, l'inoltro e l'annullamento dell'email.
 */
public class OpenedEmailController {
    @FXML
    private Label sender_text;
    @FXML
    private Label receiver_text;
    @FXML
    private Label subject_text;
    @FXML
    private TextArea text_area_field;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_forward;
    @FXML
    private Button btn_reply;
    @FXML
    private Label dateTime_text;
    private ListEmailController lstEmailController;

    /**
     * Imposta il controller per la lista delle email.
     *
     * @param lst_controller il controller per la lista delle email
     */
    public void setListEmailController(ListEmailController lst_controller) {
        this.lstEmailController = lst_controller;
    }

    /**
     * Inizializza il controller.
     * Associa gli eventi ai pulsanti e imposta il campo di testo come non modificabile.
     */
    public void initialize() {
        btn_cancel.setOnAction(actionEvent -> {
            deleteEmail();
        });
        btn_forward.setOnAction(actionEvent -> {
            try {
                forwardEmail();
            } catch (IOException e) {
                System.out.println("OpenedEmailController - forwardEmail()");
                throw new RuntimeException(e);
            }
        });
        btn_reply.setOnAction(actionEvent -> {
            try {
                replyEmail();
            } catch (IOException e) {
                System.out.println("OpenedEmailController - replyEmail()");
                throw new RuntimeException(e);
            }
        });
        text_area_field.setEditable(false); // così il contenuto sarà READ_ONLY
    }

    /**
     * Metodo per la cancellazione dell'email (chiamata a ListEmailController che lo fa) e chiude la finestra corrente.
     */
    public void deleteEmail() {
        lstEmailController.DeleteEmail(sender_text.getText(), subject_text.getText(), dateTime_text.getText());
        // chiude la finestra
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Inoltra l'email e aggiunge una marcatura "Forwarded" al testo dell'email.
     *
     * @throws IOException se si verifica un errore durante l'inoltro dell'email
     */
    public void forwardEmail() throws IOException {
        String text = text_area_field.getText();
        //controlla se l'email è già stata inoltrata o no
        if (!text.startsWith("Forwarded - ")) {
            text_area_field.setText("Forwarded - " + text);
        }
        lstEmailController.ForwardEmail(sender_text.getText(), subject_text.getText(), text_area_field.getText());
    }

    /**
     * Metodo per rispondere all'email e aggiunge una marcatura "Reply" all'oggetto dell'email.
     *
     * @throws IOException se si verifica un errore durante la risposta all'email
     */
    public void replyEmail() throws IOException {
        String subject = subject_text.getText();
        //per evitare di continuare ad aggiungere Reply nelle risposte
        if (!subject.startsWith("Reply - ")) {
            subject_text.setText("Reply - " + subject);
        }
        lstEmailController.ReplyEmail(sender_text.getText(), subject_text.getText());
    }

    /**
     * Imposta i dettagli dell'email nei rispettivi campi di visualizzazione.
     *
     * @param sender    il mittente dell'email
     * @param recipient la lista dei destinatari dell'email
     * @param subject   l'oggetto dell'email
     * @param text      il testo dell'email
     * @param date      la data di invio dell'email
     */
    public void setDetails(String sender, List<String> recipient, String subject, String text, String date) {
        sender_text.setText(sender);
        receiver_text.setText(String.join("| ", recipient)); /* conversione lista stringhe in una stringa divisa da '|'*/
        subject_text.setText(subject);
        text_area_field.setText(text);
        dateTime_text.setText(date);
    }
}