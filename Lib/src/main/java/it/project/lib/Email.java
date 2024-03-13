package it.project.lib;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Email implements Serializable {
    private String sender;
    private List<String> recipients;
    private String subject;
    private String text;
    private String date;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m:s");

    public Email(String sender, List<String> recipients, String subject, String text) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
    }

    public Email(String line) {
        String [] parts = line.split(",",5);
        String [] recipients = parts[4].split(",");
        this.sender = parts[0];
        this.recipients = Arrays.stream(recipients).toList();
        this.subject = parts[1];
        this.text = parts[2];
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

}