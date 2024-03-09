module it.polimi.sw.gianpaolocugola47 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.sw.gianpaolocugola47 to javafx.fxml;
    exports it.polimi.sw.gianpaolocugola47;
}