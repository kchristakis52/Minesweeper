package com.example.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Createpopup {
    @FXML
    private AnchorPane anchor;
    @FXML
    private TextField scenario;
    @FXML
    private TextField mines;
    @FXML
    private TextField time;
    @FXML
    private Button ok;
    @FXML
    private ToggleGroup a;

    @FXML
    private CheckBox supermine;

    @FXML
    private void ok_action(ActionEvent actionEvent) {

        File file = new File("C:\\Users\\kxris\\Desktop\\Medialab\\" + scenario.getText() + ".txt");
        try {

            Files.deleteIfExists(file.toPath());
            file.createNewFile();
            FileWriter myWriter = new FileWriter("C:\\Users\\kxris\\Desktop\\Medialab\\" + scenario.getText() + ".txt");
            RadioButton selectedRadioButton = (RadioButton) a.getSelectedToggle();
            myWriter.write(selectedRadioButton.getText() + "\n");
            myWriter.write(mines.getText() + "\n");
            myWriter.write(time.getText() + "\n");
            String supermine_string = (supermine.isSelected()) ? "1\n" : "0\n";
            myWriter.write(supermine_string);
            myWriter.close();
            Stage stage = (Stage) anchor.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
