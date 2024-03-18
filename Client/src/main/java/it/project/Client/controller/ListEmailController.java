package it.project.Client.controller;

import it.project.Client.ApplicationClient;
import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListEmailController {
    @FXML
    private Button btn_newemail;
    @FXML
    private Button btn_newarrivals;
    @FXML
    private ListView listview_email;


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

        //btn_newarrivals.setOnAction(event -> login());

        // Configura il listener per la selezione nella ListView
        /*
        listview_email.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            email_field.setText(newValue); // Mostra l'email selezionata in email_field
        });
        */
    }
    public void writeEmail() throws IOException {
        // Carica il file FXML per la nuova scena
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Crea un nuovo stage (finestra) per questa scena
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("New Email");

        // Mostra il nuovo stage
        newStage.show();
    }
    @FXML
    public void fillReceivedEmail(Object response){
        if (!(response instanceof List<?>))
            throw new IllegalArgumentException("Was expected a list of Email!");
        else{
            // Il cast è sicuro perché abbiamo verificato che l'oggetto sia una lista
            List<?> emailList = (List<?>) response;

            // Verifica se tutti gli elementi nella lista sono effettivamente oggetti di tipo Email
            for (Object obj : emailList) {
                if (!(obj instanceof Email))
                    throw new IllegalArgumentException("List contains non-Email object!");
            }
            //emailList dunque contiene solo Email
            List<Email> emails = (List<Email>) emailList;

            // pulizia ListView prima di riempirla con le email
            listview_email.getItems().clear();

            // Riempi la ListView con le email ricevute
            for (Email email : emails) {
                String subject = email.getSubject();
                String sender = email.getSender();
                String itemText = "From: \t" + sender + "\t| Subject: \t" + subject;
                listview_email.getItems().add(itemText);
            }
        }
    }

}
