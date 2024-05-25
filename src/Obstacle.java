import java.awt.Rectangle;

public class Obstacle {
    int xPos;
    int yPos;
    int width;
    int height;
    int speed;

    public Obstacle(int x, int y, int w, int h, int speed) {
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        this.speed = speed;
    }

    public void update(double dt) {
        xPos -= speed * dt;
    }

    public Rectangle getBounds() {
        return new Rectangle(xPos, yPos, width, height);
    }
}