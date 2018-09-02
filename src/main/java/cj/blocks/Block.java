package cj.blocks;

public class Block {
    BlockDirection direction;
    Block next;

    public Block(BlockDirection direction, Block next) {
        this.direction = direction;
        this.next = next;
    }

    public Block() {
    }

    public boolean hasNext() {
        return next != null;
    }

    public Block getNext() {
        return next;
    }

    public void setNext(Block next) {
        this.next = next;
    }
}