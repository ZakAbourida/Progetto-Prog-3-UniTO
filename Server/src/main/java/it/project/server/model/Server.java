package it.project.server.model;


import it.project.server.controller.ServerController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    @FXML
    private ListView listserver_view;
    private final int port = 4040; // Porta di default
    private ServerSocket serverSocket;
    private final ExecutorService pool;
    private boolean running = false;
    private ServerController serverController = null;
    private final HashMap<String, Mailbox> loadedBoxes = new HashMap<>();

    public void setController(ServerController c){
        this.serverController = c;
    }

    public Server() {
        this.pool = Executors.newCachedThreadPool();

        try {
            startServer();
        }catch (IOException e){
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

    private void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    // Gestisci la connessione del client in un thread separato
                    pool.execute(new ClientHandler(clientSocket, serverController,this));
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

    public void stopServer() throws IOException {
        running = false;
        pool.shutdown(); // Interrompe tutti i thread attivi
        if (serverSocket != null) {
            serverSocket.close();
        }
        Platform.exit();
        System.exit(0);
    }

    public boolean isRunning() {
        return running;
    }

    //Thread accessed functions
    protected synchronized Mailbox getBox(String address){
        return loadedBoxes.computeIfAbsent(address,
            (key) -> {
                try {
                    Mailbox ret = new Mailbox(key,serverController);
                    if(ret.createOrExists()){
                        //System.out.println("mailcreated");
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
            }
        );
    }

    /*private synchronized void logger(String s) {
        try {
            File logs = new File(Objects.requireNonNull(Mailbox.class.getResource("logs.txt")).toURI());
            FileWriter wr = new FileWriter(logs);
        }catch (URISyntaxException e){
            //Database fault fs has been tampered with
            pool.shutdown();
            System.exit(0);
        }catch (IOException e){
            logError();
        }

    }*/
}
