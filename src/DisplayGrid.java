/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports

import javax.swing.*;
import java.awt.*;

class DisplayGrid {

    private JFrame frame;
    private int GridToScreenRatio;
    private GridObject[][] world;

    DisplayGrid(GridObject[][] w) {
        this.world = w;

        int maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
        GridToScreenRatio = maxY / (world.length + 1);  //ratio to fit in screen as square map

        System.out.println("Map size: " + world.length + " by " + world[0].length + "\nScreen size: " + maxX + "x" + maxY + " Ratio: " + GridToScreenRatio);
        this.frame = new JFrame("Map of World");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }

    void refresh() {
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

            //NOTE: instead of i and j, use column and row.
            for (int col = 0; col < world[0].length; col++) {
                for (int row = 0; row < world.length; row++) {

                    //Grass background tile
                    g.drawImage(grass, row * GridToScreenRatio, col * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);

                    // Values are pulled from docs here - https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#drawImage(java.awt.Image,%20int,%20int,%20int,%20int,%20java.awt.image.ImageObserver)
                    // Width and height are repeated, going against my original comment, but I would say that it is okay
                    // this way because it increases readability.
                    GridObject gridObject = world[col][row];
                    int xValue = row * GridToScreenRatio;
                    int yValue = col * GridToScreenRatio;
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

                    if (genderImage != null) {
                        g.drawImage(genderImage, xValue, yValue, smallWidth, smallHeight, this);
                    }
                }
            }
        }
    }
}
