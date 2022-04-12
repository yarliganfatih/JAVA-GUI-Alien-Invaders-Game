import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

public class Source {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new Frame();
                frame.setSize(1600, 900);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setFocusable(false);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }
}

class Frame extends JFrame {

    Game game = new Game();
    private final String FIRST = "First panel";
    private final String SECOND = "Second panel";
    private final String THIRD = "Third panel";
    private JPanel mainPanel;

    GridLayout GridLay;
    BorderLayout BorderLay;

    JCheckBox checkBox1 = new JCheckBox("Player 1 (↑,←,↓,→,Space)");
    JTextField TextField1 = new JTextField("Player 1 Name");
    JCheckBox checkBox2 = new JCheckBox("Player 2 (W,A,S,D,Shift)");
    JTextField TextField2 = new JTextField("Player 2 Name");

    private JPanel jContentPane = null;

    public Frame() {
        super("Space Game");
        ReadSavedFromDisk("saved.txt");
        setLayout(BorderLay);
        mainPanel = new JPanel(new CardLayout());
        game.requestFocus();
        game.addKeyListener(game);
        game.setFocusTraversalKeysEnabled(true);
        game.setFocusable(true);
        game.setMainPanel(mainPanel);
        mainPanel.add(getJContentPane(), FIRST);
        mainPanel.add(game, SECOND);
        this.setContentPane(mainPanel);
        this.pack();
    }

    public JPanel getSavedPanel() {
        JPanel panel = new JPanel();
        JLabel savedUsers = new JLabel("Nothing from this game");
        panel.add(savedUsers);
        return panel;
    }

    private ArrayList<String> SavedFromDisk = new ArrayList<String>();

    public void ReadSavedFromDisk(String filepath) {
        Path file = Paths.get("./" + filepath);
        try (InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                SavedFromDisk.add(line);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public JPanel getSavedFromDisk() {
        JPanel panel = new JPanel();
        panel.setBounds(550, 0, 500, 200);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("<< Saved High Scores From Disk >>");
        panel.add(title);
        for (String item : SavedFromDisk) {
            JLabel savedUsers = new JLabel(item);
            panel.add(savedUsers);
        }
        return panel;
    }

    public JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);

            JPanel panel = new JPanel();

            panel.setBounds(550, 0, 500, 200);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            jContentPane.add(panel);

            JPanel player1 = new JPanel();
            player1.setBounds(0, 0, 400, 50);
            player1.setLayout(new BoxLayout(player1, BoxLayout.X_AXIS));
            checkBox1.setSelected(true);
            checkBox1.setEnabled(false);
            player1.add(checkBox1);
            player1.add(TextField1);
            panel.add(player1);

            JPanel player2 = new JPanel();
            player2.setBounds(0, 0, 400, 50);
            player2.setLayout(new BoxLayout(player2, BoxLayout.X_AXIS));
            player2.add(checkBox2);
            player2.add(TextField2);
            panel.add(player2);

            JButton btnStart = new JButton("Start");
            btnStart.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (mainPanel.getLayout());
                    cl.show(mainPanel, SECOND);
                    game.requestFocus();
                    game.addKeyListener(game);
                    game.setFocusTraversalKeysEnabled(true);
                    game.setFocusable(true);
                    if (checkBox1.isSelected()) {
                        // Already checked
                    }
                    if (checkBox2.isSelected()) {
                        String[] players = { "Player 1", "Player 2" };
                        players[0] = TextField1.getText();
                        players[1] = TextField2.getText();
                        game.start(players);
                    } else {
                        String[] players = { "Player 1" };
                        players[0] = TextField1.getText();
                        game.start(players);
                    }
                }
            });
            panel.add(btnStart);
            JButton btnStop = new JButton("Quit");
            btnStop.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            panel.add(btnStop);

            // panel.add(getSavedPanel());

            panel.add(getSavedFromDisk());

        }
        return jContentPane;
    }
}