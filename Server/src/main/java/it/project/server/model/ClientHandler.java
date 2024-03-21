package it.project.server.model;

import it.project.lib.Email;
import it.project.lib.RequestType;
import it.project.server.controller.ServerController;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerController serverController;

    public ClientHandler(Socket socket, ServerController controller, Server serverInstance) throws IOException {
        this.clientSocket = socket;
        this.serverController = controller;
        this.server = serverInstance;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {
                // Receive the request from the client
                Object request = in.readObject();
                // Manage the request
                handleRequest(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore nella gestione del client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleRequest(Object req) throws IOException, ClassNotFoundException {
        // Verifica se req è un'istanza di RequestType
        if (!(req instanceof RequestType richiesta)) {
            throw new IllegalArgumentException("Richiesta non valida");
        }

        switch (richiesta.getType()) {
            case 1: // Login request
                handleLoginRequest(richiesta);
                break;
            case 2: // Send email request
                handleSendEmailRequest(richiesta);
                break;
            case 3: // Receive email request
                handleReceiveEmailRequest(richiesta);
                break;
            case 4: // Delete email request
                handleDeleteEmailRequest(richiesta);
                break;
            default:
                throw new IllegalArgumentException("Tipo di richiesta non supportato");
        }
    }

    public void handleLoginRequest(RequestType request) throws IOException {
        serverController.logMessages("Client connesso: "+request.getEmail());
        Mailbox m = server.getBox(request.getEmail());
        out.writeObject(m.getMessages());
    }
    public void handleSendEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)){
            throw new IllegalArgumentException("An email is expected after a send request");
        }
        for (String recipient: ((Email)mail).getRecipients()){
            Mailbox box = server.getBox(recipient);
            box.addMessage((Email) mail);
            box.writeMailbox(); //TODO better caching and transactions
        }
        serverController.logMessages("Email inviata da: "+request.getEmail());
    }
    public void handleReceiveEmailRequest(RequestType request) throws IOException {
        Mailbox m = server.getBox(request.getEmail());
        m.readMailbox();
        out.writeObject(m.getMessages());
    }
    public void handleDeleteEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)){
            throw new IllegalArgumentException("An email is expected after a delete request");
        }
        Mailbox m = server.getBox(request.getEmail());
        m.removeMessage((Email) mail);
        m.writeMailbox();
        out.writeObject("Email successfully removed");
    }



}