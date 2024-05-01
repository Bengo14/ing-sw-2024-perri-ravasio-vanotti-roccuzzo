module it.polimi.sw.gianpaolocugola47 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;
    opens it.polimi.sw.gianpaolocugola47 to javafx.fxml;
    opens it.polimi.sw.gianpaolocugola47.model to com.google.gson;
    exports it.polimi.sw.gianpaolocugola47.rmi to java.rmi;
    exports it.polimi.sw.gianpaolocugola47.rmi.rmiChat to java.rmi;
}