package com.example.minesweeper;

import javafx.animation.AnimationTimer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Game {

    private AnimationTimer timer;
    private boolean restartScheduled;
    private long animationStart;
    private int minesMarked;
    private final int minesNum;
    private final int time;
    private int opened;
    private final int goal;
    private int tries;
    private int size;
    private final MinesweeperController minesweeperController;

    public Cell[][] getGrid() {
        return grid;
    }

    private Cell[][] grid;


    public Game(int difficulty, int minesNum, int time, int supermine, MinesweeperController minesweeperController) {
        this.minesNum = minesNum;
        this.time = time;
        this.minesweeperController = minesweeperController;
        minesMarked = 0;
        opened = 0;
        tries = 0;

        if (difficulty == 1) {
            this.size = 9;
        } else if (difficulty == 2) {
            this.size = 16;
        }
        goal = (int) (Math.pow(size, 2) - minesNum);

        this.grid = new Cell[size][];
        for (int x = 0; x < size; x++) {
            grid[x] = new Cell[size];
        }
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Cell(x, y, this);

            }
        }
        List<Cell> tobe = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tobe.add(grid[x][y]);
            }
        }
        for (int i = 0; i < minesNum; i++) {
            int index = ThreadLocalRandom.current().nextInt(0, tobe.size());
            tobe.get(index).setHasMine();
            if (supermine == 1 && i == 0) tobe.get(index).setSuperMine();
            tobe.remove(index);
        }

        File file = new File("C:\\Users\\kxris\\Desktop\\Medialab\\Mines.txt");
        try {
            Files.deleteIfExists(file.toPath());
            file.createNewFile();
            FileWriter myWriter = new FileWriter("C:\\Users\\kxris\\Desktop\\Medialab\\Mines.txt");
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (grid[x][y].gethasMine()) {
                        int val = (grid[x][y].isSuperMine()) ? 1 : 0;
                        myWriter.write(String.valueOf(y) + "," + String.valueOf(x) + "," + String.valueOf(val) + "\n");
                    }
                }
            }
            //myWriter.write("Files in Java might be tricky, but it is fun enough!\n");
            myWriter.close();
            timer = new AnimationTimer() {
                public void start() {
                    super.start();
                    restartScheduled = true;
                }

                @Override
                public void handle(long now) {
                    if (restartScheduled) {
                        animationStart = now;
                        restartScheduled = false;
                    }
                    now = ((now - animationStart) / 1000000000L);

                    minesweeperController.time_left.setText("Time left: " + String.valueOf(time - now));
                    if (time - now <= 0) lost_time();

                }
            };
            timer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void reveal() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].gethasMine()) {
                    grid[x][y].spoil();


                }
            }
        }
        lost();
    }

    public int getTries() {
        return tries;
    }

    public int getTime() {
        return time;
    }

    public int getMinesMarked() {
        return minesMarked;
    }

    public void setMinesMarked(int minesMarked) {
        this.minesMarked = minesMarked;
        minesweeperController.upd_mines_marked(this.minesMarked);
    }

    public int gettotalMines() {
        return minesNum;
    }

    public void win() {
        timer.stop();
        minesweeperController.game_won();
    }

    public void lost() {
        timer.stop();
        timer = null;
        minesweeperController.game_lost();
    }

    public void lost_time() {
        timer.stop();
        timer = null;
        minesweeperController.game_lost_time();
    }

    public int getSize() {
        return size;
    }

    public int getGoal() {
        return goal;
    }


    public void incTries() {
        tries++;
    }

    public void incOpened() {
        opened++;
    }


    public int getOpened() {
        return opened;
    }

    public void countAdjMines() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Cell cell = grid[x][y];
                if (cell.gethasMine()) {
                    cell.setAdjMines(-1);
                    //cell.text.setText(String.valueOf(-1));
                } else {
                    int adjMines = 0;
                    for (int xoff = -1; xoff <= 1; xoff++) {
                        for (int yoff = -1; yoff <= 1; yoff++) {
                            int newx = x + xoff;
                            int newy = y + yoff;
                            if (newx >= 0 && newx < size && newy >= 0 && newy < size && !(xoff == 0 && yoff == 0)) {
                                if (grid[newx][newy].gethasMine()) ++adjMines;
                            }
                        }
                    }
                    cell.setAdjMines(adjMines);
                    //cell.text.setText(String.valueOf(adjMines));
                }
            }

        }
    }
}

