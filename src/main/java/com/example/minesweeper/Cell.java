package com.example.minesweeper;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Cell extends StackPane {

    private final int x;
    private final int y;
    private int adjMines;
    private final Game game;

    private final Text text = new Text();
    private final Rectangle rectangle = new Rectangle(55, 55);

    private boolean hasMine;
    private boolean revealed;
    private boolean flagged;

    /**
     * @return True if cell contains a Super Mine, False otherwise.
     */
    public boolean isSuperMine() {
        return superMine;
    }

    /**
     * The caller cell will now contain a Super Mine
     */
    public void setSuperMine() {
        this.superMine = true;
    }

    private boolean superMine;
    private boolean hinted;

    /**
     * Creates a new empty cell, sets its size to the correct value
     * depending on the grid size and defines how the player will
     * interact with the cell.
     *
     * @param x    Column number
     * @param y    Row number
     * @param game The Game object that created this Cell object
     */
    public Cell(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.hasMine = false;
        this.game = game;
        this.superMine = false;
        this.revealed = false;
        this.flagged = false;
        this.hinted = false;
        text.setFill(Color.BLACK);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        rectangle.setFill(Color.GREEN);
        if (game.getSize() == 16) {
            rectangle.setHeight(50);
            rectangle.setWidth(50);
        }
        super.getChildren().addAll(rectangle, text);
        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                open();
                game.incTries();
            } else if (e.getButton() == MouseButton.SECONDARY) flag();
        });
    }

    private void flag() {
        if (revealed) return;
        if (flagged) {
            flagged = false;
            text.setText("");
            game.setMinesMarked(game.getMinesMarked() - 1);

        } else {
            if (superMine && game.getTries() <= 4) {
                for (int i = 0; i < 16; i++) {
                    if (i != x) game.getGrid()[i][y].hint();
                    if (i != y) game.getGrid()[x][i].hint();
                }
            }
            if (game.getMinesMarked() >= game.gettotalMines()) return;
            flagged = true;
            text.setText("X");
            game.setMinesMarked(game.getMinesMarked() + 1);
        }
    }

    private void hint() {
        if (revealed || hinted) return;
        revealed = true;
        hinted = true;
        if (adjMines == 0) {
            rectangle.setFill(Color.GRAY);
            game.incOpened();
        } else if (hasMine) {
            rectangle.setFill(Color.GRAY);
            text.setText("X");
        } else {
            text.setText(String.valueOf(adjMines));
            rectangle.setFill(Color.GRAY);
            game.incOpened();
        }
    }

    /**
     * Reveals the caller cell, which contains a mine
     */
    public void spoil() {
        rectangle.setFill(Color.RED);

    }

    private void open() {
        if (revealed) return;
        revealed = true;
        if (adjMines == 0) {
            game.incOpened();
            for (int xoff = -1; xoff <= 1; xoff++) {
                for (int yoff = -1; yoff <= 1; yoff++) {
                    int newx = x + xoff;
                    int newy = y + yoff;
                    if (newx >= 0 && newx < game.getSize() && newy >= 0 && newy < game.getSize() && !(xoff == 0 && yoff == 0)) {
                        rectangle.setFill(Color.GRAY);
                        game.getGrid()[newx][newy].open();
                    }
                }
            }
            if (game.getOpened() >= game.getGoal()) {
                game.win();
            }
        } else if (hasMine) {
            rectangle.setFill(Color.RED);
            game.lost();
        } else {
            text.setText(String.valueOf(adjMines));
            rectangle.setFill(Color.GRAY);
            game.incOpened();
            if (game.getOpened() >= game.getGoal()) {
                game.win();
            }
        }

    }

    /**
     * Sets the field of caller Cell equal to parameter mines
     *
     * @param mines Number of mines adjacent to this cell
     */
    public void setAdjMines(int mines) {
        adjMines = mines;
    }

    /**
     * @return True if caller Cell contains a Mine, False otherwise.
     */
    public boolean gethasMine() {
        return this.hasMine;
    }

    /**
     * The caller Cell will now contain a mine
     */
    public void setHasMine() {
        this.hasMine = true;
    }


}
