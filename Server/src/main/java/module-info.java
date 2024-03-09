module it.project.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires it.project.lib;

    opens it.project.server.controller to javafx.fxml;
    exports it.project.server;
}

