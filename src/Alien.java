import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;

class Alien extends Shape {

    public Alien(int x, int y, int vitality) {
        super(x, y);
        this.vitality = vitality;
        if (vitality > 20) {
            this.type = "monster";
        }
    }

    public Alien(int x, int y) {
        super(x, y);
    }

    public Alien() {
        super();
    }

    private int dirX = 2;
    private int dirY = 0;
    private int vitality = 1;
    private int timerExplosion = 0;
    private String type = "alien";

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimerExplosion() {
        return this.timerExplosion;
    }

    public void setTimerExplosion(int timerExplosion) {
        this.timerExplosion = timerExplosion;
    }

    public int getVitality() {
        return this.vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public void gotShot(int hurt) {
        this.vitality = this.vitality - hurt;
    }

    public boolean isAlive() {
        if (this.vitality > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getDirX() {
        return this.dirX;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public int getDirY() {
        return this.dirY;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    @Override
    public void render(Graphics g, int width, int height) {
        g.fillOval(this.getX(), this.getY(), 15 + 5 * this.getVitality(), 15 + 5 * this.getVitality());
    };

    @Override
    public void render(Graphics g, int width, int height, BufferedImage image, Game game) {
        if (this.isAlive()) {
            g.drawImage(image, this.getX(), this.getY(), 30 + 10 * this.getVitality(), 30 + 10 * this.getVitality(),
                    game);
        }
    }

    public void renderExplosion(Graphics g, BufferedImage explosion, Game game) {
        if (this.timerExplosion > 0) {
            this.timerExplosion++;
            int explosionSize = 4;
            if (this.type == "monster") {
                explosionSize = 2;
            }
            int startX = (int) (this.timerExplosion % 8);
            int startY = (int) (this.timerExplosion / 8);
            // System.out.println(startX+" - "+startY+" / "+explosion.getWidth()+" -
            // "+explosion.getHeight());
            BufferedImage image = explosion.getSubimage(startX * 256, startY * 256, 256, 256);
            g.drawImage(image, this.getX(), this.getY(), image.getWidth() / explosionSize,
                    image.getHeight() / explosionSize, game);
        }
        if (this.timerExplosion > 46) {
            this.vitality = 0;
            this.timerExplosion = -1;
        }
    }
}
