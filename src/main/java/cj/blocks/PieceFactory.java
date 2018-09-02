package cj.blocks;

public class PieceFactory {

    public static Piece createBlockPiece() {
        return new Piece(new Block(null,
                new Block(BlockDirection.RIGHT,
                        new Block(BlockDirection.DOWN,
                                new Block(BlockDirection.LEFT, null)))));
    }

    public static Piece createLinePiece() {
        return new Piece(new Block(null,
                new Block(BlockDirection.DOWN,
                        new Block(BlockDirection.DOWN,
                                new Block(BlockDirection.DOWN, null)))));
    }

    public static Piece createBrokenBlockPiece() {
        return new Piece(new Block(null,
                new Block(BlockDirection.DOWN,
                        new Block(BlockDirection.LEFT,
                                new Block(BlockDirection.DOWN, null)))));
    }

    public static Piece createTheOtherBrokenBlockPiece() {
        return new Piece(new Block(null,
                new Block(BlockDirection.DOWN,
                        new Block(BlockDirection.RIGHT,
                                new Block(BlockDirection.DOWN, null)))));
    }

    public static Piece createTrianglePiece() {
        return new Piece(new Block(null,
                new Block(BlockDirection.DOWN,
                        new Block(BlockDirection.DOWN,
                                new Block(BlockDirection.UP,
                                        new Block(BlockDirection.RIGHT, null))))));
    }
}