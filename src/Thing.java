//class that stores the information for coins and obstacles

import java.util.Random;
import java.awt.Rectangle;
public class Thing {
    int x,y,w,h,s; // X co-ord, Y co-ord, Width, Height, Speed
    char Type; // Thing type (O for Obstacle, C for Coin)

    public Thing(char T,int W, int H, int X, int Y){ // Assign the x,y,w,h and Type variables, and generate a random speed value
        Random random;
        random = new Random();
        x = X;
        y = Y;
        w = W;
        h = H;
        s = random.nextInt(10,25);
        Type = T;
    }

    //Various getters and setters
    public int getH() {return h;}

    public int getS() {return s;}

    public int getW() {return w;}

    public int getX() {return x;}

    public int getY() {return y;}
    
    public void setX(int x) {this.x = x;}
    
    public char getType() {return Type;}

    public Rectangle getBounds() { //rectangle used for collisions
        return new Rectangle(x, y, w, h);
    }
}
