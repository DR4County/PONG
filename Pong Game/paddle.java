import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//Greenfoot.isKeyDown("a")
/**
 * Write a description of class paddleLeft here.
 * 
 * @author David Reyes
 * @version 1
 */
public class paddle extends Actor
{
    public double MOVEMENT_SPEED = 6;
    private boolean Loaded = false;
    public boolean unlockedX = false, resizable = false;
    public int width = 15, height = 150;
    private boolean canCheckKeys = true;
    private int time = 0, keyTime = 0;
    private boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false;
    private String symbolUp = "w", symbolDown = "s", symbolLeft = "a", symbolRight = "d";
    public String name = "default";
    
    /**
     * Inits the needed values
     */
    public paddle(String newName, String up, String down, String left, String right)
    {
        name = newName;
        symbolUp = up; symbolDown = down;
        symbolLeft = left; symbolRight = right;
        GreenfootImage image = getImage();
        image.scale(15, 150); 
    }
    
    /*
     * Runs every frame and checks if the players keys are being pressed to move up, down, left, or right.
     */
    public void act()
    {
        time++;
        if (!Loaded){
            height = getWorld().getHeight() / 4;
            getImage().scale(width, height);
            Loaded = true;
        }
        updateKeys();
        movePaddle();
        
    }
    
    /*
     * Verifys which of the keys are down and is customizable for 4 different keys all being required.
     */
    public void updateKeys(){
        keyUp = Greenfoot.isKeyDown(symbolUp);
        keyDown = Greenfoot.isKeyDown(symbolDown);
        keyLeft = Greenfoot.isKeyDown(symbolLeft);
        keyRight = Greenfoot.isKeyDown(symbolRight);
    }
    
    /*
     * Checks for keypresses and then sets movement
     */
    public void movePaddle()
    {
        if ( keyUp )
        {
            if ( Greenfoot.isKeyDown("=") && resizable && height + 4 < getWorld().getWidth() ){
                getImage().scale(15, height+=4);
            }
            turn(-90);
            move((int)MOVEMENT_SPEED);
            turn(90);
        }
        if ( keyDown )
        {
            if ( Greenfoot.isKeyDown("=") && resizable && height - 4 > 20 ){
                getImage().scale(15, height-=4);
            }
            turn(-90);
            move((int)-MOVEMENT_SPEED);
            turn(90);
        }
        
        if (unlockedX){
            if ( keyLeft )
            {
                turn(-180);
                move((int)MOVEMENT_SPEED);
                turn(180);
            }
            if ( keyRight )
            {
                turn(-180);
                move((int)-MOVEMENT_SPEED);
                turn(180);
            }
        }
    }
    
    
    

}
