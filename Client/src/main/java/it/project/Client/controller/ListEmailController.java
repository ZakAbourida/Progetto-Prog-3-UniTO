package it.project.Client.controller;

import it.project.Client.ApplicationClient;
import it.project.Client.model.Client;
import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private LoginController loginController;
    private EmailController emailController;
    private OpenedEmailController OpEmailController;

    public void setModel(Client model) {
        this.model = model;
    }
    public void setLoginController(LoginController lg){this.loginController = lg;}

    @FXML
    public void initialize() {
        // Qui puoi inizializzare la lista con alcuni dati o configurare comportamenti aggiuntivi
        // Es: listview_email.getItems().addAll("Email 1", "Email 2", "Email 3");
        btn_newemail.setOnAction(actionEvent -> {
            try {
                writeEmail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btn_newarrivals.setOnAction(actionEvent -> {
            UpdateEmail();
        });


        listview_email.setOnMouseClicked(this::showSelectedEmail);

        btn_disconnect.setOnAction(actionEvent -> {
            try {
                Disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

            // Passa le informazioni dell'email al controller della nuova finestra
            OpEmailController.getDetails(selectedEmail.getSender(), selectedEmail.getRecipients(), selectedEmail.getSubject(), selectedEmail.getText());

            // Crea una nuova finestra
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dettagli Email || " + selectedEmail.getSubject());
            stage.show();
        }
    }

    public void Disconnect() throws IOException {
        System.out.println("Disconnessione");
        model.close();
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("login-view.fxml"));

        Stage stage = (Stage) btn_disconnect.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        Client client = new Client();
        ((LoginController)fxmlLoader.getController()).setModel(client, fxmlLoader.getController());
    }

    public void ReplyEmail(String sender, String subject) throws IOException {
        writeEmail();
        emailController.setEmailField(sender);
        emailController.setSubjectField(subject);
    }

    public void UpdateEmail() {
        try {
            // Ottieni l'indirizzo email dal titolo dello stage
            Stage stage  = (Stage) listview_email.getScene().getWindow();
            String email = stage.titleProperty().getValue();


            List<Email> updatedEmails = model.receivedEmail(email); // Utilizza receivedEmail

            // Utilizza il metodo fillReceivedEmail per aggiornare la ListView
            fillReceivedEmail(updatedEmails);
        } catch (Exception e) {
            // Log dell'errore o mostra un messaggio all'utente
            e.printStackTrace();
            // Potresti voler mostrare un dialogo di errore all'utente, per esempio:
            // showErrorDialog("Impossibile aggiornare le email.");
        }
    }

}
