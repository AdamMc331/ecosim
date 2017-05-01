import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * GridObject
 * Generic grid object for ECO sim
 * Misha Larionov
 * 2017-04-24
 */
abstract class GridObject implements Comparable<GridObject> {

    private int health;
    private boolean gender;
    private int lastUpdated = -1;

    GridObject() {
        this.gender = new Random().nextBoolean();
        this.health = 0;
    }

    void addHealth(int amount) {
        this.health += amount;
    }

    int getHealth() {
        return this.health;
    }

    int getLastUpdated() {
        return this.lastUpdated;
    }

    void setLastUpdated(int update) {
        this.lastUpdated = update;
    }

    boolean getGender() {
        return this.gender;
    }

    void takeDamage(int amount) {
        this.health -= amount;
    }

    public int compareTo(GridObject object) {
        return (this.health - object.getHealth());
    }

    int findNullSpace(ArrayList<GridObject> options) {
        if (options.contains(null)) {
            ArrayList<Integer> nulls = new ArrayList<>(options.size());
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i) == null) {
                    nulls.add(i);
                }
            }
            Collections.shuffle(nulls);
            return nulls.get(0);
        } else {
            //Give up and don't move
            return -1;
        }
    }

    abstract int findTarget(ArrayList<GridObject> options);
}
