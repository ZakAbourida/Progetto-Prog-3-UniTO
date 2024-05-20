package it.project.server.model;

import it.project.lib.Email;
import it.project.lib.RequestType;
import it.project.server.controller.ServerController;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerController serverController;

    /**
     * Costruttore della classe ClientHandler.
     *
     * @param socket         Il socket del client.
     * @param controller     Il controller del server.
     * @param serverInstance L'istanza del server.
     * @throws IOException se si verifica un errore durante l'apertura degli stream di input/output.
     */
    public ClientHandler(Socket socket, ServerController controller, Server serverInstance) throws IOException {
        this.clientSocket = socket;
        this.serverController = controller;
        this.server = serverInstance;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Metodo che gestisce il flusso di comunicazione con il client.
     * Riceve le richieste dal client e le gestisce.
     */
    @Override
    public void run() {
        try {
            // Receive the request from the client
            Object request = in.readObject();
            // Manage the request
            handleRequest(request);
        }catch (EOFException ignored){}
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error in managing the client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce le richieste ricevute dal client (1).
     *
     * @param req La richiesta ricevuta dal client.
     * @throws IOException            se si verifica un errore di I/O durante la gestione della richiesta.
     * @throws ClassNotFoundException se non è possibile trovare la classe durante la deserializzazione.
     */
    public void handleRequest(Object req) throws IOException, ClassNotFoundException {
        // Verifica se req è un'istanza di RequestType
        if (!(req instanceof RequestType richiesta)) {
            throw new IllegalArgumentException("Request Not Valid");
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
            case 5: // Close connection request
                handleCloseConnection(richiesta);
                break;
            default:
                throw new IllegalArgumentException("Type request not suppoted");
        }
    }

    /**
     * Gestisce la chiusura della connessione con il client (aggiunge il log).
     *
     * @param request La richiesta di chiusura ricevuta dal client.
     */
    private void handleCloseConnection(RequestType request) throws IOException {
        if (!(request.getEmail().isBlank()))
            server.logMessage("Client disconnected:\t" + request.getEmail());
        in.close();
        out.close();
        clientSocket.close();
    }

    /**
     * Gestisce una richiesta di login da parte del client.
     *
     * @param request La richiesta di login ricevuta dal client.
     * @throws IOException se si verifica un errore durante la gestione della richiesta di login.
     */
    public void handleLoginRequest(RequestType request) throws IOException {
        server.logMessage("Client connected:\t" + request.getEmail());
        Mailbox m = server.getBox(request.getEmail());
        out.writeObject(m.getMessages());
    }

    /**
     * Gestisce una richiesta di invio email da parte del client.
     *
     * @param request La richiesta di invio email ricevuta dal client.
     * @throws IOException            se si verifica un errore durante la gestione della richiesta di invio email.
     * @throws ClassNotFoundException se non è possibile trovare la classe durante la deserializzazione.
     */
    public void handleSendEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)) {
            throw new IllegalArgumentException("An email is expected after a send request");
        }
        for (String recipient : ((Email) mail).getRecipients()) {
            if(Mailbox.exists(recipient)) {
                Mailbox box = server.getBox(recipient);
                box.addMessage((Email) mail);
                box.writeMailbox();
            }else{
                server.logMessage("An email has been sent to " + recipient + " not existent");
            }
        }
    }

    /**
     * Gestisce una richiesta di ricezione email da parte del client.
     *
     * @param request La richiesta di ricezione email ricevuta dal client.
     * @throws IOException se si verifica un errore durante la gestione della richiesta di ricezione email.
     */
    public void handleReceiveEmailRequest(RequestType request) throws IOException {
        Mailbox m = server.getBox(request.getEmail());
        m.readMailbox();
        out.writeObject(m.getMessages());
    }

    /**
     * Gestisce una richiesta di eliminazione email da parte del client.
     *
     * @param request La richiesta di eliminazione email ricevuta dal client.
     * @throws IOException            se si verifica un errore durante la gestione della richiesta di eliminazione email.
     * @throws ClassNotFoundException se non è possibile trovare la classe durante la deserializzazione.
     */
    public void handleDeleteEmailRequest(RequestType request) throws IOException, ClassNotFoundException {
        Object mail = in.readObject();
        if (!(mail instanceof Email)) {
            throw new IllegalArgumentException("An email is expected after a delete request");
        }
        Mailbox m = server.getBox(request.getEmail());
        m.removeMessage((Email) mail);
        m.updateMailbox();
    }
}