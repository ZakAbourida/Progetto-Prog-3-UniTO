package it.project.server.model;

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

    public ClientHandler(Socket socket, ServerController controller) throws IOException {
        this.clientSocket = socket;
        this.serverController = controller;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }
    @Override
    public void run() {
            try {
                // Esempio: leggi una linea di testo e inviala indietro al client
                // Receive the request from the client
                Object request = in.readObject();
                // Manage the request
                Object response = handleRequest(request);
                // Send the response to the client
                out.writeObject(response);
                serverController.logConnection("Client connesso: ");

                // Pulizia: chiudi gli stream e il socket
                in.close();
                out.close();
                clientSocket.close();
                serverController.logConnection("Client disconnesso: ");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Errore nella gestione del client: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * System.out.println("Connesso: ");
     *         while(true){
     *             try {
     *                 // Esempio: leggi una linea di testo e inviala indietro al client
     *                 String line = in.readLine();
     *                 out.println("Hai detto: " + line);
     *                 //serverController.logConnection("Client connesso: "+line);
     *
     *             } catch (IOException e) {
     *                 System.out.println("Errore nella gestione del client: " + e.getMessage());
     *                 e.printStackTrace();
     *             }
     *
     *             try {
     *                 // Pulizia: chiudi gli stream e il socket
     *                 in.close();
     *                 out.close();
     *                 clientSocket.close();
     *                 //serverController.logConnection("Client disconnesso: ");
     *             } catch (IOException e) {
     *                 System.out.println("Errore nella gestione del client: " + e.getMessage());
     *                 e.printStackTrace();
     *             }
     *         }
     */

    public Object handleRequest(Object req) throws IOException {
        // Verifica se req è un'istanza di RequestType
        if (!(req instanceof RequestType)) {
            throw new IllegalArgumentException("Richiesta non valida");
        }

        RequestType richiesta = (RequestType) req;

        switch(richiesta.getType()) {
            case 0: // Connection request
                return handleLoginRequest(richiesta);
            case 1: // Login request
                return handleLoginRequest(richiesta);
            case 2: // Send email request
                return handleSendEmailRequest(richiesta);
            case 3: // Receive email request
                return handleReceiveEmailRequest(richiesta);
            case 4: // Delete email request
                return handleDeleteEmailRequest(richiesta);
            default:
                throw new IllegalArgumentException("Tipo di richiesta non supportato");
        }
    }

    public RequestType handleLoginRequest(RequestType request){
        serverController.logConnection("Client connesso: "+request.getEmail());
        return request;
    }
    public RequestType handleSendEmailRequest(RequestType request){
        return request;
    }
    public RequestType handleReceiveEmailRequest(RequestType request){
        return request;
    }
    public RequestType handleDeleteEmailRequest(RequestType request){
        return request;
    }



}