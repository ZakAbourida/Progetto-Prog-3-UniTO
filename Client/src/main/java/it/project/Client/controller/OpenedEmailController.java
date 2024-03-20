package it.project.Client.controller;

import it.project.lib.Email;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OpenedEmailController {
    @FXML
    private Label sender_text;
    @FXML
    private Label receiver_text;
    @FXML
    private Label subject_text;
    @FXML
    private TextArea text_area_field;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_forward;
    @FXML
    private Button btn_reply;
    private ListEmailController lstEmailController;

    public void setListEmailController(ListEmailController lst_controller) {
        this.lstEmailController = lst_controller;
    }

    public void initialize() {
        btn_cancel.setOnAction(actionEvent -> {
            cancelEmail();
        });
        btn_forward.setOnAction(actionEvent -> {
            try {
                forwardEmail();
            } catch (IOException e) {
                System.out.println("OpenedEmailController - forwardEmail()");
                throw new RuntimeException(e);
            }
        });
        btn_reply.setOnAction(actionEvent -> {
            try {
                replyEmail();
            } catch (IOException e) {
                System.out.println("OpenedEmailController - replyEmail()");
                throw new RuntimeException(e);
            }
        });
        text_area_field.setEditable(false); // così il contenuto sarà READ_ONLY
    }

    public void cancelEmail() {/*TODO:DA COMPLETARE*/}

    public void forwardEmail() throws IOException {
        lstEmailController.ForwardEmail(sender_text.getText(), subject_text.getText(), "Forwarded - \n"+text_area_field.getText());
    }

    public void replyEmail() throws IOException {
        lstEmailController.ReplyEmail(sender_text.getText(), "Reply to: "+subject_text.getText());
    }

    public void setDetails(String sender, List<String> recipient, String subject, String text) {
        sender_text.setText(sender);
        receiver_text.setText(String.join(", ", recipient)); /* conversione lista stringhe in una stringa divisa da virgole*/
        subject_text.setText(subject);
        text_area_field.setText(text);
    }




}
