import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math.*;

import static java.lang.Math.pow;

public class JumpingGame extends GameEngine{


    public static void main(String[] args){
        createGame(new JumpingGame());
    }

    public void init() {

    }


    public void update(double dt) {

    }

    public void paintComponent() {
        mFrame.setTitle("Jumping Game");
        // Clear the background to black
        changeBackgroundColor(black);
        clearBackground(width(), height());
    }



}
