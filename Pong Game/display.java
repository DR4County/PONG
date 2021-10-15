import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


/**
 * Write a description of class display here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class display extends Actor {
    private java.util.List balls;
    /**
     * Act - do whatever the display wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private MyWorld thisWorld = ((MyWorld) getWorld());
    public GreenfootImage displayImage = new GreenfootImage("clear.png");

    public boolean canChangeImage = true, loaded = false;
    private Ball ballObj;
    /*
     * Runs every frame and checks if it should display an image, text, or nothing.
     */
    public void act() {
        /* If not loaded then run the values needed and set loaded to true */
        if (!loaded) {
            loaded = true;
            thisWorld = ((MyWorld) getWorld());
            balls = thisWorld.getObjects(Ball.class);
        }

        int time = thisWorld.time;
        Ball ballObj = (Ball) balls.get(0);
        /* Checks if the current time is below the loading time and draws a splash screen */
        if (time < thisWorld.loadInTime && canChangeImage) {
            canChangeImage = false;
            /* Changes the image */
            displayImage = new GreenfootImage("splash.png");
            /* Centers the image and changes the size */
            displayImage.scale(thisWorld.getWidth(), thisWorld.getHeight());
            setLocation(thisWorld.getWidth() / 2, thisWorld.getHeight() / 2);
            setImage(displayImage);
        }
        /* Checks if the first players points is greater than or equal to the max score */
        else if (thisWorld.player1sPoints >= thisWorld.maxScore && canChangeImage) {
            canChangeImage = false;
            /* Changes the image */
            displayImage = new GreenfootImage("p1win.png");
            /* Centers the image and changes the size */
            displayImage.scale(thisWorld.getWidth(), thisWorld.getHeight());
            setLocation(thisWorld.getWidth() / 2, thisWorld.getHeight() / 2);
            setImage(displayImage);
            /* Resets the text */
            displayText("⠀ ", thisWorld.startX, thisWorld.startY);
            displayText("⠀ ", 100, 40);
        } 
        /* Checks if the second players points is greater than or equal to the max score */
        else if (thisWorld.player2sPoints >= thisWorld.maxScore && canChangeImage) {
            canChangeImage = false;
            /* Changes the image */
            displayImage = new GreenfootImage("p2win.png");
            /* Centers the image and changes the size */
            displayImage.scale(thisWorld.getWidth(), thisWorld.getHeight());
            setLocation(thisWorld.getWidth() / 2, thisWorld.getHeight() / 2);
            setImage(displayImage);
            /* Resets the text */
            displayText("⠀ ", thisWorld.startX, thisWorld.startY);
            displayText("⠀ ", 100, 40);
        } 
        /* Checks if one of the players has lost or not */
        else if (thisWorld.lost == true) {
            MyWorld world = new MyWorld();
            
            /* Countdown time until ball can move from it's center point */
            if (thisWorld.lostTime + 500 < thisWorld.time) {
                displayText("⠀ ", thisWorld.startX, thisWorld.startY);
                thisWorld.lost = false;
            } else if (thisWorld.lostTime + 400 < thisWorld.time) {
                displayText("Go!", thisWorld.startX, thisWorld.startY);
            } else if (thisWorld.lostTime + 300 < thisWorld.time) {
                displayText("1", thisWorld.startX, thisWorld.startY);
            } else if (thisWorld.lostTime + 200 < thisWorld.time) {
                displayText("2", thisWorld.startX, thisWorld.startY);
            } else if (thisWorld.lostTime + 100 < thisWorld.time) {
                displayText("3", thisWorld.startX, thisWorld.startY);
            }
            /* If there is a side that has lost (Player one or player two) then add a point to the opposite side, and reset the side lost */
            switch (thisWorld.sideLost) {
                case "left":
                    thisWorld.player2sPoints += 1;
                    thisWorld.sideLost = "none";
                    break;
                case "right":
                    thisWorld.player1sPoints += 1;
                    thisWorld.sideLost = "none";
                    break;
            }
        } 
        /* If time is greater than the loading time, and player one along with player twos points are both below the max score */
        else if (
            time > thisWorld.loadInTime &&
            thisWorld.player1sPoints <= thisWorld.maxScore &&
            thisWorld.player2sPoints <= thisWorld.maxScore
        ) {
            displayText("Player 1's score: " + thisWorld.player1sPoints + ".\nPlayer 2's score: " + thisWorld.player2sPoints + ".", 100, 40);
            /* If canChangeImage set image to clear */
            if (!canChangeImage) {
                displayImage = new GreenfootImage("clear.png");
                setImage(displayImage);
                canChangeImage = true;
            }
        }
    }
    
    /*
     * Displays text at a certain coordinate on screen.
     */
    public void displayText(String text, int x, int y) {
        getWorld().showText(text, x, y);
    }
}