package it.project.server.model;

import it.project.lib.Email;

public class ServerPersistence implements MailEventReceiver{

    @Override
    public void handleMailReceived(Email mail) {

    }

    @Override
    public void handleMailboxUpdate(Mailbox box) {

    }

    @Override
    public void handleException(Exception e) {
        //Do nothing
    }

    @Override
    public void handleMailboxCreated(Mailbox mailbox) {

    }
}
