package it.project.server.model;

import it.project.server.controller.ServerController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port = 4040; // Porta di default
    private ServerSocket serverSocket;
    private final ExecutorService pool;
    private boolean running = false;
    private ServerController serverController = null;
    private final HashMap<String, Mailbox> loadedBoxes = new HashMap<>();

    /**
     * Imposta il controller del server:
     *
     * @param c Il controller del server.
     */
    public void setController(ServerController c) {
        this.serverController = c;
    }

    /**
     * Costruttore della classe Server.
     */
    public Server() {
        this.pool = Executors.newCachedThreadPool();

        try {
            startServer();
        } catch (IOException e) {
            //logError();
        }
        this.running = true;

        // Gestisce la chiusura della finestra tramite il pulsante "X"
        Platform.runLater(() -> {
            Stage stage = serverController.getStage();
            if (stage != null) {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent e) {
                        try {
                            stopServer();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }

    /**
     * Avvia il server in un nuovo thread.
     *
     * @throws IOException se si verifica un errore di I/O durante l'avvio del server.
     */
    private void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    // Gestisci la connessione del client in un thread separato
                    pool.execute(new ClientHandler(clientSocket, serverController, this));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    this.serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Arresta il server.
     *
     * @throws IOException Se si verifica un errore di I/O durante la chiusura del serverSocket.
     */
    public void stopServer() throws IOException {
        running = false;
        pool.shutdown(); // Interrompe tutti i thread attivi
        if (serverSocket != null) {
            serverSocket.close();
        }
        Platform.exit();
        System.exit(0);
    }



    /**
     * Restituisce la mailbox associata all'indirizzo specificato.
     *
     * @param address L'indirizzo della mailbox.
     * @return La mailbox associata all'indirizzo specificato.
     */
    protected synchronized Mailbox getBox(String address) {
        return loadedBoxes.computeIfAbsent(address, (key) -> {
            try {
                Mailbox ret = new Mailbox(key, serverController);
                if (ret.createOrExists()) {
                    serverController.logMessages("New Mailbox created:\t" + address);
                }
                ret.readMailbox();
                return ret;
            } catch (URISyntaxException e) { //TODO crash
                //Database fault fs has been tampered with
                pool.shutdown();
                System.exit(0);
            } catch (IOException e) { //TODO notify logger
                //IO error notify observers
                throw new RuntimeException(e);
            }
            return null;
        });
    }
}
