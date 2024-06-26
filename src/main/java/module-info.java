module it.polimi.sw.gianpaolocugola47 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires error.prone.annotations;
    opens it.polimi.sw.gianpaolocugola47 to javafx.fxml;
    opens it.polimi.sw.gianpaolocugola47.view.gui to javafx.fxml;
    opens it.polimi.sw.gianpaolocugola47.model to com.google.gson;
    opens it.polimi.sw.gianpaolocugola47.controller to com.google.gson;
    opens it.polimi.sw.gianpaolocugola47.utils to com.google.gson;
    exports it.polimi.sw.gianpaolocugola47.network to java.rmi;
    exports it.polimi.sw.gianpaolocugola47.utils to java.rmi;
    exports it.polimi.sw.gianpaolocugola47.network.rmi to java.rmi;
    exports it.polimi.sw.gianpaolocugola47.view.gui to javafx.graphics;
    opens it.polimi.sw.gianpaolocugola47.network to com.google.gson;
}