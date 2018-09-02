package cj.blocks;

public class PlayingField {

    private boolean[][] gameArea;

    public PlayingField(int height, int width) {
        gameArea = new boolean[height][width];
    }

    public boolean[][] getGameArea() {
        return gameArea;
    }

    @Override
    public String toString() {
        for (boolean[] x : gameArea) {
            for (boolean y : x) {
               System.out.print("y");
            }
            System.out.println();
        }
        return super.toString();
    }
}
