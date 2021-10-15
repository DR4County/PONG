import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

/**
 * Write a description of class Ball here.
 * 
 * @author David Reyes 
 * @version 4
 */
public class Ball extends Actor
{
    /* List for paddle objects [0] being left and [1] being right. */
    private java.util.List paddles, displays;
    
    /* The last paddle that had been touched by the ball, can also be "none". */
    public paddle lastPaddleTouched;
    
    /* States whether the game has been loaded with non-static variables, 
     * and if the ball has passed the screen, there are other variables which are used for
     * debugging purposes or gamemodes
     */
    private boolean loaded = false,
                    readyToRestart = false,
                    unlockedX = false,
                    resizable = false,
                    collided = false;
                    
    /* States which movement keys are being used; */
    private boolean canCheckKeys = true,
                    keyUp = false,
                    keyDown = false,
                    keyLeft = false,
                    keyRight = false;
    /*
     * [paddleWidth / paddleHeight] Modifiable dimensions for the paddles.
     */
    private int paddleWidth = 15,
                paddleHeight = 150;
    
    /* 
     * [player1sPoints / player2sPoints / maxScore] Displays the amount of points the user has.
     * - also has the max amount of points that can be played until a winner is determined.
     * [themeID] The ID / Number for the theme that will be selected.
     * [maxThemes] The highest amount of themes that are able to be used.
     */
    public int player1sPoints = 0,
               player2sPoints = 0,
               maxScore = 2,
               themeID = 0,
               maxThemes = 7;
    
    /*
     * [dX / dY] The speed and direction that the ball will currently move  in.
     * [w / h] The dimensions of the ball more specifically used with powerups to change the ball size.
     */
    public double dX = 5,
                  dY = 5,
                  w = 30, 
                  h = 30;
    
    /* 
     * [sideLost] The side that has let the ball pass their paddle
     */
    private String sideLost = "none";
    
    /*
     * The sounds for the game which include the paddle collision (hitSound) and the sound when the player looses (looseSound)
     */
    private GreenfootSound 
            hitSound = new GreenfootSound("dry_tick.wav"),
            looseSound = new GreenfootSound("ionic_pulse.wav");
            
    /*
     * The Images used for the game.
     */
    private GreenfootImage backgroundImage = new GreenfootImage("bg-neon.jpg"),
                            ballImage = new GreenfootImage("ball-neon.png"),
                            paddle1Image = new GreenfootImage("paddle-neon.png"),
                            paddle2Image = new GreenfootImage("paddle-neon.png");
    /*
     * Gets the current object's world
     */
    private MyWorld thisWorld = ((MyWorld) getWorld());

    /*
     * The first function to run on the ball / init
     */
    public Ball(){
        hitSound.setVolume(70);
        looseSound.setVolume(70);
        GreenfootImage image = getImage();
        image.scale((int)w, (int)h); 
    }
    
    /*
     * Runs every frame and checks if certain parameters are true to call other functions
     */
    public void act()
    {
        if (!loaded){
            loaded = true;
            
            thisWorld = ((MyWorld) getWorld());
            thisWorld.loadTheme(0);
            paddles = thisWorld.getObjects(paddle.class);
        }
        if (
            thisWorld.time < thisWorld.loadInTime+50 
            || thisWorld.player1sPoints > thisWorld.maxScore
            || thisWorld.player2sPoints > thisWorld.maxScore
           ) return;
        move();
        
        checkForBounce();
        
        if ( checkKeys() == true ){
            thisWorld.keyTime = thisWorld.time;
            canCheckKeys = false;
        };
    }
    
    /*
     * Moves the ball to a new location.
     */
    private void move(){
        setLocation(getX() + (int) dX, getY() + (int) dY);
    }
    
