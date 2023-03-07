package com.example.minesweeper;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Cell extends StackPane {

    private final int x;
    private final int y;
    private int adjMines;
    private final Game game;

    public Text text = new Text();
    private Rectangle rectangle = new Rectangle(50, 50);

    private boolean hasMine;
    private boolean revealed;
    private boolean flagged;

    public boolean isSuperMine() {
        return superMine;
    }

    public void setSuperMine(boolean superMine) {
        this.superMine = superMine;
    }

    private boolean superMine;

    public Cell(int x, int y, boolean hasMine, Game game) {
        this.x = x;
        this.y = y;
        this.hasMine = hasMine;
        this.game = game;
        this.superMine=false;
        this.revealed = false;
        this.flagged = false;
        text.setFill(Color.BLACK);
        rectangle.setFill(Color.GREEN);
        super.getChildren().addAll(rectangle, text);
        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                open();
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
            flagged = true;
            text.setText("X");
            game.setMinesMarked(game.getMinesMarked() + 1);
        }
    }

    private void open() {
        if (revealed) return;
        revealed = true;
        if (adjMines == 0) {
            for (int xoff = -1; xoff <= 1; xoff++) {
                for (int yoff = -1; yoff <= 1; yoff++) {
                    int newx = x + xoff;
                    int newy = y + yoff;
                    if (newx >= 0 && newx < game.getSize() && newy >= 0 && newy < game.getSize() && !(xoff == 0 && yoff == 0)) {
                        rectangle.setFill(Color.GRAY);
                        game.grid[newx][newy].open();
                    }
                }
            }
        } else if (hasMine) {
            rectangle.setFill(Color.RED);
        } else {
            text.setText(String.valueOf(adjMines));
            rectangle.setFill(Color.GRAY);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setAdjMines(int mines) {
        adjMines = mines;
    }

    public boolean gethasMine() {
        return this.hasMine;
    }

    public boolean getrevealed() {
        return this.revealed;
    }

    public void setrevealed(boolean a) {
        this.revealed = a;
    }

    public void setHasMine() {
        this.hasMine = !hasMine;
    }


}
