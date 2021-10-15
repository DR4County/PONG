import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    public java.util.List paddles, balls;
    
    
    private GreenfootImage backgroundImage = new GreenfootImage("bg-neon.jpg"),
                            ballImage = new GreenfootImage("ball-neon.png"),
                            paddle1Image = new GreenfootImage("paddle-neon.png"),
                            paddle2Image = new GreenfootImage("paddle-neon.png");
    
    public boolean singlePlayer = false, lost = false,
    
                    powerupsEnabled = true;
    
    /* 
     * [startX / startY] The coordinates that the ball will move to when a player looses.
     */
    public int player1sPoints = 0,
               player2sPoints = 0,
               maxScore = 8,
               themeID = 0,
               maxThemes = 7,
               startX = 0,
               startY = 0;
    /*
     * [time] Time in frames to measure how many frames has passed.
     * [LostTime / KeyTime] Time when the player has either lost or pressed a key.
     */
    public int time = 0,
               lostTime = 0,
               keyTime = 0,
               collisionTime = 0,
               loadInTime = 200;
    
    
    
    /* 
     * [sideLost] The side that has let the ball pass their paddle
     */
    public String sideLost = "none";
    
    /*
     * [powerupDelay] The amount of time in frames until the next powerup can be added to the screen.
     * [timeSincepowerup] The amount of time that has been gone since the last power up was added.
     */
    public int powerupDelay = 350,
               timeSincePowerup = 0;
    /**
     * Inits all world values
     */
    public MyWorld()
    {    
        super(800, 600, 1); 
        prepare();
        paddles = getObjects(paddle.class);
        balls = getObjects(Ball.class);
        setPaintOrder(display.class, paddle.class, Ball.class, powerup.class);
        startX = getWidth() / 2+15;
        startY = getHeight() / 2-20;
    }
    
    /*
     * Runs every frame.
     */
    public void act(){
        time += 1;
        if (timeSincePowerup == 0 && powerupsEnabled){
            timeSincePowerup = time;
        }
        else if (timeSincePowerup+powerupDelay < time && powerupsEnabled){
            createPowerup();
            timeSincePowerup = time;
        }
    }
    /*
     * Changes the overall look of the stage / world
     */
    public void loadTheme(int ID){
        paddles = getObjects(paddle.class);
        if ( ID >= maxThemes ){
            themeID = -1;
        }
        else {
            themeID = ID;
        }
        
        if (ID == 0){
            backgroundImage = new GreenfootImage("bg-neon.jpg");
            ballImage = new GreenfootImage("ball-neon.png");
            paddle1Image = new GreenfootImage("paddle-neon.png");
            paddle2Image = new GreenfootImage("paddle-neon.png");
        }
        else if (ID == 1){
            backgroundImage = new GreenfootImage("bg-cartoon-BR.jpg");
            ballImage = new GreenfootImage("ball-cartoon-BR.png");
            paddle1Image = new GreenfootImage("paddle-cartoon-blue.png");
            paddle2Image = new GreenfootImage("paddle-cartoon-red.png");
        }
        else if (ID == 2){
            backgroundImage = new GreenfootImage("rivets.jpg");
            ballImage = new GreenfootImage("ball-planet.png");
            paddle1Image = new GreenfootImage("paddle-lift.png");
            paddle2Image = new GreenfootImage("paddle-lift.png");
        }
        else if (ID == 3){
            backgroundImage = new GreenfootImage("space.jpg");
            ballImage = new GreenfootImage("bricks2.jpg");
            paddle1Image = new GreenfootImage("bricks1.jpg");
            paddle2Image = new GreenfootImage("bricks1.jpg");
        }
        else if (ID == 4){
            backgroundImage = new GreenfootImage("bg-light.png");
            ballImage = new GreenfootImage("ball-dark.png");
            paddle1Image = new GreenfootImage("paddle-dark.png");
            paddle2Image = new GreenfootImage("paddle-dark.png");
        }
        else if (ID == 5){
            backgroundImage = new GreenfootImage("bg-dark.png");
            ballImage = new GreenfootImage("ball-light.png");
            paddle1Image = new GreenfootImage("paddle-light.png");
            paddle2Image = new GreenfootImage("paddle-light.png");
        }
        else if (ID == 6){
            backgroundImage = new GreenfootImage("bg-zombie.png");
            ballImage = new GreenfootImage("ball-zombie.png");
            paddle1Image = new GreenfootImage("paddle-zombie.png");
            paddle2Image = new GreenfootImage("paddle-zombie.png");
        }
        else if (ID == 7){
            backgroundImage = new GreenfootImage("bg-gradient-BR.png");
            ballImage = new GreenfootImage("ball-gradient-BR.png");
            paddle1Image = new GreenfootImage("paddle-gradient-BR-bottom.png");
            paddle2Image = new GreenfootImage("paddle-gradient-BR-top.png");
        }
        Ball ballObj = (Ball) balls.get(0); 
        
        ballImage.scale((int) ballObj.w, (int)ballObj.h);
        ballObj.setImage(ballImage);
        
        paddle paddle1 = (paddle) paddles.get(0); 
        paddle1Image.scale(paddle1.width, paddle1.height);
        paddle1.setImage(paddle1Image);
        
        paddle paddle2 = (paddle) paddles.get(1);
        paddle2Image.scale(paddle2.width, paddle2.height);
        paddle2.setImage(paddle2Image);
        
        setBackground(backgroundImage);
    }
    
    
    /*
     * usePowerup(powerup objectID) - Takes the effect of the powerup, applies it then deletes the displayed powerup.
     */
    public void usePowerup(powerup objectID){
        Ball ballObj = (Ball) balls.get(0); 
        paddle lastPaddleTouched = ballObj.lastPaddleTouched;
        int w = (int) ballObj.w, h = (int) ballObj.h;
        if ( lastPaddleTouched == null ){ return; }
        
        String powerupName = ((powerup) objectID).powerUp;
        paddle paddleToAffect = (paddle) lastPaddleTouched;
        
        removeObject(objectID);
        
        switch (powerupName){
            case "pepper": 
                ballObj.dX += 0.1;
                paddleToAffect.MOVEMENT_SPEED += 0.1;
            break;
            case "mushroom":
                if (w + 1 > 50) return;
                w += 1; h += 1;
                ballImage.scale(w, h);
            break;
            case "smol":
                if (w - 1 > 10) return;
                w -= 1; h -= 1;
                ballImage.scale(w, h);
            break;
        }
    }
    /*
     * Creates a new powerup on coords
     */
    public void createPowerup(){
        
        String[] powerupName = {"pepper", "mushroom", "smol"};
        Random random = new Random();
        
        powerup pup = new powerup(powerupName[random.nextInt(3)]);
        
        int pupX = random.nextInt(getWidth() - 120)+60,
            pupY = random.nextInt(getHeight());
            
        addObject(pup, pupX, pupY);
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        paddle paddleLeft = new paddle("left", "w","s","a","d");
        paddle paddleRight = new paddle("right", "up","down","left","right");
        if (singlePlayer){
            paddleRight = new paddle("right", "w","s","a","d");
        }
        Ball ball = new Ball();

        addObject(ball,getWidth() / 2+12, getHeight() / 2-20);
        addObject(paddleLeft, 50, getHeight() / 2 );
        addObject(paddleRight, getWidth()-50, getHeight() / 2 );

        display display = new display();

        addObject(display,1,1);
        display.setLocation(0,0);
    }
}
