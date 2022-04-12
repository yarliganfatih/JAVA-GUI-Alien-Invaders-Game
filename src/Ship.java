import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.time.*;
import java.time.format.DateTimeFormatter;

class Ship extends Shape {

    public Ship(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    public Ship(int x, int y, int vitality) {
        super(x, y);
        this.vitality = vitality;
    }

    public Ship(int x, int y) {
        super(x, y);
    }

    public Ship() {
        super();
    }

    private String name = "";
    private int dirX = 0;
    private int dirY = 15;
    private int vitality = 1;
    private int killAlien = 0;
    private int shotAlien = 0;
    private int shotAngle = 0;
    private int timerExplosion = 0;
    private int score = 0;
    private int reachedLevel = 1;

    public int getReachedLevel() {
        return this.reachedLevel;
    }

    public void setReachedLevel(int reachedLevel) {
        this.reachedLevel = reachedLevel;
    }

    public void levelUp() {
        if (this.vitality > 0) {
            this.reachedLevel++;
        }
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimerExplosion() {
        return this.timerExplosion;
    }

    public void setTimerExplosion(int timerExplosion) {
        this.timerExplosion = timerExplosion;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKillAlien(int killAlien) {
        this.killAlien = killAlien;
    }

    public void setShotAlien(int shotAlien) {
        this.shotAlien = shotAlien;
    }

    public int getShotAngle() {
        return this.shotAngle;
    }

    public void setShotAngle(int shotAngle) {
        this.shotAngle = shotAngle;
    }

    public int getKillAlien() {
        return this.killAlien;
    }

    public void KillAlien() {
        this.killAlien++;
        this.score += 5;
    }

    public int getShotAlien() {
        return this.shotAlien;
    }

    public void ShotAlien() {
        this.shotAlien++;
        this.score += 3;
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

    @Override
    public void render(Graphics g, int width, int height) {
    };

    @Override
    public void render(Graphics g, int width, int height, BufferedImage image, Game game) {
        if (this.isAlive()) {
            g.drawImage(image, this.getX(), this.getY(), image.getWidth() / 10, image.getHeight() / 10, game);
        }
    }

    public void renderExplosion(Graphics g, BufferedImage explosion, Game game) {
        if (this.timerExplosion > 0) {
            this.timerExplosion++;
            int startX = (int) (this.timerExplosion % 8);
            int startY = (int) (this.timerExplosion / 8);
            // System.out.println(startX+" - "+startY+" / "+explosion.getWidth()+" -
            // "+explosion.getHeight());
            BufferedImage image = explosion.getSubimage(startX * 256, startY * 256, 256, 256);
            g.drawImage(image, this.getX(), this.getY(), image.getWidth() / 3, image.getHeight() / 3, game);
        }
        if (this.timerExplosion > 46) {
            this.vitality = 0;
            this.timerExplosion = -1;
        }
    }

    public String toString() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println(date.format(formatter));
        return this.name + ", " + this.score + ", " + this.reachedLevel + "," + date.format(formatter);
    }
}