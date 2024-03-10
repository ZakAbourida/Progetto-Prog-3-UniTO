package it.project.server;

import it.project.server.model.*;
import it.project.server.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        ServerController controller = new ServerController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();

        Server s = new Server();
        s.setController(controller);
        controller.setServer(s);

        s.startServer();

    }

    public static void main(String[] args) {
        launch();
    }
}