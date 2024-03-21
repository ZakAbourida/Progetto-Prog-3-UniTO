package it.project.Client.controller;

import it.project.Client.ApplicationClient;
import it.project.Client.model.Client;
import it.project.lib.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ListEmailController {
    @FXML
    private Button btn_newemail;
    @FXML
    private Button btn_newarrivals;
    @FXML
    private Button btn_disconnect;
    @FXML
    private ListView<Email> listview_email;
    private Client model;
    private EmailController emailController;
    private OpenedEmailController OpEmailController;
    private ListEmailController lst_em; // Sé stesso, anche se questa dichiarazione sembra non necessaria

    public void setModel(Client model) {
        this.model = model;
    }

    public void setListEmailController(ListEmailController lg) {
        this.lst_em = lg;
    }

    @FXML
    public void initialize() {
        // Configura l'azione del bottone per creare una nuova email
        btn_newemail.setOnAction(actionEvent -> {
            try {
                writeEmail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Configura l'azione del bottone per aggiornare la lista delle email
        btn_newarrivals.setOnAction(actionEvent -> UpdateEmail());

        // Imposta il comportamento per quando si clicca su un'email nella lista
        listview_email.setOnMouseClicked(this::showSelectedEmail);

        // Configura l'azione del bottone per disconnettere l'utente
        btn_disconnect.setOnAction(actionEvent -> {
            try {
                Disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Gestisce la chiusura della finestra tramite il pulsante "X"
        Platform.runLater(() -> {
            Stage stage = (Stage) btn_disconnect.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                try {
                    Disconnect(); // Chiama il tuo metodo Disconnect
                } catch (IOException e) {
                    e.printStackTrace(); // Stampa l'errore o gestiscilo come preferisci
                    event.consume(); // Opzionale: previene la chiusura della finestra se ci sono problemi nella disconnessione
                }
            });
        });
    }


    public void writeEmail() throws IOException {
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //prendo l'EmailController della finestra
        emailController = fxmlLoader.getController();
        // Crea un nuovo stage (finestra) per questa scena
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("New Email");
        ((EmailController) fxmlLoader.getController()).setModel(model);

        // Mostra il nuovo stage
        newStage.show();
    }

    @FXML
    public void fillReceivedEmail(Object response) {
        if (!(response instanceof List<?>))
            throw new IllegalArgumentException("Was expected a list of Email!");
        else {
            // Il cast è sicuro perché abbiamo verificato che l'oggetto sia una lista
            List<?> emailList = (List<?>) response;

            // Verifica se tutti gli elementi nella lista sono effettivamente oggetti di tipo Email
            for (Object obj : emailList) {
                if (!(obj instanceof Email))
                    throw new IllegalArgumentException("List contains non-Email object!");
            }
            //emailList dunque contiene solo Email
            List<Email> emails = (List<Email>) emailList;
            // Ordina la lista delle email per data (dalla più recente alla meno recente)
            emails.sort(Comparator.comparing(Email::getDate).reversed());
            // pulizia ListView prima di riempirla con le email
            listview_email.getItems().clear();

            // Riempi la ListView con le email ricevute
            for (Email email : emails) {
                listview_email.getItems().add(email);
            }

            listview_email.setCellFactory(param -> new EmailListCell());
            listview_email.refresh();
        }
    }

    protected void showSelectedEmail(MouseEvent mouseEvent) {
        // Ottieni l'email selezionata dalla ListView
        Email selectedEmail = listview_email.getSelectionModel().getSelectedItem();

        if (selectedEmail != null) {
            // Deseleziona l'elemento selezionato nella ListView
            listview_email.getSelectionModel().clearSelection();

            // Carica il file FXML della finestra "email.fxml"
            FXMLLoader loader = new FXMLLoader(ApplicationClient.class.getResource("email.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                System.out.println("ListEmailController - showSelectedEmail()");
                e.printStackTrace();
                return;
            }

            // Ottieni il controller della nuova finestra
            OpEmailController = loader.getController();

            OpEmailController.setListEmailController(lst_em);

            // Passa le informazioni dell'email al controller della nuova finestra
            OpEmailController.setDetails(selectedEmail.getSender(), selectedEmail.getRecipients(), selectedEmail.getSubject(), selectedEmail.getText(), selectedEmail.getDate());

            // Crea una nuova finestra
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dettagli Email || " + selectedEmail.getSubject());
            stage.show();
        }
    }

    public void Disconnect() throws IOException {
        Stage stage = (Stage) listview_email.getScene().getWindow();
        String email = stage.titleProperty().getValue();
        model.close(email);
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("login-view.fxml"));

        //Stage stage = (Stage) btn_disconnect.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        Client client = new Client();
        ((LoginController) fxmlLoader.getController()).setModel(client, fxmlLoader.getController());
    }

    public void ReplyEmail(String sender, String subject) throws IOException {
        writeEmail();
        emailController.setEmailField(sender);
        emailController.setSubjectField(subject);
    }

    public void ForwardEmail(String sender, String subject, String text) throws IOException {
        writeEmail();
        emailController.setEmailField(sender);
        emailController.setSubjectField(subject);
        emailController.setTextField(text);
    }

    public void UpdateEmail() {
        try {
            // Ottieni l'indirizzo email dal titolo dello stage
            Stage stage = (Stage) listview_email.getScene().getWindow();
            String email = stage.titleProperty().getValue();


            List<Email> updatedEmails = model.receivedEmail(email); // Utilizza receivedEmail

            // Utilizza il metodo fillReceivedEmail per aggiornare la ListView
            fillReceivedEmail(updatedEmails);
        } catch (Exception e) {
            // Log dell'errore o mostra un messaggio all'utente
            e.printStackTrace();
        }
    }

    public void CancelEmail(String sender, String subject, String date) {
        boolean found = false;
        for (Email email : listview_email.getItems()) {
            if (email.getSender().equals(sender) && email.getSubject().equals(subject) && email.getDate().equals(date)) {
                // Trovata l'email corrispondente
                model.cancelEmail(email);
                found = true;
                return; // Esce dal ciclo una volta trovata l'email corrispondente
            }
        }
        if (found == false)
            System.out.println("Nessuna email corrispondente trovata.");
        else
            System.out.println("Email corrispondente trovata.");

        //TODO: FARE REFRESH PER AGGIORNARE LA MAILBOX
    }

}
