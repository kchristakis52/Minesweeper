package com.example.minesweeper;

public class PastGame {
    public int getTotalMines() {
        return totalMines;
    }

    public int getTotalTries() {
        return totalTries;
    }

    public int getTime() {
        return time;
    }

    public boolean isWin() {
        return win;
    }

    private final int totalMines;
    private final int totalTries;
    private final int time;
    private final boolean win;

    public PastGame(Game game, boolean win) {
        this.totalMines = game.gettotalMines();
        this.totalTries = game.getTries();
        this.time = game.getTime();
        this.win = win;
    }


}
