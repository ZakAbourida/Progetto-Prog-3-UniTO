package it.project.lib;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Email implements Serializable {
    private int id;
    private String sender;
    private List<String> recipients;
    private String subject;
    private String text;
    private String sentDate;

    public Email(int id, String sender, List<String> recipients, String subject, String text, String sentDate) {
        this.id = id;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
        this.sentDate = sentDate;
    }

    // Getter methods to retrieve values of private variables

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getSentDate() {
        return sentDate;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", recipients=" + recipients +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", sentDate='" + sentDate + '\'' +
                '}';
    }

    public void csvAppend(File path){

    }
}