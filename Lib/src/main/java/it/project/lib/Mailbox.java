package it.project.lib;

import java.io.File;
import java.io.IOException;
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
    }

    private File newMailbox(String path) throws IOException {
        File f = new File(path);
        f.createNewFile();
        return f;
    }

    public void sendEmail(Email email) {
        messages.add(email);
    }

    public List<Email> readEmails() {
        return messages;
    }

    public String getEmailAccount() {
        return emailAccount;
    }
}