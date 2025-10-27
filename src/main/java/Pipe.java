import java.awt.*;

public class Pipe {
    private Image image;
    private double x;
    private double y;
    private double width;
    private double height;
    private final double velocity;

    public Pipe(Image image, double x, double y, double width, double height, double velocity) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void update() {
        x -= velocity;
    }

    public void drawPipe(Graphics g) {
        g.drawImage(image, (int) x, (int) y, null);
    }
}
