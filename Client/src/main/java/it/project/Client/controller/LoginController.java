package it.project.Client.controller;

import it.project.lib.*;
import it.project.Client.ApplicationClient;
import it.project.Client.model.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;

/**
 * Controller per la schermata di accesso.
 * Gestisce il login dell'utente e il passaggio alla schermata di visualizzazione delle email.
 */
public class LoginController {

    @FXML
    private Button btn_login;
    @FXML
    private TextField email_field;
    @FXML
    private Label lbl_error;
    private ListEmailController listEmailController;
    private Client model;
    private LoginController lgn_controller;

    /**
     * Inizializza il controller.
     * Imposta gli eventi per il pulsante di login e il campo di testo dell'email.
     * Imposta inoltre un gestore per l'evento di chiusura della finestra.
     */
    public void initialize() {
        btn_login.setOnAction(event -> login());
        email_field.setOnKeyPressed((keyEvent) -> { //Lambda form EventHandler<KeyEvent>
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                login();
            }
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) btn_login.getScene().getWindow();
            if (stage != null) {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent e) {
                        model.sendDisconnect();
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });
    }

    /**
     * Imposta il client e il riferimento al controller di login.
     *
     * @param model il modello del client
     * @param lg    il riferimento al controller di login
     */
    public void setModel(Client model, LoginController lg) {
        this.model = model;
        this.lgn_controller = lg;
    }

    /**
     * Gestisce l'evento di login dell'utente.
     * Controlla se l'email inserita è valida e se lo è, fa richiesta per eseguire il login.
     */
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

        //richiesta per il login
        connectClient(email);
    }

    /**
     * Effettua la connessione al server per il login dell'utente.
     *
     * @param address l'indirizzo email dell'utente
     */
    private void connectClient(String address) {
        try {
            List<Email> mailbox = model.sendLogin(address);
            listEmailController = new ListEmailController();

            // Dopo la connessione riuscita, cambia la vista.
            Platform.runLater(() -> {
                try {
                    switchToEmailListView();
                    listEmailController.fillReceivedEmail(mailbox);
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

    /**
     * Cambia la vista alla schermata di visualizzazione delle email.
     *
     * @throws IOException se si verifica un errore durante il caricamento della nuova schermata
     */
    private void switchToEmailListView() throws IOException {
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("listemail-view.fxml"));

        // Ottiene lo stage corrente (dalla finestra attuale) e imposta la nuova scena
        fxmlLoader.setController(listEmailController);
        listEmailController.setListEmailController(fxmlLoader.getController());
        Stage stage = (Stage) btn_login.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle(email_field.getText().trim());
        stage.show();
        listEmailController.setModel(model);
    }

}