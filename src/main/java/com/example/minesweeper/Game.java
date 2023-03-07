package com.example.minesweeper;

import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.entity.level.tiled.Tile;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Game {
    private int difficulty;

    public int getMinesMarked() {
        return minesMarked;
    }

    public void setMinesMarked(int minesMarked) {
        this.minesMarked = minesMarked;
    }

    public int getWinning() {
        return winning;
    }

    private int winning;
    private int minesMarked;
    private int minesNum;
    private int time;
    private int size;
    private HelloController helloController;
    public Cell[][] grid;

    public Game(int difficulty, int minesNum, int time) {
        this.minesNum = minesNum;
        this.time = time;
        minesMarked = 0;
        winning = 2;
        if (difficulty == 1) {
            this.size = 9;
        } else if (difficulty == 2) {
            this.size = 16;
        }
        this.grid = new Cell[size][];
        for (int x = 0; x < size; x++) {
            grid[x] = new Cell[size];
        }
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Cell(x, y, false, this);

            }
        }
        List<Cell> tobe = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tobe.add(grid[x][y]);
            }
        }
        for (int i = 0; i < minesNum; i++) {
            int index = ThreadLocalRandom.current().nextInt(0, tobe.size() + 1);
            tobe.get(index).setHasMine();
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void win() {
        winning = 1;
    }

    public void lost() {
        winning = 0;

    }

    public int getSize() {
        return size;
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
                            int newx = cell.getX() + xoff;
                            int newy = cell.getY() + yoff;
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

