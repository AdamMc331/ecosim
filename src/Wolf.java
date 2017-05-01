/**
 * Wolf
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Wolf extends GridObject{

    Wolf() {
        super();
        //TODO: Use this SO answer for a better way to get a random number between two ints: http://stackoverflow.com/a/5887736/3131147
        int min = 50;
        int max = 100;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 50-100 (inclusive)
    }

    Wolf(int health) {
        super();
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {
        ArrayList<GridObject> newOptions = (ArrayList)options.clone();

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        //TODO: See my comments in Sheep.java. You can replace all of these with one for loop.
        int firstFemaleWolf = -1;
        int firstSheep = -1;
        int firstMaleWolf = -1;

        for(int i = 0; i < newOptions.size(); i++) {
            GridObject o = newOptions.get(i);

            // Since firstOppositeWolf is the main thing we want to find, break if we find it.
            if (firstFemaleWolf == -1
                    && o instanceof Wolf
                    && o.getGender() != super.getGender()
                    && o.getHealth() >= EcoSim.MIN_MATE_HEALTH
                    && super.getHealth() >= EcoSim.MIN_MATE_HEALTH) {
                firstFemaleWolf = i;
                break;
            } else if (firstSheep == -1 && o instanceof Sheep) {
                firstSheep = i;
            } else if (firstMaleWolf == -1 && o instanceof Wolf && o.getGender() && super.getGender()) {
                firstMaleWolf = i;
            }
        }

        if (firstFemaleWolf >= 0) {
            return firstFemaleWolf;
        } else if (firstSheep >= 0) {
            return firstSheep;
        } else if (firstMaleWolf >= 0) {
            return firstMaleWolf;
        } else {
            return super.findNullSpace(options);
        }
    }
}
