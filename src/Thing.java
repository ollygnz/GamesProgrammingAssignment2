import java.util.Random;
public class Thing {
    int x,y,w,h,s;

    public Thing(){
        Random random;
        random = new Random();
        x = random.nextInt(400,500);
        y = random.nextInt(0,400);
        w = random.nextInt(10,50);
        h = random.nextInt(10,50);
        s = random.nextInt(1,10);
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
}
