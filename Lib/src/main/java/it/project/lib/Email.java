package it.project.lib;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Email implements Serializable {
    private String sender;
    private List<String> recipients;
    private String subject;
    private String text;
    private String date;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m:s");

    public Email(String sender, List<String> recipients, String subject, String text) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
    }

    public Email(String line) {
        String[] parts = line.split(",",5);
        String[] recipients = parts[4].split(",");
        this.recipients = Arrays.stream(recipients).toList();
        this.sender = parts[0];
        this.subject = parts[1];
        this.text = parts[3];
    }

    //costruttore vuoto temporaneo o forse no
    public Email() {

    }

    @Override
    public String toString() {
        return sender + ',' + subject + ',' + text + ',' + date + ',' + recipients;
    }

    // Getter methods to retrieve values of private variables

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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate() {
        this.date = format.format(Calendar.getInstance().getTime());
    }
}