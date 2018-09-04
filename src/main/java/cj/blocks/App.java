package cj.blocks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class App implements BlockPrintListener, KeyListener {
    private BlockGame game;
    private JTextArea textArea;

    public App(BlockGame game) {
        this.game = game;
    }

    public static void main(String[] args) {
        BlockGame blockGame;
        if (args.length > 0) {
            String tickTime = args[0];
            blockGame = new BlockGame(Integer.valueOf(tickTime));
        } else {
            blockGame = new BlockGame(200);
        }
        App app = new App(blockGame);
        blockGame.addPrintListener(app);
        app.buildUI();
    }

    private void buildUI() {
        JFrame frame = new JFrame("CJBlocks");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea = new JTextArea(BlockGame.PLAYING_FIELD_HEIGHT, BlockGame.PLAYING_FIELD_WIDTH);
        JButton startGame = new JButton("start game");
        startGame.addActionListener(e -> {
            Thread thread = new Thread(game);
            textArea.addKeyListener(this);
            textArea.requestFocusInWindow();
            thread.start();
        });
        panel.add(textArea);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(startGame);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void print(boolean[][] gameArea) {
        StringBuilder output = new StringBuilder();
        for (boolean[] line : gameArea) {
            output.append("|");
            for (boolean field : line) {
                output.append(field ? "x" : "  ");
            }
            output.append("|\n");
        }
        SwingUtilities.invokeLater(() -> {
            textArea.setText(output.toString());
            textArea.repaint();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case 37:
                game.moveLeft();
                break;
            case 39:
                game.moveRight();
                break;
            case 38:
                game.rotateClockwise();
            default:
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
