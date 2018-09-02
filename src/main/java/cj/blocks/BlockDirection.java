package cj.blocks;

public enum BlockDirection {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    BlockDirection(int index) {
        this.index = index;
    }

    private int index;

    public int getIndex() {
        return index;
    }

}
