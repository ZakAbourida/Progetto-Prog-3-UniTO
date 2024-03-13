module it.project.Client {
    requires javafx.controls;
    requires javafx.fxml;
    requires it.project.lib;

    opens it.project.Client to javafx.fxml;
    exports it.project.Client;
    exports it.project.Client.controller;
    opens it.project.Client.controller to javafx.fxml;
}