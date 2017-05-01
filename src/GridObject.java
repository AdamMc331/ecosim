/**
 * GridObject
 * Generic grid object for ECO sim
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

abstract class GridObject implements Comparable<GridObject>{

    private int health;
    private boolean gender;
    private int lastUpdated = -1;

    GridObject() {
        Random random = new Random();
        this.gender =  random.nextBoolean();
        this.health = 0;
        //Health is later set individually in the subclasses
    }

    public void addHealth(int amount) {
        this.health += amount;
    }

    public int getHealth() { return this.health; }

    public int getLastUpdated() { return this.lastUpdated; }

    public void setLastUpdated(int update) { this.lastUpdated = update; }

    public boolean getGender() { return this.gender; }

    public void takeDamage(int amount) {
        this.health -= amount;
    }

    public int compareTo(GridObject object) {
        return (this.health - object.getHealth());
    }

    //This is here because it's the same for wolves and sheep
    public int findNullSpace(ArrayList<GridObject> options) {
        if (options.contains(null)) {
            //TODO: Super minor performance optomization, but something to keep in mind because this bit me in an interview once.
            // When you say new ArrayList<>(0). You're instantiating it with a size 0. So, once you add the first item,
            // the system has to go reallocate more memory. A trick you could use is that, since you know the size of
            // nulls can't be larger than the size of options, you can start with that value so you never have to
            // worry about the system reassigning more memory.
            ArrayList<Integer> nulls = new ArrayList<>(options.size());
            for (int i = 0; i < options.size(); i ++) {
                if (options.get(i) == null) {
                    nulls.add(i);
                }
            }
            Collections.shuffle(nulls);
            //TODO: I would normally say be wary of Null Pointer Exceptions here, but since you already checked that
            // options contains null, you're guaranteed that this will have at least one item.
            // So this is fine, but a note to be aware of in the future.
            return nulls.get(0);
        } else {
            //Give up and don't move
            return -1;
        }
    }

    abstract int findTarget(ArrayList<GridObject> options);
}
