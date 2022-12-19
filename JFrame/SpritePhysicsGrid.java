import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class SpritePhysicsGrid extends JFrame implements KeyListener {

    // constants for the size of the grid
    private static final int ROWS = 20;
    private static final int COLS = 100;

    // 2D array to represent the grid
    private JPanel[][] grid;

    // variables to keep track of the sprite's position
    private int spriteRow;
    private int spriteCol;

    // variables to keep track of the sprite's velocity
    private double xVelocity;
    private double yVelocity;

    public SpritePhysicsGrid() {
        // set up the JFrame
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(ROWS, COLS));

        // initialize the grid and add it to the JFrame
        grid = new JPanel[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new JPanel();
                add(grid[i][j]);
            }
        }

        // initialize the sprite's position
        spriteRow = 0;
        spriteCol = 0;
        grid[spriteRow][spriteCol].setBackground(Color.RED);

        // add a key listener to the JFrame to detect key events
        addKeyListener(this);

        // set the JFrame to be focusable so it can receive key events
        setFocusable(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // when the left arrow key is pressed, set the sprite's horizontal velocity to -1
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xVelocity = -1;
        }
        // when the right arrow key is pressed, set the sprite's horizontal velocity to 1
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xVelocity = 1;
        }
        // when the space bar is pressed, start the sprite jumping

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            yVelocity = -1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // when the left or right arrow key is released, stop the sprite from moving horizontally
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xVelocity = 0;
        }
        // when the space bar is released, stop the sprite from jumping
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            yVelocity = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    public void update() {
        // store the sprite's previous position
        int prevRow = spriteRow;
        int prevCol = spriteCol;

        // update the sprite's position based on its velocity
        spriteRow += yVelocity;
        spriteCol += xVelocity;

        // clear the sprite's previous position
        grid[prevRow][prevCol].setBackground(Color.WHITE);

        // apply gravity to the sprite's vertical velocity
        yVelocity += 0.5;

        // clamp the sprite's position to the grid
        spriteRow = Math.min(Math.max(0, spriteRow), ROWS - 1);
        spriteCol = Math.min(Math.max(0, spriteCol), COLS - 1);

        // update the sprite's position on the grid
        grid[spriteRow][spriteCol].setBackground(Color.RED);
    }


    public static void main(String[] args) {
        // create the game and set it to be visible
        SpritePhysicsGrid game = new SpritePhysicsGrid();
        game.setVisible(true);

        // main game loop
        while (true) {
            game.update();

            // delay to slow down the game
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                // do nothing
            }
        }
    }
}

