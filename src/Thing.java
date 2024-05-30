import java.util.Random;
import java.awt.Rectangle;
public class Thing {
    int x,y,w,h,s,r;
    char Type;

    public Thing(char T,int W, int H, int X, int Y){
        Random random;
        random = new Random();
        x = X;
        y = Y;
        w = W;
        h = H;
        s = random.nextInt(10,25);
        r = random.nextInt(10,25);
        Type = T;
    }

    public int getH() {
        return h;
    }

    public int getS() {
        return s;
    }

    public int getW() {
        return w;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setS(int s) {
        this.s = s;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getType() {
        return Type;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }
}
