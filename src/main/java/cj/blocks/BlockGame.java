package cj.blocks;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockGame implements Runnable {

    private Set<BlockPrintListener> printListeners = new HashSet<>();
    private Random random;

    public void addPrintListener(BlockPrintListener listener) {
        printListeners.add(listener);
    }

    public void removePrintListener(BlockPrintListener listener) {
        printListeners.remove(listener);
    }

    private static final int STARTING_X = 6;
    private static final int STARTING_Y = 0;
    public static final int PLAYING_FIELD_WIDTH = 14;
    public static final int PLAYING_FIELD_HEIGHT = 18;
    private final int tickTime;
    private PlayingField playingField = new PlayingField(PLAYING_FIELD_HEIGHT, PLAYING_FIELD_WIDTH);
    private Piece currentPiece;
    private int currentPiece_x;
    private int currentPiece_y;
    private BlockDirection[] currentOrientation = new BlockDirection[4];

    public BlockGame(int tickTime) {
        this.tickTime = tickTime;
    }

    private void resetOrientation() {
        currentOrientation[0] = BlockDirection.UP;
        currentOrientation[1] = BlockDirection.RIGHT;
        currentOrientation[2] = BlockDirection.DOWN;
        currentOrientation[3] = BlockDirection.LEFT;
    }

    public BlockDirection[] changeOrientation(Rotation rotation) {
        BlockDirection[] result = new BlockDirection[4];
        System.arraycopy(currentOrientation, 0, result, 0, 4);
        BlockDirection tmp = result[0];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = result[i + 1];
        }
        result[result.length - 1] = tmp;
        return result;
    }

    @Override
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
        }
        addPieceToPlayingField(playingField.getGameArea());
    }

    public boolean tick() throws InterruptedException {
        Thread.sleep(tickTime);
        boolean dropped = dropPiece();
        boolean linesRemoved = removeFullLines();
        print();
        return dropped;
    }

    private boolean removeFullLines() {
        boolean linesRemoved = false;
        for (int y = 0; y < PLAYING_FIELD_HEIGHT; y++) {
            boolean removeLine = true;
            for (int x = 0; x < PLAYING_FIELD_WIDTH; x++) {
                if (!playingField.getGameArea()[y][x]) {
                    removeLine = false;
                    break;
                }
            }
            if (removeLine) {
                for (int x = 0; x < PLAYING_FIELD_WIDTH; x++) {
                    playingField.getGameArea()[y][x] = false;
                }
                linesRemoved = true;
            }
        }
        return linesRemoved;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void print() {
        clearScreen();
        final boolean[][] currentField = copyPlayingField();
        addPieceToPlayingField(currentField);
        printListeners.forEach(l -> l.print(currentField));
        for (boolean[] line : currentField) {
            System.out.print("|");
            for (boolean field : line) {
                System.out.print(field ? "x" : " ");
            }
            System.out.println("|");
        }
    }

    private boolean[][] copyPlayingField() {
        boolean[][] currentField = new boolean[PLAYING_FIELD_HEIGHT][PLAYING_FIELD_WIDTH];
        boolean[][] gameArea = playingField.getGameArea();
        for (int y = 0; y < gameArea.length; y++) {
            for (int x = 0; x < gameArea[y].length; x++) {
                currentField[y][x] = gameArea[y][x];
            }
        }
        return currentField;
    }

    private void addPieceToPlayingField(boolean[][] currentField) {
        int x = currentPiece_x;
        int y = currentPiece_y;
        Block block = currentPiece.getBlock();
        currentField[y][x] = true;
        while (block.hasNext()) {
            block = block.getNext();
            Coordinates coordinates = Coordinates.determineNextBlockCoordinates(block, currentOrientation, y, x);
            x = coordinates.getX();
            y = coordinates.getY();
            currentField[y][x] = true;
        }
    }

    private boolean canPieceMoveTo(int relativeY, int relativeX, BlockDirection[] rotatedOrientation) {
        int x = currentPiece_x;
        int y = currentPiece_y;
        Block block = currentPiece.getBlock();
        if (!canBlockMoveTo(y + relativeY, x + relativeX)) {
            return false;
        }
        while (block.hasNext()) {
            block = block.getNext();
            Coordinates coordinates = Coordinates.determineNextBlockCoordinates(block, rotatedOrientation, y, x);
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
        if (canPieceMoveTo(1, 0, currentOrientation)) {
            currentPiece_y++;
            return true;
        }
        return false;
    }

    public void generateNewPiece() {
        random = new Random();
        currentPiece = PieceFactory.nextPiece(random.nextInt(5));
        currentPiece_x = STARTING_X;
        currentPiece_y = STARTING_Y;
        resetOrientation();
    }

    public void moveRight() {
        if (canPieceMoveTo(0, 1, currentOrientation)) {
            currentPiece_x++;
        }
    }

    public void moveLeft() {
        if (canPieceMoveTo(0, -1, currentOrientation)) {
            currentPiece_x--;
        }
    }

    public void rotateClockwise() {
        BlockDirection[] rotatedOrientation = changeOrientation(Rotation.CLOCKWISE);
        if (canPieceMoveTo(0, 0, rotatedOrientation)) {
            currentOrientation = rotatedOrientation;
        }
    }

    private void rotateCounterClockwise() {
        changeOrientation(Rotation.COUNTERCLOCKWISE);
    }
}
