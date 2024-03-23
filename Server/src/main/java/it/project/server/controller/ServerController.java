package it.project.server.controller;

import it.project.server.model.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Questa classe funge da controller per la GUI del server.
 * Gestisce l'interazione tra la vista e il modello del server.
 */
public class ServerController {

    private Server model; // Il modello del server

    @FXML
    private ListView listserver_view; // Lista per visualizzare i messaggi del server

    @FXML
    private Button btn_start; // Bottone per avviare il server

    /**
     * Metodo setter per il Server
     *
     * @param s Server da impostare.
     */
    public void setServer(Server s) {
        this.model = s;
    }

    /**
     * Restituisce lo stage associato alla finestra della lista dei messaggi del server.
     *
     * @return Lo stage associato alla finestra della lista dei messaggi del server.
     */
    public Stage getStage() {
        return (Stage) this.listserver_view.getScene().getWindow();
    }

    /**
     * Aggiunge un messaggio alla lista dei messaggi del server.
     * Questo metodo viene eseguito sul thread di JavaFX per garantire la sicurezza dei thread.
     *
     * @param message Il messaggio da aggiungere alla lista dei messaggi del server.
     */
    public void logMessages(String message) {
        Platform.runLater(() -> listserver_view.getItems().add(message));
    }
}