package it.project.server.model;

import it.project.lib.Email;
import it.project.lib.MailEventReceiver;
import it.project.lib.Mailbox;

public class ServerLogging implements MailEventReceiver {
    @Override
    public void handleMailReceived(Email mail) {

    }

    @Override
    public void handleMailboxUpdate(Mailbox box) {

    }

    @Override
    public void handleException(Exception e) { //TODO
        e.printStackTrace();
    }
}
