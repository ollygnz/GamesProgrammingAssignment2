import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class JumpingGame extends GameEngine{
    Player player;
    ScreenScroller screenScroller = new ScreenScroller();
    LinkedList<Thing> queue = new LinkedList<>();
    Image sheet;
    Image startMenu;
    Image helpMenu;
    int totalFrames;
    int rows = 3;
    int cols = 6;
    Image[] frames;
    double t;
    double acceleration = -2; //gravity
    int yFloor = 400; //the "floor" that the sprite runs on
    Random rand = new Random();
    AudioClip coinSound;
    AudioClip bgMusic;
    AudioClip loseSound;
    int score = 0;
    public boolean option1 = true, option2 = false;
    enum GameState {Menu, Help, Game, GameOver};
    GameState gameState = GameState.Menu; //when going in and adding a start menu you'll need to change this

    public static void main(String[] args){
        createGame(new JumpingGame());
    }

    public void init() {
        startMenu = loadImage("Images/StartMenu.png");
        helpMenu = loadImage("Images/Help.png");

        player = new Player(0, yFloor, 100, 100);
        sheet = loadImage("Images/playerSprite.png");
        totalFrames = rows * cols;
        frames = new Image[totalFrames];
        for(int iy = 0; iy < rows; iy++)
        {
            for(int ix = 0; ix < cols; ix++)
            {
                frames[iy * cols + ix] = subImage(sheet, ix * 170, iy * 170, 170, 170);
            }
        }
        coinSound = loadAudio("Audio/coin.wav");
        loseSound = loadAudio("Audio/lose.wav");
        bgMusic = loadAudio("Audio/bgm.wav");
        t = 0;
    }
    public int getFrame(double d)
    {
        int frame = (int)Math.floor(((t % d) / d) * totalFrames);
        return frame;
    }

    public void update(double dt) {
        if (gameState == GameState.Menu) {
            return;
        }

        if (gameState == GameState.Help) {
            return;
        }

        if(gameState == GameState.Game){
            ScreenScroller.Scroll(queue);
            ScreenScroller.ShuffleList(queue);
            t+=dt;
            player.updatePosition(acceleration);
            checkRoof();
            // Spawn obstacles
            if (rand.nextInt(200) < 3) { // adjust the frequency as needed
                queue.add(new Thing('O',rand.nextInt(10,50),rand.nextInt(10,50),rand.nextInt(400,500),rand.nextInt(0,450)));
            }

            // Spawn coins
            if (rand.nextInt(100) < 2) { // adjust the frequency as needed
                int newCoinY = 40 + rand.nextInt(440);
                boolean valid = true;
                for (Thing thingtemp : queue) {
                    if (thingtemp.getType() == 'O') {
                        if(newCoinY >= thingtemp.getY() - 50 && newCoinY <= thingtemp.getY() + 150){
                            valid = false;}
                    }
                }
                if(valid){
                    queue.add(new Thing('C', 25, 25, rand.nextInt(400, 500), rand.nextInt(0, 450)));
                }
            }

            for (int i = queue.size() - 1; i >= 0; i--) {
                Thing object = queue.get(i);
                if ((object.getBounds().intersects(new Rectangle((int) player.hitboxX, (int) player.hitboxY, player.hitboxWidth, player.hitboxHeight)))&&object.getType()=='O') {
                    // Game over
                    playAudio(loseSound);
//                    System.out.println("Game Over! Score: " + score);
                    stopAudioLoop(bgMusic);
                    gameState = GameState.GameOver;
                    break;
                } else if ((object.getBounds().intersects(new Rectangle((int) player.hitboxX, (int) player.hitboxY, player.hitboxWidth, player.hitboxHeight)))&&object.getType()=='C'){
                    // Increment Score
                    playAudio(coinSound);
                    queue.remove(i);
                    score++;
//                    System.out.println("Score: " + score);
                    }
            }
        }

    }

    public void checkRoof(){
        if(player.yPos <= 0){
            player.yVelocity = 0;
            player.yPos = 0;
            player.calculateHitbox();
        }
        if(player.yPos >= yFloor){
            player.yVelocity = 0;
            player.yPos = yFloor;
            player.calculateHitbox();
            player.state = Player.States.Running;
        }
    }

    public void startMenuScreen() {
        drawImage(startMenu, 0, 0, width(), height());
        changeColor(red);
        if (option1){
            drawSolidRectangle(30, 310, 20, 20);
        } else if (option2) {
            drawSolidRectangle(30, 350, 20, 20);
        }
    }

    public void helpMenuScreen() {
        drawImage(helpMenu, 0, 0, width(), height());
    }
    public void gameOverScreen(){
        changeColor(white);
        drawText(85, 150, "GAME OVER!", "Arial", 50);
        String scoreString = "Final score: " + score;
        drawText(150, 200, scoreString, "Arial", 30);
    }
    public void paintComponent() {
        mFrame.setTitle("Jumping Game");
        // Clear the background to black
        changeBackgroundColor(black);
        clearBackground(width(), height());

        if (gameState == GameState.Menu) {
            startMenuScreen();
        } else if (gameState == GameState.Help) {
            helpMenuScreen();
        } else if(gameState == GameState.Game) {
            int f = getFrame(1);
            DrawWith1DArray(f);

            // Draw obstacles
            for (Thing temp : queue) {
                if(temp.Type == 'C') {
                    changeColor(Color.YELLOW);
                    drawSolidCircle(temp.getX(), temp.getY(), 12.5);
                } else{
                    changeColor(Color.RED);
                    drawSolidRectangle(temp.getX(), temp.getY(), temp.getW(), temp.getH());
                }
            }
            changeColor(Color.white);
            String scoreString = "Score: " + score;
            drawText(25, 40, scoreString, "Arial", 25);

        } else if(gameState == GameState.GameOver){
            gameOverScreen();
        }
    }

    private void DrawWith1DArray(int f)
    {

        // Draw Selected Frame
        drawImage(frames[f], player.xPos, player.yPos, player.width, player.height);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameState == GameState.Game) {
                player.jump(true);
            }
        }

        if (gameState == GameState.Menu) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                option1 = false;
                option2 = true;
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                option1 = true;
                option2 = false;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (option1) {
                    gameState = GameState.Game;
                    startAudioLoop(bgMusic);
                } else if (option2) {
                    gameState = GameState.Help;
                }
            }
        } else if (gameState == GameState.Help && e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameState = GameState.Menu;
        }
    }

    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.jump(false);
        }
    }

}
