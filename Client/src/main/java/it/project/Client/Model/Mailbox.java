package it.project.Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Mailbox {
    private String emailAccount;
    private List<Email> messages;

    public Mailbox(String emailAccount) {
        this.emailAccount = emailAccount;
        this.messages = new ArrayList<>();
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