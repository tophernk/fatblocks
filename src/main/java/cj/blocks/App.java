package cj.blocks;

public class App {
    public static void main(String[] args) {
        BlockGame blockGame;
        if (args.length > 0) {
            String tickTime = args[0];
            blockGame = new BlockGame(Integer.valueOf(tickTime));
        } else {
            blockGame = new BlockGame(200);
        }
        blockGame.run();
    }
}
