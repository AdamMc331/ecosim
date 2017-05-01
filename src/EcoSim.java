/**
 * EcoSim
 * Main project file for ECOsim
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;

public class EcoSim {

    //Important constants
    //TODO: As mentioned in DisplayGrid.java, a number of these fields are only used once, so they can probably be defined as a local variable
    // instead of at the class level. This is less important here than in the other file, since these are static, though.
    // one way around the intellij warnings is to label them `private static final`. By adding in the final keyword,
    // you're making it so that they can't be changed, so you don't have to worry about the race condition I mentioned
    // in my DisplayGrid comments.
    private static final int GRID_SIZE = 15;
    private static final int ITERATIONS = 1000;
    private static final double GROWTH_RATE = 5.4; //Chance a new plant will spawn that turn (Can be greater than 1)
    //Example: 20.4 guarantees 20 plants per turn, plus 40% chance of a 21st plant
    private static final int TICK_LENGTH = 200; //Milliseconds per grid refresh

    //World densities. Not guaranteed but they'll be close.
    private static final double WORLD_FILL_DENSITY = 0.5;
    //WOLF_DENSITY + SHEEP_DENSITY + PLANT_DENSITY = 1
    private static final double WOLF_DENSITY = 0.05;
    private static final double SHEEP_DENSITY = 0.9;
    //We don't need to declare PLANT_DENSITY because we get it algebraically

    //Mating values
    public static int MIN_MATE_HEALTH = 50;
    public static int BABY_HEALTH = 40;

    private static final double STRUGGLE_CHANCE = 0.3; //Chance a wolf will be unable to attack a weaker sheep

    //TODO: Keeping a static index of your current position is a bad idea. So many race conditions.
    // Instead, I've added it as a parameter to the methods that need it.
    // private static int currentIter;

    public static void main(String[] args) throws Exception {
        GridObject[][] map = new GridObject[GRID_SIZE][GRID_SIZE];

        //Set up Grid Panel
        DisplayGrid grid = new DisplayGrid(map);

        //Populate the grid
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                //Check if the tile even needs to be filled in the first place
                if (Math.random() <= WORLD_FILL_DENSITY) {
                    //Choose the GridObject we're going to spawn
                    double randomType = Math.random();
                    //Spawn a GridObject
//                    if (randomType <= WOLF_DENSITY) {
//                        map[y][x] = new Wolf();
//                    } else if (randomType <= WOLF_DENSITY + SHEEP_DENSITY) {
//                        map[y][x] = new Sheep();
//                    } else {
//                        map[y][x] = new Plant();
//                    }

                    //TODO: What you have is fine, but I would typically go with the same route I did in GridObject
                    // where I get the object I want, and then do the assignment. This is just personal preference
                    GridObject gridObject = null;
                    if (randomType <= WOLF_DENSITY) {
                        gridObject = new Wolf();
                    } else if (randomType <= WOLF_DENSITY + SHEEP_DENSITY) {
                        gridObject = new Sheep();
                    } else {
                        gridObject = new Plant();
                    }

                    map[y][x] = gridObject;
                }
            }
        }

        //Initial grid draw
        grid.refresh();


        //Run the specified number of iterations
        for(int i = 0; i < ITERATIONS; i++) {

            double plantSpawn = GROWTH_RATE;
            do {
                //Figure out if we want to spawn a plant this turn
                //TODO: You have two if statements here, combine them into one for simplicity
                if (Math.random() <= plantSpawn && EcoSim.hasSpaceLeft(map)) {
                    GridObject newPlantSpot;
                    int newY;
                    int newX;
                    do { //Statistically, this should never be an infinite loop
                        newY = (int) (Math.random() * GRID_SIZE);
                        newX = (int) (Math.random() * GRID_SIZE);
                        newPlantSpot = map[newY][newX];
                    } while (newPlantSpot != null);

                    //Let it grow
                    map[newY][newX] = new Plant();
                }
                plantSpawn -= 1;
            } while(plantSpawn > 1);

            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    EcoSim.updateObject(x, y, map, i);
                }
            }
            grid.refresh();

            //Make it pause so the user sees it
            try{ Thread.sleep(TICK_LENGTH); }catch(Exception e) { System.out.println("Couldn't sleep for some reason."); }
        }
    }

    private static void updateObject(int x, int y, GridObject[][] map, int currentPosition) {
        GridObject object = map[y][x];
        //Make sure the object is an animal we haven't moved yet
        if (object != null && !(object instanceof Plant) && object.getLastUpdated() < currentPosition) {

            //Create the ArrayList instances to be passed to the object
            //TODO: Notice what I said in GridObject about instantiating these as 0. In this case, I think the
            // largest size might be 3? Not sure. You /can/ leave it at 0 and it's not bad at all, just commenting
            // on performance for academic sake.
            ArrayList<GridObject> options = new ArrayList<>(0);
            ArrayList<int[]> optionCoords = new ArrayList<>(0);

            //Note: Animals can move diagonally
            for (int i = -1; i <= 1; i += 1) {
                if (y + i >= 0 && y + i < GRID_SIZE) { //If the outer loop is out of bounds we don't need an inner
                    for (int j = -1; j <= 1; j += 1) {
                        if (x + j >= 0 && x + j < GRID_SIZE) { //Make sure it's not out of bounds
                            if (!(j == 0 && i == 0)) { //Make sure non-movement isn't on the list
                                options.add(map[y+i][x+j]);
                                optionCoords.add(new int[]{y + i, x + j});
                            }
                        }
                    }
                }
            }

            //Get the animal to find a target it likes (food, mate, etc...)
            int arrayIndex = object.findTarget(options);

            int[] coords;

            if (arrayIndex > -1) { //-1 is returned if the object doesn't move
                coords = optionCoords.get(arrayIndex);

                int newY = coords[0];
                int newX = coords[1];

                //Make sure the animal is actually moving
                if ((newY != y || newX != x) && (newY < GRID_SIZE && newX < GRID_SIZE && newY >= 0 && newX >= 0)) {
                    GridObject targetSpot = map[newY][newX];

                    if (targetSpot != null) {
                        if (object instanceof Sheep) {
                            if (targetSpot instanceof Plant) {
                                //Yum!
                                object.addHealth(targetSpot.getHealth());
                                //Move, overwriting the plant
                                map[newY][newX] = map[y][x];
                                map[y][x] = null;
                            } else if (
                                    targetSpot instanceof Sheep &&
                                    targetSpot.getGender() != object.getGender() && //Opposite genders
                                    targetSpot.getHealth() >= MIN_MATE_HEALTH &&
                                    object.getHealth() >= MIN_MATE_HEALTH //Enough health
                                    ) {
                                //Spawn a sheep
                                GridObject newSheepSpot;
                                int newSheepY;
                                int newSheepX;

                                if (EcoSim.hasSpaceLeft(map)) {
                                    do {
                                        newSheepY = (int) (Math.random() * GRID_SIZE);
                                        newSheepX = (int) (Math.random() * GRID_SIZE);
                                        newSheepSpot = map[newSheepY][newSheepX];
                                    } while (newSheepSpot != null);
                                    map[newSheepY][newSheepX] = new Sheep(BABY_HEALTH);
                                } else { //We can overwrite plants in a pinch
                                    do {
                                        newSheepY = (int) (Math.random() * GRID_SIZE);
                                        newSheepX = (int) (Math.random() * GRID_SIZE);
                                        newSheepSpot = map[newSheepY][newSheepX];
                                    } while (!(newSheepSpot instanceof  Plant));
                                    //The sheep is slightly more damaged b/c it'll have to eat its way out anyways
                                    //Landing on plants must hurt
                                    map[newSheepY][newSheepX] = new Sheep(BABY_HEALTH - 10);
                                }

                                //Damage the parents
                                targetSpot.takeDamage(BABY_HEALTH/2);
                                object.takeDamage(BABY_HEALTH/2);
                            }
                        } else if (object instanceof Wolf) {
                            if (targetSpot instanceof Sheep) {
                                if (object.compareTo(targetSpot) > 0 || Math.random() <= STRUGGLE_CHANCE) {
                                    //Yum!
                                    object.addHealth(targetSpot.getHealth() / 2);
                                    //Move, overwriting the sheep
                                    map[newY][newX] = map[y][x];
                                    map[y][x] = null;
                                }
                                //If previous if statement is false, the wolf was too weak! No sheep for him!
                                //The sheep used to kill the wolf but apparently that "never happens in the world"
                            } else if (targetSpot instanceof Wolf && object.getGender() && targetSpot.getGender()) {
                                //Male wolves fight
                                if (object.compareTo(targetSpot) > 0) {
                                    targetSpot.takeDamage(10);
                                } else if (object.compareTo(targetSpot) < 0) {
                                    object.takeDamage(10);
                                } else {
                                    object.takeDamage(5);
                                    targetSpot.takeDamage(5);
                                }
                            } else if (
                                    targetSpot instanceof Wolf &&
                                    targetSpot.getGender() != object.getGender() && //Opposite genders
                                    targetSpot.getHealth() >= MIN_MATE_HEALTH &&
                                    object.getHealth() >= MIN_MATE_HEALTH //Enough health
                                    ) {
                                //Spawn a wolf
                                GridObject newWolfSpot;
                                int newWolfY;
                                int newWolfX;
                                if (EcoSim.hasSpaceLeft(map)) {
                                    do { //Statistically, this should never be an infinite loop
                                        newWolfY = (int) (Math.random() * GRID_SIZE);
                                        newWolfX = (int) (Math.random() * GRID_SIZE);
                                        newWolfSpot = map[newWolfY][newWolfX];
                                    } while (newWolfSpot != null);
                                } else {
                                    do { //Statistically, this should never be an infinite loop
                                        newWolfY = (int) (Math.random() * GRID_SIZE);
                                        newWolfX = (int) (Math.random() * GRID_SIZE);
                                        newWolfSpot = map[newWolfY][newWolfX];
                                    } while (!(newWolfSpot instanceof Plant));
                                }

                                //Damage the parents
                                //Wolves take more damage than sheep because they needed to be nerfed
                                targetSpot.takeDamage(BABY_HEALTH);
                                object.takeDamage(BABY_HEALTH);
                                map[newWolfY][newWolfX] = new Wolf(BABY_HEALTH);
                            }
                        }
                    } else {
                        //just move
                        map[newY][newX] = map[y][x];
                        map[y][x] = null;
                    }
                }
            }

            //Time-based damage (end of round)
            object.takeDamage(1);
            if (object.getHealth() <= 0) {
                map[y][x] = null;
            }
        }

        //Flag the object as updated
        if (object != null) { object.setLastUpdated(currentPosition); }
    }

    private static boolean hasSpaceLeft(GridObject[][] map) {
        for (GridObject[] a : map) {
            for (GridObject o : a) {
                if (o == null) {
                    return true;
                }
            }
        }
        return false;
    }

}


//TODO: Remove this comment