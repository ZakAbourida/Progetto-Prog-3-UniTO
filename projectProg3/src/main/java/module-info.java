module it.project.projectprog3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.project.projectprog3 to javafx.fxml;
    exports it.project.projectprog3;
    exports it.project.projectprog3.controller;
    opens it.project.projectprog3.controller to javafx.fxml;
}