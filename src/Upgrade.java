import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;

class Upgrade extends Shape {

    private String type = "UpgradeHurt";

    public Upgrade(int x, int y) {
        super(x, y);
    }

    public Upgrade() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void render(Graphics g, int width, int height) {
        g.fillOval(this.getX(), this.getY(), width, height);
    }

    @Override
    public void render(Graphics g, int width, int height, BufferedImage image, Game game) {
        g.drawImage(image, this.getX(), this.getY(), image.getWidth() / 10, image.getHeight() / 10, game);
    }
}