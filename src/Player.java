public class Player {
    int xPos; //x position for drawing sprite
    int yPos; //y position for drawing sprite
    int width; //width for drawing sprite
    int height; //height for drawing sprite
    int hitboxX; //x position for collisions
    int hitboxY; //y position for collisions
    int hitboxHeight = 50; //height for collisions
    int hitboxWidth = 17; //width for collisions

    public Player(int x, int y, int w, int h){
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        calculateHitbox();
    }

    public void calculateHitbox(){
        hitboxX = xPos + 38;
        hitboxY = yPos + 35;
    }
}
