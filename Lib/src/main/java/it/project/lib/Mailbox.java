package it.project.lib;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Mailbox{
    private String emailAccount;
    private List<Email> messages;
    private File fd;

    public Mailbox(String emailAccount) throws URISyntaxException, IOException {
        this.emailAccount = emailAccount;
        this.messages = new ArrayList<>();

        File dir = new File(Objects.requireNonNull(Mailbox.class.getResource("database")).toURI());
        Predicate<File> check = (elem) -> elem.getName().substring(0,elem.getName().lastIndexOf(".")).equals(emailAccount);
        this.fd = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(check)
                .findAny()
                .orElse(newMailbox(dir.getPath()+ "/" + emailAccount + ".csv"));
        readMailbox();
    }

    private synchronized void readMailbox() throws IOException{
        BufferedReader rd = new BufferedReader(new FileReader(fd));
        String line;
        while((line = rd.readLine()) !=  null){
            Email curr = new Email(line);
            messages.add(curr);
        }
        rd.close();
    }

    private synchronized void writeMailbox() throws IOException{
        BufferedWriter wr = new BufferedWriter(new FileWriter(fd));
        for (Email email : messages){
            wr.write(email.toString());
        }
        wr.close();
    }

    private File newMailbox(String path) throws IOException {
        File f = new File(path);
        f.createNewFile();
        System.out.println(f.getAbsolutePath());
        return f;
    }

    public void sendEmail(Email email) {
        messages.add(email);
    }

    public String getEmailAccount() {
        return emailAccount;
    }
}