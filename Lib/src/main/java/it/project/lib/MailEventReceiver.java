package it.project.lib;

public interface MailEventReceiver {
    void handleMailReceived(Email mail);
    void handleMailboxUpdate(Mailbox box);
    void handleException(Exception e);
}