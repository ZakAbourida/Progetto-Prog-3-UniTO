package it.project.Client;

import it.project.Client.controller.LoginController;
import it.project.Client.model.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Questa classe rappresenta l'applicazione client.
 * Carica la vista di accesso (login-view.fxml) e imposta il modello del client per il controller.
 */
public class ApplicationClient extends Application {

    /**
     * Avvia l'applicazione client, caricando la vista di accesso e impostando il modello del client per il controller.
     *
     * @param stage Lo stage principale dell'applicazione.
     * @throws IOException Se si verifica un errore durante il caricamento della vista FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        Client client = new Client();
        ((LoginController) fxmlLoader.getController()).setModel(client, fxmlLoader.getController());
    }

    /**
     * Metodo di avvio dell'applicazione client.
     *
     * @param args Argomenti passati all'avvio dell'applicazione (non utilizzati in questo caso).
     */
    public static void main(String[] args) {
        launch();
    }
}