    /*
     * Checks if the ball has a collision with a ball or a paddle
     */
    public void checkForBounce(){
        paddle paddleCollision = (paddle) getOneIntersectingObject(paddle.class);
        powerup powerupCollision = (powerup) getOneIntersectingObject(powerup.class);
        /* Keeps ball at center of screen while resetting */
        if (thisWorld.lost == true){
            setLocation(thisWorld.startX, thisWorld.startY);
        }
        
        /* Prevents ball from getting stuck inside paddle or jittering around */
        if (collided && thisWorld.collisionTime + 50 < thisWorld.time){
            collided = false;
        }
        
        /* Detects if ball is out of map on the X coordinates */
        if (getX() <= 0 || getX() >= getWorld().getWidth()-1){
            thisWorld.lost = true;
            /* Checks if the ball has gone below 0 on the X axis*/
            if (getX() <= 0){
                thisWorld.sideLost = "left";
            }
            /* Checks if the ball has gone above the worlds width on the X axis*/
            else if (getX() >= getWorld().getWidth()-1){
                thisWorld.sideLost = "right";
            }
            /* Inverts the balls X speed essencially bouncing / flipping directions. */
            dX = -dX;
            looseSound.play();
            thisWorld.lostTime = thisWorld.time;
        }
        /* 
         * Checks if the ball hits the top or bottom of the world and if so
         * it will flip the balls Y speed.
         */
        if (getY() <= 0 || getY() >= getWorld().getHeight()-1){
            dY = -dY;
            hitSound.play();
        }
        
        /*
         * Checks if collision between the ball and the paddles is not null,
         * Also checks if there is no collision (also helps prevent sticky paddle).
         */
        if (paddleCollision != null && !collided) {
            paddle collidedPaddle = (paddle) paddleCollision;
            /* Saves the last paddle touched */
            lastPaddleTouched = collidedPaddle;
            dX = -dX;
            hitSound.play();
            collided = true;
            thisWorld.collisionTime = thisWorld.time;
            move();
        }
        
        /* Checks if there is a collision between the ball and any powerup is not null */
        if (powerupCollision != null){
            ((MyWorld) getWorld()).usePowerup(powerupCollision);
        }
    }
    
    /* Draws text on screen at an X and Y value. */
    public void displayText(String text, int x, int y){
        getWorld().showText(text , x, y);
    }
    
    /*
     * Checks if any keys are pressed
     */
    public boolean checkKeys(){
        /* if canCheckKeys is false then wait 25 seconds to make canCheckKeys true then return */
        if (canCheckKeys == false){
            if (thisWorld.keyTime+25 < thisWorld.time){
                canCheckKeys = true;
            }
            return false;
        }
        
        /* Tiny Mode */
        if ( Greenfoot.isKeyDown(".") ){
            thisWorld.themeID += 1;
            thisWorld.loadTheme(thisWorld.themeID);
            return true;
        }
        
        /* Resizable mode */
        if ( Greenfoot.isKeyDown("o") ){
            thisWorld.lost = true;
            thisWorld.lostTime = thisWorld.time;
            thisWorld.player1sPoints = 0;
            thisWorld.player2sPoints = 0;
            
            System.out.println("Reset Game!");
            return true;
        }
        
        /* Resizable mode */
        if ( Greenfoot.isKeyDown("r") ){
            resizable = !resizable;
            paddle paddle1 = (paddle) paddles.get(0); paddle1.resizable = resizable;
            paddle paddle2 = (paddle) paddles.get(1); paddle2.resizable = resizable;
            
            System.out.println("enabled == "+resizable+" resize mode press \"=\" and"+
            "a movement key to change your size");
            return true;
        }
        
        /* Unlock the X axis mode */
        if ( Greenfoot.isKeyDown("x") ){
            unlockedX = !unlockedX;
            paddle paddle1 = (paddle) paddles.get(0); paddle1.unlockedX = unlockedX;
            paddle paddle2 = (paddle) paddles.get(1); paddle2.unlockedX = unlockedX;
            
            System.out.println("Unlocked the X axis == "+unlockedX+"!");
            return true;
        }
        
        return false;
    }
}
