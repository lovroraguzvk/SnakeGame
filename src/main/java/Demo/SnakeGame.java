package Demo;

import com.sun.tools.javac.Main;
import entity.Snake;
import exception.SnakeBitItself;
import exception.SnakeWallHit;
import graphics.SnakeGameComponent;
import graphics.SnakeMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SnakeGame extends JFrame {

    public static final int WIDTH_TILES = 20;
    public static final int HEIGHT_TILES = 20;
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final boolean AUTO_RUN = true;
    public static int AUTO_RUN_DELAY = 200;

    private final JDialog dialog = new JDialog(this, "Game Over", true);
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    SnakeMap map = new SnakeMap(WIDTH_TILES, HEIGHT_TILES, BACKGROUND_COLOR);
    Container cp;
    SnakeGameComponent snakeGame = new SnakeGameComponent(map);
    private int currentDirection = Snake.EAST;
    public SnakeGame(int difficulty) {
        setTitle("Snake Game - by Lovro Raguž");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(700, 700));
        setBounds(300, 50, 500, 500);
        initGUI();
        addControls();
        pack();

        AUTO_RUN_DELAY = difficulty;

        if (AUTO_RUN) {
            executor.scheduleAtFixedRate(() -> {
                try {
                    map.moveSnake(currentDirection);
                } catch (SnakeBitItself e) {
                    gameOver("You bit yourself!");
                } catch (SnakeWallHit e) {
                    gameOver("You hit the wall!");
                }
                snakeGame.repaint();
            }, 0, AUTO_RUN_DELAY, java.util.concurrent.TimeUnit.MILLISECONDS);

            this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    executor.shutdown();
                }
            });
        }
    }

    private void initGUI() {
        cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(snakeGame, BorderLayout.CENTER);

    }

    private void difficultyWindow() {

    }

    private void gameOver(String message) {
        executor.shutdown();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(200, 100));
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(message);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);
        dialog.setBounds(500, 300, 200, 100);

        JPanel panel = new JPanel();

        panel.add(new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                dispose();
            }
        }));

        panel.add(new JButton(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                map = new SnakeMap(WIDTH_TILES, HEIGHT_TILES, BACKGROUND_COLOR);
                snakeGame.setMap(map);
                dialog.dispose();
                dispose();
                new MainMenu().setVisible(true);
            }
        }));

        dialog.add(panel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
    }

    public class MoveAction extends AbstractAction {
        private int direction;

        public MoveAction(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentDirection = direction;
            try {
                map.moveSnake(direction);
            } catch (SnakeBitItself e1) {
                gameOver("You bit yourself!");
            } catch (SnakeWallHit e1) {
                gameOver("You hit the wall!");
            }
            snakeGame.repaint();
        }
    }
    public class MoveAndAddAction extends AbstractAction {
        private int direction;

        public MoveAndAddAction(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentDirection = direction;
            try {
                map.moveAndGrowSnake(direction);
            } catch (SnakeBitItself e1) {
                gameOver("You bit yourself!");
            }
            snakeGame.repaint();
        }
    }

    private void addControls() {
        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        snakeGame.getActionMap().put("up", new MoveAction(Snake.NORTH));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        snakeGame.getActionMap().put("down", new MoveAction(Snake.SOUTH));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        snakeGame.getActionMap().put("left", new MoveAction(Snake.WEST));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        snakeGame.getActionMap().put("right", new MoveAction(Snake.EAST));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("shift UP"), "shift up");
        snakeGame.getActionMap().put("shift up", new MoveAndAddAction(Snake.NORTH));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"), "shift down");
        snakeGame.getActionMap().put("shift down", new MoveAndAddAction(Snake.SOUTH));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("shift LEFT"), "shift left");
        snakeGame.getActionMap().put("shift left", new MoveAndAddAction(Snake.WEST));

        snakeGame.getInputMap().put(KeyStroke.getKeyStroke("shift RIGHT"), "shift right");
        snakeGame.getActionMap().put("shift right", new MoveAndAddAction(Snake.EAST));



    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new SnakeGame(200).setVisible(true);
        });
    }

}
