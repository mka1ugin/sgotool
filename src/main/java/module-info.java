module com.mkalugin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.mkalugin to javafx.fxml;
    exports com.mkalugin;
}
