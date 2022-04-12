import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;

abstract class Shape {
    private int x;
    private int y;

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Shape() {

    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract void render(Graphics g, int width, int height);

    public abstract void render(Graphics g, int width, int height, BufferedImage image, Game game);
}
