/**
 * Plant
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;

public class Plant extends GridObject{

    Plant() {
        super();
        //TODO: Use this SO answer for a better way to get a random number between two ints: http://stackoverflow.com/a/5887736/3131147
        int min = 2;
        int max = 5;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 2-5 (inclusive)
    }

    public int findTarget(ArrayList<GridObject> options) {

        return 0;
    }

}
