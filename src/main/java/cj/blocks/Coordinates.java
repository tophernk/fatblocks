package cj.blocks;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Coordinates determineNextBlockCoordinates(Block block, int previousBlockY, int previousBlockX) {
        int y = previousBlockY;
        int x = previousBlockX;
        if (block.direction != null) {
            switch (block.direction) {
                case UP:
                    y--;
                    break;
                case DOWN:
                    y++;
                    break;
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
            }
        }
        return new Coordinates(x, y);
    }
}
