package it.project.Client.controller;

import it.project.lib.Email;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
class EmailListCell extends ListCell<Email> {
    private final GridPane gridPane = new GridPane();
    private final Label senderLabel = new Label();
    private final Label subjectLabel = new Label();
    private final Label dateLabel = new Label();
    public EmailListCell() {
        super();
        // Imposta l'allineamento della griglia
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        // Aggiungi le label alla griglia
        gridPane.add(senderLabel, 0, 0);
        gridPane.add(subjectLabel, 1, 0);
        gridPane.add(dateLabel, 2, 0);
        // Imposta il prefisso per la data
        dateLabel.setTextAlignment(TextAlignment.RIGHT);
        //dateLabel.setPrefWidth(100);
        senderLabel.setStyle("-fx-font-size: 14;");
        subjectLabel.setStyle("-fx-font-size: 14;");
        dateLabel.setStyle("-fx-font-size: 14;");
        // Aggiungi la griglia al componente
        setGraphic(gridPane);
    }

    @Override
    protected void updateItem(Email item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            //setText(item.getSubject());
            // Imposta i valori delle label
            senderLabel.setText(item.getSender());
            subjectLabel.setText(item.getSubject());
            dateLabel.setText(item.getDate());
            System.out.println("email: "+item.getSender()+" "+item.getSubject()+" "+item.getDate());
            // Aggiorna le dimensioni della griglia
            gridPane.setPrefWidth(getListView().getWidth() - 20);
            senderLabel.setPrefWidth(gridPane.getPrefWidth() * 0.25);
            subjectLabel.setPrefWidth(gridPane.getPrefWidth() * 0.50);
            setPrefHeight(34);
            setGraphic(gridPane);
        }
        // Aggiorna la ListView
        getListView().refresh();
    }
}


