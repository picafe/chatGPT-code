import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpritePhysics extends JPanel implements KeyListener {
    private static final int SPRITE_WIDTH = 50;  // Sprite width in pixels
    private static final int SPRITE_HEIGHT = 50;  // Sprite height in pixels
    private static final int GRAVITY = 1;  // Gravity in pixels per frame
    private static final int JUMP_VELOCITY = -20;  // Jump velocity in pixels per frame
    private static final int MOVE_VELOCITY = 5;  // Move velocity in pixels per frame

    private int x;  // Sprite x-coordinate
    private int y;  // Sprite y-coordinate
    private int vy;  // Sprite y-velocity
    private int vx;  // Sprite x-velocity

    public SpritePhysics() {
        x = 0;
        y = 0;
        vy = 0;
        vx = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        g.fillRect(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                // Jump
                vy = JUMP_VELOCITY;
                break;
            case KeyEvent.VK_LEFT:
                // Move left
                vx = -MOVE_VELOCITY;
                break;
            case KeyEvent.VK_RIGHT:
                // Move right
                vx = MOVE_VELOCITY;
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                // Stop moving left
                if (vx < 0) {
                    vx = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                // Stop moving right
                if (vx > 0) {
                    vx = 0;
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    public void update() {
        // Update sprite position
        x += vx;
        y += vy;
        vy += GRAVITY;

        // Check for collision with the ground
        if (y + SPRITE_HEIGHT > getHeight()) {
            y = getHeight() - SPRITE_HEIGHT;
            vy = 0;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sprite Movement Example");
        SpritePhysics panel = new SpritePhysics();
        frame.add(panel);
        frame.setSize(1280, 720);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(panel);

        // Game loop
        while (true) {
            panel.update();
            panel.repaint();
            try {
                Thread.sleep(1000 / 60);  // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

