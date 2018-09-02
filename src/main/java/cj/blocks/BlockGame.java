package cj.blocks;

import cj.blocks.Block;
import cj.blocks.Piece;
import cj.blocks.PieceFactory;
import cj.blocks.PlayingField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockGame {

    public static final int STARTING_X = 6;
    public static final int STARTING_Y = 0;
    public static final int PLAYING_FIELD_WIDTH = 14;
    public static final int PLAYING_FIELD_HEIGHT = 18;
    private PlayingField playingField = new PlayingField(PLAYING_FIELD_HEIGHT, PLAYING_FIELD_WIDTH);
    private Piece currentPiece;
    private int currentPiece_x;
    private int currentPiece_y;
    private boolean toggle;

    public void run() {
        while (noPieceAtTop()) {
            try {
                turn();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "Thread has been interrupted");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("= GAME OVER =");
    }

    private boolean noPieceAtTop() {
        for (int i = 0; i < PLAYING_FIELD_WIDTH; i++) {
            if (playingField.getGameArea()[0][i]) {
                return false;
            }
        }
        return true;
    }

    private void turn() throws InterruptedException {
        generateNewPiece();
        print();
        boolean dropped = true;
        while (dropped) {
            dropped = tick();
            movePieceLaterally();
        }
        addPieceToPlayingField(playingField.getGameArea());
    }

    private void movePieceLaterally() {
        if (toggle) {
            moveRight();
        } else {
            moveLeft();
        }
    }

    public boolean tick() throws InterruptedException {
        Thread.sleep(200);
        boolean dropped = dropPiece();
        print();
        return dropped;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void print() {
        clearScreen();
        boolean[][] currentField = new boolean[PLAYING_FIELD_HEIGHT][PLAYING_FIELD_WIDTH];
        boolean[][] gameArea = playingField.getGameArea();
        for (int y = 0; y < gameArea.length; y++) {
            for (int x = 0; x < gameArea[y].length; x++) {
                currentField[y][x] = gameArea[y][x];
            }
        }
        addPieceToPlayingField(currentField);

        for (boolean[] line : currentField) {
            System.out.print("|");
            for (boolean field : line) {
                System.out.print(field ? "x" : " ");
            }
            System.out.println("|");
        }
    }

    private void addPieceToPlayingField(boolean[][] currentField) {
        int x = currentPiece_x;
        int y = currentPiece_y;
        Block block = currentPiece.getBlock();
        currentField[y][x] = true;
        while (block.hasNext()) {
            block = block.getNext();
            Coordinates coordinates = Coordinates.determineNextBlockCoordinates(block, y, x);
            x = coordinates.getX();
            y = coordinates.getY();
            currentField[y][x] = true;
        }
    }

    private boolean canPieceMoveTo(int relativeY, int relativeX) {
        int x = currentPiece_x;
        int y = currentPiece_y;
        Block block = currentPiece.getBlock();
        if (!canBlockMoveTo(y + relativeY, x + relativeX)) {
            return false;
        }
        while (block.hasNext()) {
            block = block.getNext();
            Coordinates coordinates = Coordinates.determineNextBlockCoordinates(block, y, x);
            x = coordinates.getX();
            y = coordinates.getY();
            if (!canBlockMoveTo(y + relativeY, x + relativeX)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBlockMoveTo(int toY, int toX) {
        return toY < PLAYING_FIELD_HEIGHT &&
                toX >= 0 &&
                toX < PLAYING_FIELD_WIDTH &&
                !playingField.getGameArea()[toY][toX];
    }

    private boolean dropPiece() {
        if (canPieceMoveTo(1, 0)) {
            currentPiece_y++;
            return true;
        }
        return false;
    }

    public void generateNewPiece() {
        if (toggle) {
            currentPiece = PieceFactory.createTrianglePiece();
        } else {
            currentPiece = PieceFactory.createTheOtherBrokenBlockPiece();
        }
        toggle = !toggle;
//        currentPiece = PieceFactory.createBrokenBlockPiece();
//        currentPiece = PieceFactory.createLinePiece();
//        currentPiece = PieceFactory.createBlockPiece();
        currentPiece_x = STARTING_X;
        currentPiece_y = STARTING_Y;
    }

    private void moveRight() {
        if (canPieceMoveTo(0, 1)) {
            currentPiece_x++;
        }
    }

    private void moveLeft() {
        if (canPieceMoveTo(0, -1)) {
            currentPiece_x--;
        }
    }
}
