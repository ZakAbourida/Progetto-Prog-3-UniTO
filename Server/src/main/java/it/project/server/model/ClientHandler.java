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
            while (true) {
                // Esempio: leggi una linea di testo e inviala indietro al client
                // Receive the request from the client
                Object request = in.readObject();
                // Manage the request
                Object response = handleRequest(request);
                // Send the response to the client
                out.writeObject(response);
            }

            // Pulizia: chiudi gli stream e il socket
                /*in.close();
                out.close();
                clientSocket.close();
                serverController.logConnection("Client disconnesso: ");*/
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore nella gestione del client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object handleRequest(Object req) throws IOException, ClassNotFoundException {
        // Verifica se req è un'istanza di RequestType
        if (!(req instanceof RequestType)) {
            throw new IllegalArgumentException("Richiesta non valida");
        }

        RequestType richiesta = (RequestType) req;

        switch (richiesta.getType()) {
            case 1: // Login request
                return handleLoginRequest(richiesta);
            case 2: // Send email request
                return handleSendEmailRequest(richiesta);
            case 3: // Receive email request
                return handleReceiveEmailRequest(richiesta);
            case 4: // Delete email request
                return handleDeleteEmailRequest(richiesta); // Restituisce direttamente un booleano
            default:
                throw new IllegalArgumentException("Tipo di richiesta non supportato");
        }
    }


    public RequestType handleLoginRequest(RequestType request) throws IOException {
        serverController.logMessages("Client connesso: " + request.getEmail());
        Mailbox m = server.getBox(request.getEmail());
        out.writeObject(m.getMessages());
        return request;
    }

    public RequestType handleSendEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)) {
            throw new IllegalArgumentException("An email is expected after a send request");
        }
        for (String recipient : ((Email) mail).getRecipients()) {
            Mailbox box = server.getBox(recipient);
            box.addMessage((Email) mail);
            box.writeMailbox(); //TODO better caching and transactions
        }
        serverController.logMessages("Email inviata da: " + request.getEmail());
        return request;
    }

    public RequestType handleReceiveEmailRequest(RequestType request) throws IOException {
        Mailbox m = server.getBox(request.getEmail());
        out.writeObject(m.getMessages());
        return request;
    }

    public String handleDeleteEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)) {
            throw new IllegalArgumentException("An email is expected after a delete request");
        }
        Mailbox m = server.getBox(request.getEmail());
        m.removeMessage((Email) mail);
        return "Email cancellata con successo.";
    }

}