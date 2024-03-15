package it.project.server.model;

import it.project.lib.Email;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mailbox{
    private final List<Email> messages;
    private final File fd;

    public Mailbox(String emailAccount) throws URISyntaxException, IOException {
        this.messages = new ArrayList<>();
        File dir = new File(Objects.requireNonNull(Mailbox.class.getResource("database")).toURI());
        this.fd = new File(dir.getPath()+ "/" + emailAccount + ".csv");
    }

    public synchronized List<Email> getMessages(){
        return messages;
    }

    protected boolean createOrExists() throws IOException{
        return this.fd.createNewFile();
    }
    protected synchronized void readMailbox() throws IOException{
        BufferedReader rd = new BufferedReader(new FileReader(fd));
        String line;
        while((line = rd.readLine()) !=  null){
            Email curr = new Email(line);
            messages.add(curr);
        }
        rd.close();
    }

    protected synchronized void writeMailbox() throws IOException{
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages){
            wr.write(email.toString());
        }
        wr.flush();
        wr.close();
    }

    protected synchronized void addMessage(Email m){
        messages.add(m);
    }
    protected synchronized void removeMessage(Email m){messages.remove(m);}
}