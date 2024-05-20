package it.project.server.model;

import it.project.lib.Email;
import it.project.server.controller.ServerController;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mailbox {

    private Server server;
    private List<Email> messages;
    private final File fd;
    private String address;

    public Mailbox(String emailAccount, Server server) throws URISyntaxException, IOException {
        this.server = server;
        this.messages = new ArrayList<>();
        File dir = new File(Objects.requireNonNull(Mailbox.class.getResource("database")).toURI());
        this.fd = new File(dir.getPath() + "/" + emailAccount + ".csv");
        this.address = emailAccount;
    }

    public static boolean exists(String recipient) {
        try {
            File dir = new File(Objects.requireNonNull(Mailbox.class.getResource("database")).toURI());
            File fd = new File(dir.getPath() + "/" + recipient + ".csv");
            return fd.exists();
        }catch (URISyntaxException ignored){}
        return false;
    }

    /**
     * Restituisce la lista dei messaggi presenti nella casella di posta.
     *
     * @return La lista dei messaggi presenti nella casella di posta.
     */
    public synchronized List<Email> getMessages() {
        return messages;
    }

    /**
     * Verifica se il file della casella di posta esiste già. Se non esiste, lo crea.
     *
     * @return true se il file è stato creato correttamente, altrimenti false.
     * @throws IOException se si verifica un errore durante la creazione del file.
     */
    protected boolean createOrExists() throws IOException {
        return this.fd.createNewFile();
    }

    /**
     * Legge i messaggi salvati nella casella di posta dal file CSV.
     *
     * @throws IOException se si verifica un errore durante la lettura del file.
     */
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

    /**
     * Scrive i messaggi della casella di posta sul file CSV.
     *
     * @throws IOException se si verifica un errore durante la scrittura del file.
     */
    protected synchronized void writeMailbox() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages) {
            wr.write(email.toString());

        }
        wr.flush();
        wr.close();
        server.logMessage("The mailbox " + fd.getName().replace(".csv", "") + " has received a new message!");
    }

    /**
     * Aggiunge un nuovo messaggio alla casella di posta e aggiunge il log.
     *
     * @param m Il messaggio da aggiungere alla casella di posta.
     */
    protected synchronized void addMessage(Email m) {
        messages.add(m);
        server.logMessage(m.getSender() + " sent a new email!");
    }

    /**
     * Rimuove un messaggio dalla casella di posta e aggiunge il log.
     *
     * @param m Il messaggio da rimuovere dalla casella di posta.
     * @throws IOException se si verifica un errore durante la rimozione del messaggio.
     */
    protected synchronized void removeMessage(Email m) throws IOException {
        // Rimuove l'email dalla lista di messaggi
        messages.remove(m);

        server.logMessage(this.address + " deleted an email");
    }

    /**
     * Aggiorna il file CSV della casella di posta con i messaggi attualmente presenti.
     *
     * @throws IOException se si verifica un errore durante l'aggiornamento del file.
     */
    protected synchronized void updateMailbox() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages) {
            wr.write(email.toString());
        }
        wr.flush();
        wr.close();
    }
}