import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;

class Bullet extends Shape {
    public Bullet(int x, int y, int whose, int angle) {
        super(x, y);
        this.whose = whose;
        this.dirX = (int) (Math.cos(Math.toRadians(angle)) * 6);
        this.dirY = -(int) (Math.sin(Math.toRadians(angle)) * 6);
    }

    public Bullet(int x, int y, int whose) {
        super(x, y);
        this.whose = whose;
    }

    public Bullet() {
        super();
    }

    private int whose;
    private int dirX = 2;
    private int dirY = 0;

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

    public int getWhose() {
        return this.whose;
    }

    public void setWhose(int whose) {
        this.whose = whose;
    }

    @Override
    public void render(Graphics g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.draw(new Line2D.Float(this.getX(), this.getY(), this.getX() + this.getDirX() * 10,
                this.getY() + this.getDirY() * 10));
    }

    @Override
    public void render(Graphics g, int width, int height, BufferedImage image, Game game) {
    };
}
