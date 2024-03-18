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

    public void initialize(){
        btn_cancel.setOnAction(actionEvent -> {cancelEmail();});
        btn_forward.setOnAction(actionEvent -> {forwardEmail();});
        btn_reply.setOnAction(actionEvent -> {replyEmail();});
    }

    public void cancelEmail(){/*TODO:DA COMPLETARE*/}
    public void forwardEmail(){/*TODO: DA COMPLETARE*/}
    public void replyEmail(){/*TODO: DA COMPLETARE*/}

    public void getDetails(String sender, List<String>recipient, String subject, String text) {
        sender_text.setText(sender);
        receiver_text.setText(String.join(", ", recipient)); /* conversione lista stringhe in una stringa divisa da virgole*/
        subject_text.setText(subject);
        text_area_field.setText(text);
    }





}
