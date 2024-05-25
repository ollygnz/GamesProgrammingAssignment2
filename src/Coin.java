import java.awt.Rectangle;

public class Coin {
    int xPos;
    int yPos;
    int radius;
    int speed;

    public Coin(int x, int y, int radius, int speed) {
        xPos = x;
        yPos = y;
        this.radius = radius;
        this.speed = speed;
    }

    public void update(double dt) {
        xPos -= speed * dt;
    }

    public Rectangle getBounds() {
        return new Rectangle(xPos - radius, yPos - radius, radius * 2, radius * 2);
    }
}