//Group members:
// Jaxon Goss (22001841)
// Aidan Callaghan (23006140)
// Sze Leung (22002258) (Anna)
// Olivia Goodman (22015684)

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class JumpingGame extends GameEngine{
    Player player; //the player object that controls everything to do with player position, state, movement
    ScreenScroller screenScroller = new ScreenScroller();
    LinkedList<Thing> queue = new LinkedList<>(); //queue that controls the obstacles and coins movement
    Image sheet; //sprite sheet
    Image startMenu; //background image for menu
    Image helpMenu; //background image for help screen
    int totalFrames; //variables used for sprite animation
    int rows = 3;
    int cols = 6;
    Image[] frames;
    double t;
    double acceleration = -2; //gravity
    int yFloor = 400; //the "floor" that the sprite runs on
    Random rand = new Random(); //used for any time we need a random number
    AudioClip coinSound; //audio files
    AudioClip bgMusic;
    AudioClip loseSound;
    int score = 0; //player score
    public boolean option1 = true, option2 = false; // booleans for menu navigation
    enum GameState {Menu, Help, Game, GameOver}; //game state that controls what screen the player sees
    GameState gameState = GameState.Menu;

    public static void main(String[] args){
        createGame(new JumpingGame());
    }

    public void init() {
        //set up everything required for a new game
        score = 0;
        queue = new LinkedList<>();
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
    public int getFrame(double d) //for sprite animation
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

        if(gameState == GameState.Game){ //only try to update if actually playing game
            ScreenScroller.Scroll(queue);
            ScreenScroller.ShuffleList(queue);
            t+=dt;
            player.updatePosition(acceleration, dt);
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
                    stopAudioLoop(bgMusic);
                    gameState = GameState.GameOver;
                    break;
                } else if ((object.getBounds().intersects(new Rectangle((int) player.hitboxX, (int) player.hitboxY, player.hitboxWidth, player.hitboxHeight)))&&object.getType()=='C'){
                    // Increment Score
                    playAudio(coinSound);
                    queue.remove(i);
                    score++;
                    }
            }
        }

    }

    //checks collisions with the "roof" (top of screen) and "floor" (bottom of screen)
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

    // displays the start menu and where the player has selected
    public void startMenuScreen() {
        drawImage(startMenu, 0, 0, width(), height());
        changeColor(red);
        if (option1){
            drawSolidRectangle(30, 310, 20, 20);
        } else if (option2) {
            drawSolidRectangle(30, 350, 20, 20);
        }
    }

    // displays the help menu
    public void helpMenuScreen() {
        drawImage(helpMenu, 0, 0, width(), height());
    }

    // displays the game over screen, the final score, and where the player has selected

    public void gameOverScreen(){
        changeColor(white);
        drawText(85, 150, "GAME OVER!", "Arial", 50);
        String scoreString = "Final score: " + score;
        drawText(150, 200, scoreString, "Arial", 30);
        drawText(85, 310, "Play again", "Arial", 30);
        drawText(85, 390, "Return to Main Menu", "Arial", 30);
        changeColor(red);
        if (option1){
            drawSolidRectangle(30, 290, 20, 20);
        } else if (option2) {
            drawSolidRectangle(30, 375, 20, 20);
        }
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
        } else if(gameState == GameState.Game) { //only try to animate the character if playing game
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
            String scoreString = "Score: " + score; //displaying score as player is playing
            drawText(380, 40, scoreString, "Arial", 25);

        } else if(gameState == GameState.GameOver){
            gameOverScreen();
        }
    }

    private void DrawWith1DArray(int f) //for sprite animation
    {

        // Draw Selected Frame
        drawImage(frames[f], player.xPos, player.yPos, player.width, player.height);

    }

    public void keyPressed(KeyEvent e) { //key events - jumping during game and menu traversal outside of game
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
                    init();
                    gameState = GameState.Game;
                    startAudioLoop(bgMusic);
                } else if (option2) {
                    gameState = GameState.Help;
                }
            }
        } else if (gameState == GameState.Help && e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameState = GameState.Menu;
        }

        if(gameState == GameState.GameOver){
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                option1 = false;
                option2 = true;
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                option1 = true;
                option2 = false;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (option1) {
                    init();
                    gameState = GameState.Game;
                    startAudioLoop(bgMusic);
                } else if (option2) {
                    gameState = GameState.Menu;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e){ //key released event so the player can "fall" after jumping
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.jump(false);
        }
    }

}
