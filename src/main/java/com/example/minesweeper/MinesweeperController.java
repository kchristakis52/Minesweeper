package com.example.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;


public class MinesweeperController {
    private Game current_game;
    private final Fifo<PastGame> past_games = new Fifo<>(5);
    private boolean correct = false;

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
    public Label time_left;


    public void upd_mines_marked(int num) {
        Marked_mines.setText("Marked mines: " + num);
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

            stage.setTitle("Choose scenario");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void menu_solution_action(ActionEvent actionEvent) {
        if (current_game != null) current_game.reveal();
    }


    @FXML
    private void menu_load_action(ActionEvent actionEvent) {
        if (current_game != null) return;
        File selected = null;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Choose description");
        dialog.setHeaderText("Please enter description name");
        dialog.setContentText("Description name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            selected = new File("C:\\Users\\kxris\\Desktop\\Medialab\\" + result.get() + ".txt");
            if (!selected.exists()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("The description file doesn't exist. Previous description was used.");
                alert.showAndWait();
                return;
            }
        } else return;
        long lines = 0;
        try {
            Path path = selected.toPath();

            lines = Files.lines(path).count();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (lines != 4) {
                throw new InvalidDescriptionException("Invalid Description");
            }
        } catch (InvalidDescriptionException e) {
            correct = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The description is invalid. Load another description.");
            alert.showAndWait();
            return;
        }
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
            total_mines.setText("Total Mines: " + minesNum);
            time_left.setText("Time left: " + time);
            //Marked_mines.setText("");
        } catch (Exception e) {
            correct = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The description contains invalid values. Load another description.");
            alert.showAndWait();
        }
    }

    @FXML
    private void menu_start_action(ActionEvent actionEvent) {
        if (correct && current_game == null) {
            current_game = new Game(difficulty, minesNum, time, superBomb, this);
            current_game.countAdjMines();
            Marked_mines.setText("Marked mines: " + current_game.getMinesMarked());
            for (int x = 0; x < current_game.getSize(); x++) {
                for (int y = 0; y < current_game.getSize(); y++) {
                    current_game.getGrid()[x][y].setPrefSize(50, 50);
                    minesgrid.add(current_game.getGrid()[x][y], x, y);
                }
            }
        }

    }

    @FXML
    private void menu_details_action(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detailspopup.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Last 5 games");

            stage.setScene(new Scene(root1));
            DetailsController controller = fxmlLoader.getController();
            controller.init(past_games);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void game_lost() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sorry");
        alert.setHeaderText(null);
        alert.setContentText("You lost... Try again!");
        alert.showAndWait();
        past_games.add(new PastGame(current_game, false));
        init_GUI();
        current_game = null;
    }

    public void game_lost_time() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sorry");
        alert.setHeaderText(null);
        alert.setContentText("Time's up... Try again!");
        alert.show();
        past_games.add(new PastGame(current_game, false));
        init_GUI();
        current_game = null;
    }

    public void game_won() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You won!");
        alert.showAndWait();
        past_games.add(new PastGame(current_game, true));
        init_GUI();
        current_game = null;
    }

    private void init_GUI() {
        minesgrid.getChildren().clear();
        current_game = null;
        total_mines.setText("Total Mines: " + minesNum);
        time_left.setText("Time left: " + time);
        Marked_mines.setText("Marked mines: ");

    }
}
