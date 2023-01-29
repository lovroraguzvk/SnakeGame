package Demo;


import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public static final int HARD_DIFFICULTY = 200;
    public static final int MEDIUM_DIFFICULTY = 400;
    public static final int EASY_DIFFICULTY = 600;
    public MainMenu() {
        setTitle("Snake Game - by Lovro Raguž");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 200));
        setBounds(500, 300, 400, 200);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");
        panel.add(easy);
        panel.add(medium);
        panel.add(hard);

        easy.addActionListener(e -> {
            dispose();
            new SnakeGame(EASY_DIFFICULTY).setVisible(true);
        });

        medium.addActionListener(e -> {
            dispose();
            new SnakeGame(MEDIUM_DIFFICULTY).setVisible(true);
        });

        hard.addActionListener(e -> {
            dispose();
            new SnakeGame(HARD_DIFFICULTY).setVisible(true);
        });

        cp.add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

}
