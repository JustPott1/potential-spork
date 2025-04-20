module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing;
    requires java.desktop;
    requires com.google.zxing.javase;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}
