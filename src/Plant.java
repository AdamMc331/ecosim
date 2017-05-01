import java.util.ArrayList;
import java.util.Random;

/**
 * Plant
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */
public class Plant extends GridObject{

    Plant() {
        super();

        int min = 2;
        int max = 5;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 2-5 (inclusive)
    }

    public int findTarget(ArrayList<GridObject> options) {
        return 0;
    }
}
