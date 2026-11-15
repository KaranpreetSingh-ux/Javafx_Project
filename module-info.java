module Starter_App.Starter_App {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens Starter_App.Starter_App to javafx.fxml;
    exports Starter_App.Starter_App;
}