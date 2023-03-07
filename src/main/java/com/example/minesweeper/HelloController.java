package com.example.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;

public class HelloController {
    private Game current_game;
    private Queue<Game> past_games;

    private boolean correct = false;
    public MenuItem menu_start;
    private int minesNum;
    private int difficulty;
    private int time;
    private int superBomb;
    @FXML
    private Label Marked_mines;
    @FXML
    public GridPane minesgrid;
    @FXML
    private Label total_mines;
    @FXML
    private Label time_left;


    @FXML
    private MenuItem menu_exit;
    @FXML
    private MenuItem menu_load;
    @FXML
    private AnchorPane anchor;

    public void upd_mines_marked(int num) {
        Marked_mines.setText("Marked mines: "+String.valueOf(num));
    }

    @FXML
    private void menu_exit_action(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void menu_create_action(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createpopup.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle("ABC");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void menu_solution_action(ActionEvent actionEvent) {
        current_game.reveal();
    }


    @FXML
    private void menu_load_action(ActionEvent actionEvent) {

        Stage stage = (Stage) anchor.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selected = fileChooser.showOpenDialog(stage);
        try {
            Scanner scanner = new Scanner(selected);

            difficulty = Integer.parseInt(scanner.nextLine());
            minesNum = Integer.parseInt(scanner.nextLine());
            time = Integer.parseInt(scanner.nextLine());
            superBomb = Integer.parseInt(scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (!(difficulty == 1 && (minesNum <= 11 && minesNum >= 9) && (time >= 120 && time <= 180) && superBomb == 0) && !(difficulty == 2 && (minesNum <= 45 && minesNum >= 35) && (time >= 240 && time <= 360) && (superBomb == 0 || superBomb == 1))) {
                throw new InvalidValueException("Invalid values");
            }
            correct = true;
            total_mines.setText("Total Mines: " + String.valueOf(minesNum));
            time_left.setText("Time left: " + String.valueOf(time));
            Marked_mines.setText("Marked mines: ");
        } catch (Exception e) {
            total_mines.setText("Error: Invalid values");
            correct = false;
        }
    }

    @FXML
    public void menu_start_action(ActionEvent actionEvent) {
        if (correct) {
            current_game = new Game(difficulty, minesNum, time,superBomb, this);
            current_game.countAdjMines();
            Marked_mines.setText("Marked mines: " + String.valueOf(current_game.getMinesMarked()));
            for (int x = 0; x < current_game.getSize(); x++) {
                for (int y = 0; y < current_game.getSize(); y++) {
                    current_game.grid[x][y].setPrefSize(50, 50);
                    minesgrid.add(current_game.grid[x][y], x, y);

                }
            }

        }
    }
    private void init_fields(){
        this.correct=false;

    }

    public void game_lost() {
        total_mines.setText("");
        Marked_mines.setText("You Lost");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You lost");
        alert.setHeaderText("Information Alert");
        String s ="This is an example of JavaFX 8 Dialogs... ";
        alert.setContentText(s);
        alert.show();

    }

    public void game_won() {
        Marked_mines.setText("You Won");

    }
}
