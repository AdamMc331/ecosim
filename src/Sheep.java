import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Sheep
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */
public class Sheep extends GridObject {

    Sheep() {
        super();
        int min = 50;
        int max = 100;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 50-100 (inclusive)
    }

    Sheep(int health) {
        super();
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {

        ArrayList<GridObject> newOptions = new ArrayList<>(options);

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        int firstSheep = -1;
        int firstPlant = -1;
        for (int i = 0; i < newOptions.size(); i++) {
            GridObject gridObject = newOptions.get(i);

            // Check for a sheep and break if we've found one.
            // Otherwise, check if we have a plant and store the first index.
            if (gridObject instanceof Sheep && gridObject.getGender() != super.getGender() && gridObject.getHealth() >= EcoSim.MIN_MATE_HEALTH && super.getHealth() >= EcoSim.MIN_MATE_HEALTH) {
                firstSheep = i;
                break;
            } else if (firstPlant == -1 && gridObject instanceof Plant) {
                firstPlant = i;
            }
        }

        // Check for a sheep, then a plant, otherwise go with an empty space.
        if (firstSheep >= 0) {
            return firstSheep;
        } else if (firstPlant >= 0) {
            return firstPlant;
        } else {
            return super.findNullSpace(options);
        }
    }
}
