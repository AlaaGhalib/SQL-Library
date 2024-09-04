module se.kth.alialaa.labb1db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens se.kth.alialaa.labb1db to javafx.fxml;
    opens se.kth.alialaa.labb1db.model to javafx.base;

    exports se.kth.alialaa.labb1db;
}