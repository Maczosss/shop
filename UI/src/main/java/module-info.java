module com.example.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires application;
    requires org.jdbi.v3.core;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.ui to javafx.fxml;
    exports com.example.ui;
}

/*
requires org.controlsfx.controls;

        groupId        controls to artifactID
 */