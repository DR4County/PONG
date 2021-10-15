import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class powerup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class powerup extends Actor
{
    public String powerUp = "default";
    public GreenfootImage image = new GreenfootImage("beeper.png");
    /**
     * Inits the required variables and images on creation.
     */
    public powerup (String powerupName){
        powerUp = powerupName;
        switch (powerUp){
            case "pepper":
                image =  new GreenfootImage("powerups/pepper.png");
                image.scale(30, 30); 
            break;
            case "mushroom":
                image =  new GreenfootImage("powerups/mushroom.png");
                image.scale(30, 30); 
            break;
            case "smol":
                image =  new GreenfootImage("beeper.png");
                image.scale(30, 30); 
            break;
            default:
                System.out.println(this+" powerup: "+powerUp);
            break;
        }
        
        setImage(image);
    }

}
