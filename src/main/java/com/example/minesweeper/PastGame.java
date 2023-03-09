package com.example.minesweeper;

public class PastGame {
    private int totalMines;
    private int totalTries;
    private int time;
    private boolean win;

    public PastGame(Game game, boolean win) {
        this.totalMines = game.gettotalMines();
        this.totalTries = game.getTries();
        this.time = game.getTime();
        this.win = win;
    }


}
