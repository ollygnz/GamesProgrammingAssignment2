import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class JumpingGame extends GameEngine{
    Player player;
    ScreenScroller screenScroller = new ScreenScroller();
    Image sheet;
    int totalFrames;
    int rows = 3;
    int cols = 6;
    Image[] frames;
    double t;
    double acceleration = -2; //gravity
    int yFloor = 400; //the "floor" that the sprite runs on
    ArrayList<Obstacle> obstacles;
    ArrayList<Coin> coins;
    Random rand = new Random();
    AudioClip coinSound;
    AudioClip bgMusic;
    AudioClip loseSound;
    int score = 0;
    enum GameState {Menu, Game, GameOver};
    GameState gameState = GameState.Game; //when going in and adding a start menu you'll need to change this

    public static void main(String[] args){
        createGame(new JumpingGame());
    }

    public void init() {
        player = new Player(0, yFloor, 100, 100);
        sheet = loadImage("Images/playerSprite.png");
        totalFrames = rows * cols;
        frames = new Image[totalFrames];
        //sprites = new Image[rows][cols];
        for(int iy = 0; iy < rows; iy++)
        {
            for(int ix = 0; ix < cols; ix++)
            {
                //Flatten out into 1D array index
                //System.out.println("x " + ix + " y " + iy);
                frames[iy * cols + ix] = subImage(sheet, ix * 170, iy * 170, 170, 170);
            }
        }
        coinSound = loadAudio("Audio/coin.wav");
        loseSound = loadAudio("Audio/lose.wav");
        bgMusic = loadAudio("Audio/bgm.wav");
        t = 0;
        obstacles = new ArrayList<>();
        coins = new ArrayList<>();
        startAudioLoop(bgMusic);
    }
    public int getFrame(double d)
    {
        int frame = (int)Math.floor(((t % d) / d) * totalFrames);
        return frame;
    }

    public void update(double dt) {
        if(gameState == GameState.Game){
            t+=dt;
            player.updatePosition(acceleration);
            checkRoof();
            // Spawn obstacles
            if (rand.nextInt(200) < 3) { // adjust the frequency as needed
                obstacles.add(new Obstacle(800, 100 + rand.nextInt(300), 50, 50, 200));
            }

            // Spawn coins
            if (rand.nextInt(100) < 2) { // adjust the frequency as needed
                int newCoinY = 40 + rand.nextInt(440);
                for(Obstacle obstacle : obstacles){
                    if(newCoinY <= obstacle.yPos - 50 || newCoinY >= obstacle.yPos + 150){
                        coins.add(new Coin(800, newCoinY, 10, 200));
                    }
                }
            }

            // Update obstacles
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obstacle = obstacles.get(i);
                obstacle.update(dt);
                if (obstacle.xPos + obstacle.width < 0) {
                    obstacles.remove(i);
                } else if (obstacle.getBounds().intersects(new Rectangle((int) player.hitboxX, (int) player.hitboxY, player.hitboxWidth, player.hitboxHeight))) {
                    // Game over
                    playAudio(loseSound);
                    System.out.println("Game Over! Score: " + score);
                    stopAudioLoop(bgMusic);
                    gameState = GameState.GameOver;
                    break;
                }
            }

            // Update coins
            for (int i = coins.size() - 1; i >= 0; i--) {
                Coin coin = coins.get(i);
                coin.update(dt);
                if (coin.xPos + coin.radius * 2 < 0) {
                    coins.remove(i);
                } else if (coin.getBounds().intersects(new Rectangle((int) player.hitboxX, (int) player.hitboxY, player.hitboxWidth, player.hitboxHeight))) {
                    // Collect coin
                    playAudio(coinSound);
                    coins.remove(i);
                    score++;
                    System.out.println("Score: " + score);
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

        if(gameState == GameState.Game) {
            int f = getFrame(1);
            DrawWith1DArray(f);

//        //uncomment this to show the hitbox for testing purposes
//        changeColor(yellow);
//        drawRectangle(player.hitboxX, player.hitboxY, player.hitboxWidth, player.hitboxHeight);
            // Draw obstacles
            changeColor(Color.red);
            for (Obstacle obstacle : obstacles) {
                drawRectangle(obstacle.xPos, obstacle.yPos, obstacle.width, obstacle.height);
            }

            // Draw coins
            changeColor(Color.yellow);
            for (Coin coin : coins) {
                drawCircle(coin.xPos, coin.yPos, coin.radius);
            }
        } else if(gameState == GameState.GameOver){
            gameOverScreen();
        }
    }

    private void DrawWith1DArray(int f)
    {

        // Draw Selected Frame
        drawImage(frames[f], player.xPos, player.yPos, player.width, player.height);

    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.jump(true);
        }
    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.jump(false);
        }
    }

}
