module com.example.caridentificationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;



    opens com.example.caridentificationsystem to javafx.fxml;
    exports com.example.caridentificationsystem;
    exports com.example.caridentificationsystem.controller;
    opens com.example.caridentificationsystem.controller to javafx.fxml;

    // ADD THIS LINE:
    opens com.example.caridentificationsystem.model to javafx.base;

}