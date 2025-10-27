import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bird {
    private final List<Image> Images;

    private double x;
    private double y;
    private double height;
    private double width;
    private int currentImage;
    private double velocity;

    // ConstVelocity
    private final double DefaultVelocity = 5;
    private final double Gravity = 0.25;

    public Bird(double x, double y) {
        Images = new ArrayList<Image>();
        for (int i = 1; i <= 3; i++) {
            Image image = new ImageIcon("src/main/java/assets/bird" + i + ".png").getImage();
            Images.add(image);
            height = image.getHeight(null);
            width = image.getWidth(null);
        }
        currentImage = 0;
        velocity = 0;

        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void flap() {
        currentImage = (currentImage + 1) % Images.size();
    }

    public void draw(Graphics g) {
        g.drawImage(Images.get(currentImage),(int) x, (int) y, null);
    }

    public void jump() {
        velocity = -DefaultVelocity;
    }

    public void update() {
        velocity += Gravity;
        y += (int) velocity;
    }
}
