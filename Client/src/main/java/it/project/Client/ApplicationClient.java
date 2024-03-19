package it.project.Client;

import it.project.Client.controller.LoginController;
import it.project.Client.model.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        Client client = new Client();
        ((LoginController)fxmlLoader.getController()).setModel(client);
    }

    public static void main(String[] args) {
        launch();
    }
}