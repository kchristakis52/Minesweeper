package com.example.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class DetailsController {
    @FXML
    private GridPane grid;

    public void init(Fifo<PastGame> pastGames) {
        int i = 1;
        for (PastGame pastGame : pastGames) {
            Label totalMines = new Label(String.valueOf(pastGame.getTotalMines()));
            Label totalClicks = new Label(String.valueOf(pastGame.getTotalTries()));
            Label totalTime = new Label(String.valueOf(pastGame.getTime()));
            String winnerString = (pastGame.isWin()) ? "You" : "Computer";
            Label winner = new Label(winnerString);
            totalMines.setFont(new Font("System",24));
            totalClicks.setFont(new Font("System",24));
            totalTime.setFont(new Font("System",24));
            winner.setFont(new Font("System",24));

            grid.add(totalMines, 1, i);
            grid.add(totalClicks, 2, i);
            grid.add(totalTime, 3, i);
            grid.add(winner, 4, i);
            i++;

        }

    }
}
