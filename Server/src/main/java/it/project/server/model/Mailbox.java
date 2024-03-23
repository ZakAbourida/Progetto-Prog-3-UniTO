package it.project.server.model;

import it.project.lib.Email;
import it.project.server.controller.ServerController;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mailbox {

    private ServerController serverController;
    private List<Email> messages;
    private final File fd;

    public Mailbox(String emailAccount, ServerController controller) throws URISyntaxException, IOException {
        this.serverController = controller;
        this.messages = new ArrayList<>();
        File dir = new File(Objects.requireNonNull(Mailbox.class.getResource("database")).toURI());
        this.fd = new File(dir.getPath() + "/" + emailAccount + ".csv");
    }

    public synchronized List<Email> getMessages() {
        return messages;
    }

    protected boolean createOrExists() throws IOException {
        return this.fd.createNewFile();
    }

    protected synchronized void readMailbox() throws IOException {
        BufferedReader rd = new BufferedReader(new FileReader(fd));
        String line;
        messages = new ArrayList<>();
        while ((line = rd.readLine()) != null) {
            Email curr = new Email(line);
            messages.add(curr);
        }
        rd.close();
    }

    protected synchronized void writeMailbox() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages) {
            wr.write(email.toString());

        }
        wr.flush();
        wr.close();
        serverController.logMessages("La mailbox " + fd.getName().replace(".csv", "") + " ha ricevuto un nuovo messaggio!");
        //serverController.logMessages("Messaggi ricevuti" + messages.size());
    }

    protected synchronized void addMessage(Email m) {
        messages.add(m);
        serverController.logMessages(m.getSender() + " ha inviato una nuova email!");
    }

    protected synchronized void removeMessage(Email m) throws IOException {
        // Rimuove l'email dalla lista di messaggi
        messages.remove(m);

        serverController.logMessages(m.getRecipients().toString() + " ha eliminato un'email!");
    }

    protected synchronized void updateMailbox() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages) {
            wr.write(email.toString());
        }
        wr.flush();
        wr.close();
    }


}