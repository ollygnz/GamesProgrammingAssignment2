import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math.*;

import static java.lang.Math.pow;

public class JumpingGame extends GameEngine{
    Player player;

    Image sheet;
    int totalFrames;
    int rows = 3;
    int cols = 6;
    Image[] frames;
    double t;

    public static void main(String[] args){
        createGame(new JumpingGame());
    }

    public void init() {
        player = new Player(0, 400, 100, 100);
        sheet = loadImage("Images/playerSprite.png");
        totalFrames = rows * cols;
        frames = new Image[totalFrames];
        //sprites = new Image[rows][cols];
        for(int iy = 0; iy < rows; iy++)
        {
            for(int ix = 0; ix < cols; ix++)
            {
                //Flatten out into 1D array index
                System.out.println("x " + ix + " y " + iy);
                frames[iy * cols + ix] = subImage(sheet, ix * 170, iy * 170, 170, 170);
            }
        }
        t = 0;
    }
    public int getFrame(double d)
    {
        int frame = (int)Math.floor(((t % d) / d) * totalFrames);
        return frame;
    }

    public void update(double dt) {
        t+=dt;
    }

    public void paintComponent() {
        mFrame.setTitle("Jumping Game");
        // Clear the background to black
        changeBackgroundColor(black);
        clearBackground(width(), height());

        int f = getFrame(1);
        DrawWith1DArray(f);

        //uncomment this to show the hitbox for testing purposes
//        changeColor(yellow);
//        drawRectangle(player.hitboxX, player.hitboxY, player.hitboxWidth, player.hitboxHeight);
    }

    private void DrawWith1DArray(int f)
    {

        // Draw Selected Frame
        drawImage(frames[f], player.xPos, player.yPos, player.width, player.height);

    }

}
