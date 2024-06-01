public class Player {
    double xPos; //x position for drawing sprite
    double yPos; //y position for drawing sprite
    int width; //width for drawing sprite
    int height; //height for drawing sprite
    double hitboxX; //x position for collisions
    double hitboxY; //y position for collisions
    int hitboxHeight = 50; //height for collisions
    int hitboxWidth = 17; //width for collisions
    double yVelocity = 0; //y velocity for jumping

    enum States {Running, JumpUp, JumpFall}; // controls whether the user is jumping or not, and where in the jump they are
    // (jetpack joyride style motion)
    States state;

    public Player(int x, int y, int w, int h){
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        calculateHitbox();
        state = States.Running; //user always starts by running
    }

    public void calculateHitbox(){ //by using a hitbox collisions occur in a more "fair" way for the player
        hitboxX = xPos + 38;
        hitboxY = yPos + 35;
    }

    public void jump(boolean pressed){ //changes the jumping state based on player input
        if(state == States.Running || state == States.JumpFall && pressed){
            state = States.JumpUp;
        } else if(state == States.JumpUp && !pressed){
            state = States.JumpFall;
        }
    }

    public void updatePosition(double acceleration, double dt){ //updates the player's position with respect to acceleration
        if(state == States.JumpUp){
            if(yVelocity <= 10){
                yVelocity = yVelocity + (40 - acceleration) * dt;
            }
            yPos -= yVelocity;
            calculateHitbox();
        }
        if(state == States.JumpFall){
            if(yVelocity >= -10) {
                yVelocity += acceleration;
            }
            yPos -= yVelocity;
            calculateHitbox();
        }
    }

}
