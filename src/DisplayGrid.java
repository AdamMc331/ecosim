/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;


class DisplayGrid {

    private JFrame frame;

    //TODO: Intellij should be telling you that maxX and maxY should be local variables. It's a good idea to do that
    // because it prevents the same field from being modified in a different thread. Don't ignore these warnings.
    // Also, use camelCase for gridToScreenRatio instead. This is a better Java convention.
    private int maxX,maxY, GridToScreenRatio;

    private GridObject[][] world;

    DisplayGrid(GridObject[][] w) {
        this.world = w;

        maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        maxY = Toolkit.getDefaultToolkit().getScreenSize().height;

        //TODO: Add some white space in between your plus symbols. Even though it's not required, it is a huge help
        // for readability. As it is, this gives me a head ache.
        GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map
        System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);

        this.frame = new JFrame("Map of World");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }

    public void refresh() {
        frame.repaint();
    }

    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {

            //Import images
            Image sheep = Toolkit.getDefaultToolkit().getImage("res/sheep.png");
            Image wolf = Toolkit.getDefaultToolkit().getImage("res/wolf.png");
            Image plant = Toolkit.getDefaultToolkit().getImage("res/plant.png");
            Image grass = Toolkit.getDefaultToolkit().getImage("res/grass.png");
            Image maleIcon = Toolkit.getDefaultToolkit().getImage("res/male.png");
            Image femaleIcon = Toolkit.getDefaultToolkit().getImage("res/female.png");

            setDoubleBuffered(true);
            g.setColor(Color.BLACK);

            //TODO: Make sure your brackets are consistent. This one was on the previous line.
            //TODO: Same comment from above about white space.
            //TODO: You're not wrong, but convention is usually i++ and j++;
            for(int i = 0; i<world[0].length;i=i+1) {
                for(int j = 0; j<world.length;j=j+1) {

                    //Grass background tile
                    g.drawImage(grass, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);

                    //TODO: You had a lot of repeated code in your example below. Sometimes this is okay, but in this example
                    // it would probably help a lot to define some variables you can reuse. I've commented your original code out
                    // for comparison, and replaced it with my suggestions.
//                    if (world[i][j] instanceof Sheep) {
//                        g.drawImage(sheep, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
//                        if (world[i][j].getGender()) {
//                            g.drawImage(maleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
//                        } else {
//                            g.drawImage(femaleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
//                        }
//                    } else if (world[i][j] instanceof Wolf) {
//                        g.drawImage(wolf, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
//                        if (world[i][j].getGender()) {
//                            g.drawImage(maleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
//                        } else {
//                            g.drawImage(femaleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
//                        }
//                    } else if (world[i][j] instanceof Plant) {
//                        g.drawImage(plant, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
//                    }

                    // Values are pulled from docs here - https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#drawImage(java.awt.Image,%20int,%20int,%20int,%20int,%20java.awt.image.ImageObserver)
                    // Width and height are repeated, going against my original comment, but I would say that it is okay
                    // this way because it increases readabilty.
                    GridObject gridObject = world[i][j];
                    int xValue = j * GridToScreenRatio;
                    int yValue = i * GridToScreenRatio;
                    int width = GridToScreenRatio;
                    int height = GridToScreenRatio;
                    int smallWidth = width / 5;
                    int smallHeight = height / 5;

                    // Note: In the above example, drawImage is called at least once in each if block,
                    // and the only thing that ever changes is the icon. We also draw a gender icon if necessary.
                    // Let's figure that stuff out first, and then call our drawImage method.
                    Image baseImage = null;
                    Image genderImage = null;

                    if (gridObject instanceof Sheep) {
                        baseImage = sheep;
                        genderImage = gridObject.getGender() ? maleIcon : femaleIcon;
                    } else if (gridObject instanceof Wolf) {
                        baseImage = wolf;
                        genderImage = gridObject.getGender() ? maleIcon : femaleIcon;
                    } else if (gridObject instanceof Plant) {
                        baseImage = plant;
                    }

                    // Draw the images if they're not null.
                    if (baseImage != null) {
                        g.drawImage(baseImage, xValue, yValue, width, height, this);
                    }

                    if (genderImage!= null) {
                        g.drawImage(genderImage, xValue, yValue, smallWidth, smallHeight, this);
                    }
                }
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid
