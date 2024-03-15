package it.project.server.model;

import it.project.lib.Email;

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

    @Override
    public void handleMailboxCreated(Mailbox mailbox) {

    }
}
