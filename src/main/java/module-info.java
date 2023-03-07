module com.example.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.minesweeper to javafx.fxml;
    exports com.example.minesweeper;
}