import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.Timer;
import java.sql.Timestamp; //x
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.geom.Line2D;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.*;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

public class Game extends JPanel implements KeyListener, ActionListener {

    private int level = 1;
    private int difficulty = 5000; // reverse
    Timer timer = new Timer(5, this);

    int screenWidth = 1600;
    int screenHeight = 900;

    private long started_time = 0;

    private ArrayList<Ship> saved = new ArrayList<Ship>();

    private BufferedImage spaceship, alienship, monster, upgradeIcon, background, explosion;

    private ArrayList<Bullet> Bullets = new ArrayList<Bullet>();

    private ArrayList<Bullet> BulletsofAlien = new ArrayList<Bullet>();

    private ArrayList<Alien> aliens = new ArrayList<Alien>();

    private ArrayList<Ship> ships = new ArrayList<Ship>();

    private ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();

    private int timerDeleteUpgrade = 0;
    private int timerFinishBulletUpgrade = 0;

    private int dirY = 2;
    private int BulletHurt = 1;
    private int angleLimit = 30;
    private int stepAngle = 15;
    private int BulletsofAlienHurt = 1;

    public boolean CheckCollision() {
        for (Alien Alien : aliens) {
            for (Bullet Bullet : Bullets) {
                if (new Rectangle(Bullet.getX(), Bullet.getY(), 10, 20).intersects(new Rectangle(Alien.getX(),
                        Alien.getY(), 15 + 5 * Alien.getVitality(), 15 + 5 * Alien.getVitality()))) {
                    Alien.gotShot(BulletHurt);
                    Bullets.remove(Bullet);
                    int shotWhose = Bullet.getWhose();
                    ships.get(shotWhose).ShotAlien();
                    if (!Alien.isAlive()) {
                        Alien.setTimerExplosion(1); // for start explosion
                        ships.get(shotWhose).KillAlien();

                        Random rand = new Random();
                        int rand_int1 = rand.nextInt(20);
                        if (rand_int1 == 0) {
                            upgrades.add(new Upgrade(Alien.getX(), Alien.getY()));
                            timerDeleteUpgrade = 1;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean CheckCollisionforUs() {
        for (Ship ship : ships) {
            if (ship.isAlive()) {
                for (Bullet Bullet : BulletsofAlien) {
                    if (new Rectangle(Bullet.getX(), Bullet.getY(), 10, 20)
                            .intersects(new Rectangle(ship.getX(), ship.getY(), 72, 41))) {
                        ship.gotShot(BulletsofAlienHurt);
                        BulletsofAlien.remove(Bullet);
                        if (!ship.isAlive()) {
                            ship.setTimerExplosion(1); // for start explosion
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void CheckTimers() {
        if (timerDeleteUpgrade > 0) {
            timerDeleteUpgrade++;
        }
        if (timerDeleteUpgrade > 500) {
            upgrades.removeAll(upgrades);
            timerDeleteUpgrade = 0;
        }
        if (timerFinishBulletUpgrade > 0) {
            timerFinishBulletUpgrade++;
        }
        if (timerFinishBulletUpgrade > 1000) {
            BulletHurt--;
            timerFinishBulletUpgrade = 0;
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background, 0, 0, screenWidth, screenHeight, null);

        g.setColor(Color.blue);
        for (Bullet Bullet : Bullets) {
            Bullet.render(g, 20, 10);
        }
        g.setColor(Color.red);
        for (Bullet Bullet : BulletsofAlien) {
            Bullet.render(g, 20, 10);
        }
        g.setColor(Color.green);
        for (Upgrade upgrade : upgrades) {
            upgrade.render(g, 1, 1, upgradeIcon, this);
        }

        g.setColor(Color.red);
        for (Alien Alien : aliens) {
            Random rand = new Random();
            int rand_int1 = rand.nextInt(difficulty);
            rand_int1 -= (int) (angleLimit / stepAngle);
            if (rand_int1 < 3) {
                BulletsofAlien.add(new Bullet(Alien.getX() - 100, Alien.getY() + 10, -1, stepAngle * rand_int1));
            }
            if (Alien.getTimerExplosion() > 0) {
                Alien.renderExplosion(g, explosion, this);
            } else if (Alien.getTimerExplosion() == 0) {
                // Alien.render(g, 20, 20);
                if (Alien.getType() == "alien") {
                    Alien.render(g, 1, 1, alienship, this);
                } else if (Alien.getType() == "monster") {
                    Alien.render(g, 1, 1, monster, this);
                }
            } else if (Alien.getTimerExplosion() == -1) {
                aliens.remove(Alien);
                if (aliens.size() == 0) {
                    timer.stop();
                    this.levelup();
                }
            }
        }

        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        Color c1 = new Color(255, 255, 255, 75);
        g.setColor(c1);
        int shipi = 0;
        for (Ship ship : ships) {
            if (ship.getTimerExplosion() > 0) {
                ship.renderExplosion(g, explosion, this);
            } else {
                ship.render(g, 1, 1, spaceship, this);
            }
            g.drawString("[" + (shipi + 1) + "] Name : " + ship.getName(), 10, screenHeight - 140 - 100 * shipi);
            g.drawString("[" + (shipi + 1) + "] Vitality : " + ship.getVitality(), 10,
                    screenHeight - 120 - 100 * shipi);
            g.drawString("[" + (shipi + 1) + "] Score : " + ship.getScore(), 10, screenHeight - 100 - 100 * shipi);
            g.drawString("[" + (shipi + 1) + "] Velocity : " + ship.getDirY() + " km/s", 10,
                    screenHeight - 80 - 100 * shipi);
            g.drawString("[" + (shipi + 1) + "] Angle : " + ship.getShotAngle() + "°", 10,
                    screenHeight - 60 - 100 * shipi);
            shipi++;
        }

        g.drawString("Time : " + ((timestamp2.getTime() - started_time) / 1000.0) + " s", screenWidth - 130,
                screenHeight - 80);
        g.drawString("Hurt of Bullets : " + BulletHurt, screenWidth - 130, screenHeight - 60);

        for (Bullet Bullet : Bullets) {
            if (Bullet.getX() > screenWidth) {
                Bullets.remove(Bullet);
            }
        }
        for (Bullet Bullet : BulletsofAlien) {
            if (Bullet.getX() < -100) {
                BulletsofAlien.remove(Bullet);
            }
        }

        CheckTimers();

        if (CheckCollision()) {
            if (aliens.size() == 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "You Won");
                // Next Level
            }
        }
        if (CheckCollisionforUs()) {
            if (ships.size() == 2) {
                if (!ships.get(0).isAlive() && !ships.get(1).isAlive()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over");

                    for (Ship ship : ships) {
                        System.out.println(ship.toString());
                        FileWrite("saved.txt", ship.toString());
                        saved.add(ship);
                    }
                    getNewPage();
                } else {
                    if (!ships.get(0).isAlive()) {
                        // Player 1 is dead
                    } else {
                        // Player 2 is dead
                    }
                }
            } else if (ships.size() == 1) {
                if (!ships.get(0).isAlive()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over");

                    for (Ship ship : ships) {
                        System.out.println(ship.toString());
                        FileWrite("saved.txt", ship.toString());
                        saved.add(ship);
                    }
                    getNewPage();
                } else {
                    // Player 1 is shot
                }
            }
            // System.exit(0);
        }
    }

    public void levelup() {
        level++;
        if (level < 4) {
            JOptionPane.showMessageDialog(this, "Next Level : " + level + "\n Ready to start?");
            Bullets.removeAll(Bullets);
            BulletsofAlien.removeAll(BulletsofAlien);

            for (Ship ship : ships) {
                ship.levelUp();
            }
            if (level == 2) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 8; j++) {
                        aliens.add(new Alien(screenWidth - 200 + 60 * i, 10 + 60 * j, i + 1));
                    }
                }
            } else if (level == 3) {
                for (int i = 0; i < 6; i++) {
                    aliens.add(new Alien(screenWidth - 400, screenHeight / 2 - 400 + 60 * i, 3));
                }
                aliens.add(new Alien(screenWidth - 300, screenHeight / 2 - 400, 30));
                difficulty = 1000;
            }
            timer.start();
        } else {
            JOptionPane.showMessageDialog(this, "You Won");

            for (Ship ship : ships) {
                System.out.println(ship.toString());
                FileWrite("saved.txt", ship.toString());
                saved.add(ship);
            }
            getNewPage();
        }
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    public JPanel mainPanel;

    private final String FIRST = "First panel";
    private final String SECOND = "Second panel";
    private final String THIRD = "Third panel";

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void FileWrite(String filepath, String content) {
        content += "\n";
        byte data[] = content.getBytes();
        Path p = Paths.get("./" + filepath);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private ArrayList<String> SavedFromDisk = new ArrayList<String>();
    private ArrayList<String> SavedFromGame = new ArrayList<String>();

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
        JLabel subtitle = new JLabel("Name, Score, Reached Level, Time");
        panel.add(title);
        panel.add(subtitle);
        for (String item : SavedFromDisk) {
            JLabel savedUsers = new JLabel(item);
            panel.add(savedUsers);
        }
        return panel;
    }

    public JPanel getSavedPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(550, 0, 500, 200);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("<< Saved High Scores in This Game >>");
        JLabel subtitle = new JLabel("Name, Score, Reached Level, Time");
        panel.add(title);
        panel.add(subtitle);
        for (Ship item : saved) {
            // SavedFromGame.add(item.toString());
            JLabel savedUsers = new JLabel(item.toString());
            panel.add(savedUsers);
        }
        return panel;
    }

    JCheckBox checkBox1 = new JCheckBox("Player 1 (↑,←,↓,→,Space)");
    JTextField TextField1 = new JTextField("Player 1 Name");
    JCheckBox checkBox2 = new JCheckBox("Player 2 (W,A,S,D,Shift)");
    JTextField TextField2 = new JTextField("Player 2 Name");

    public JPanel ThirdPage() {
        JPanel jContentPane = new JPanel();
        JPanel panel = new JPanel();
        panel.setBounds(550, 0, 500, 200);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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

        JButton btnStart = new JButton("Restart");
        btnStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, SECOND);

                Focus();

                if (checkBox1.isSelected()) {
                    // Already checked
                }
                if (checkBox2.isSelected()) {
                    String[] players = { "Player 1", "Player 2" };
                    players[0] = TextField1.getText();
                    players[1] = TextField2.getText();
                    restart(players);
                } else {
                    String[] players = { "Player 1" };
                    players[0] = TextField1.getText();
                    restart(players);
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

        panel.add(getSavedPanel());
        panel.add(getSavedFromDisk());

        jContentPane.add(panel);

        return jContentPane;
    }

    public void Focus() {
        this.requestFocus();
        this.addKeyListener(this);
        this.setFocusTraversalKeysEnabled(true);
        this.setFocusable(true);
    }

    public void getNewPage() {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        mainPanel.add(ThirdPage(), THIRD);
        cl.show(mainPanel, THIRD);
    }

    public void restart(String[] players) {
        level = 1;
        BulletHurt = 1;
        upgrades.removeAll(upgrades);
        Bullets.removeAll(Bullets);
        BulletsofAlien.removeAll(BulletsofAlien);
        ships.removeAll(ships);
        aliens.removeAll(aliens);
        this.start(players);
        System.out.println("restarted");

    }

    public void start(String[] players) {
        timer.start();
        int fori = 0;
        for (String player : players) {
            ships.add(new Ship(38, screenHeight / 3 + 80 * fori, player));
            fori++;
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                aliens.add(new Alien(screenWidth - 200 + 60 * i, 10 + 60 * j));
            }
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        started_time = timestamp.getTime();
        System.out.println("started");

    }

    public Game() {

        ReadSavedFromDisk("saved.txt");
        try {
            spaceship = ImageIO.read(new FileImageInputStream(new File("img/spaceship.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            background = ImageIO.read(new FileImageInputStream(new File("img/background.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            explosion = ImageIO.read(new FileImageInputStream(new File("img/explosion.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            alienship = ImageIO.read(new FileImageInputStream(new File("img/alien.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            monster = ImageIO.read(new FileImageInputStream(new File("img/monster.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            upgradeIcon = ImageIO.read(new FileImageInputStream(new File("img/upgrade.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (Upgrade upgrade : upgrades) {
                    if (new Rectangle(upgrade.getX(), upgrade.getY(), 50, 50)
                            .intersects(new Rectangle(e.getX(), e.getY(), 1, 1))) {
                        BulletHurt++;
                        System.out.println("Upgraded Hurt of Bullets -> " + BulletHurt);
                        upgrades.remove(upgrade);
                        timerFinishBulletUpgrade = 1;
                    }
                }
            }
        });

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int c = e.getKeyCode();
        if (ships.get(0).isAlive()) {

            if (c == KeyEvent.VK_UP) {
                if (ships.get(0).getY() <= 0) {
                    ships.get(0).setY(0);
                } else {
                    ships.get(0).setY(ships.get(0).getY() - ships.get(0).getDirY());
                }
            } else if (c == KeyEvent.VK_DOWN) {
                if (ships.get(0).getY() >= screenHeight - 100) {
                    ships.get(0).setY(screenHeight - 100);
                } else {
                    ships.get(0).setY(ships.get(0).getY() + ships.get(0).getDirY());
                }
            }
            if (c == KeyEvent.VK_LEFT) {
                if (ships.get(0).getShotAngle() < angleLimit) {
                    ships.get(0).setShotAngle(ships.get(0).getShotAngle() + stepAngle);
                }
            } else if (c == KeyEvent.VK_RIGHT) {
                if (ships.get(0).getShotAngle() > -angleLimit) {
                    ships.get(0).setShotAngle(ships.get(0).getShotAngle() - stepAngle);
                }
            }
            if (c == KeyEvent.VK_SPACE) {
                Bullets.add(new Bullet(100, ships.get(0).getY() + 25, 0, ships.get(0).getShotAngle()));
            }
        }

        if (ships.get(1).isAlive()) {
            if (c == KeyEvent.VK_W) {
                if (ships.get(1).getY() <= 0) {
                    ships.get(1).setY(0);
                } else {
                    ships.get(1).setY(ships.get(1).getY() - ships.get(1).getDirY());
                }
            } else if (c == KeyEvent.VK_S) {
                if (ships.get(1).getY() >= screenHeight - 100) {
                    ships.get(1).setY(screenHeight - 100);
                } else {
                    ships.get(1).setY(ships.get(1).getY() + ships.get(1).getDirY());
                }
            }
            if (c == KeyEvent.VK_A) {
                if (ships.get(1).getShotAngle() < angleLimit) {
                    ships.get(1).setShotAngle(ships.get(1).getShotAngle() + stepAngle);
                }
            } else if (c == KeyEvent.VK_D) {
                if (ships.get(1).getShotAngle() > -angleLimit) {
                    ships.get(1).setShotAngle(ships.get(1).getShotAngle() - stepAngle);
                }
            }
            if (c == KeyEvent.VK_SHIFT) {
                Bullets.add(new Bullet(100, ships.get(1).getY() + 25, 1, ships.get(1).getShotAngle()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (Bullet Bullet : Bullets) {

            Bullet.setX(Bullet.getX() + Bullet.getDirX());
            Bullet.setY(Bullet.getY() + Bullet.getDirY());

        }
        for (Bullet Bullet : BulletsofAlien) { // opposite direction

            Bullet.setX(Bullet.getX() - Bullet.getDirX());
            Bullet.setY(Bullet.getY() - Bullet.getDirY());

        }
        int newdirY = dirY;
        for (Alien Alien : aliens) {

            Alien.setY(Alien.getY() + dirY);
            int swipe = 100;
            if (Alien.getType() == "monster") {
                swipe = 400;
            }
            if (Alien.getY() >= screenHeight - swipe) {
                newdirY = -dirY;
            }
            if (Alien.getY() <= 0) {
                newdirY = -dirY;
            }

        }
        dirY = newdirY;

        repaint();
    }

}
