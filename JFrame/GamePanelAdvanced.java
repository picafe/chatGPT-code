// "more advanced" game generated, I just changed the double values of the scores as they were too insane
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanelAdvanced extends JPanel implements KeyListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private static final int DELAY = 10; // delay between updates, in milliseconds

    private static final int PLAYER_WIDTH = 50;
    private static final int PLAYER_HEIGHT = 50;
    private static final int ENEMY_WIDTH = 30;
    private static final int ENEMY_HEIGHT = 30;
    private static final int BOSS_WIDTH = 100;
    private static final int BOSS_HEIGHT = 100;
    private static final int NUM_ENEMIES = 2;

    private int playerX = WIDTH / 2; // x-coordinate of player
    private int playerY = HEIGHT / 2; // y-coordinate of player
    private int playerDX = 0; // change in player's x-coordinate per update
    private int playerDY = 0; // change in player's y-coordinate per update

    private List<Enemy> enemies; // list of enemies
    private Boss boss; // boss enemy
    private List<PowerUp> powerUps; // list of power-ups

    private double score = 0; // current score
    private int level = 1; // current level

    public GamePanelAdvanced() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // create enemies
        enemies = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < NUM_ENEMIES; i++) {
            int x = rand.nextInt(WIDTH - ENEMY_WIDTH);
            int y = rand.nextInt(HEIGHT - ENEMY_HEIGHT);
            enemies.add(new Enemy(x, y));
        }

        // create boss
        boss = new Boss(WIDTH / 2, 0);

        // create power-ups
        powerUps = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int x = rand.nextInt(WIDTH - PowerUp.WIDTH);
            int y = rand.nextInt(HEIGHT - PowerUp.HEIGHT);
            powerUps.add(new PowerUp(x, y));
        }

        // set up timer to trigger updates at regular intervals
        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void update() {
        playerX += playerDX;
        playerY += playerDY;

        // update enemies
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // update boss
        boss.update();

        // check for collisions
        for (Enemy enemy : enemies) {
            if (playerX < enemy.getX() + ENEMY_WIDTH && playerX + PLAYER_WIDTH > enemy.getX()
                    && playerY < enemy.getY() + ENEMY_HEIGHT && playerY + PLAYER_HEIGHT > enemy.getY()) {
                // player has collided with enemy, decrease score and reset position
                score -= 0.25;
                playerX = WIDTH / 2;
                playerY = HEIGHT / 2;
            }
        }

        if (playerX < boss.getX() + BOSS_WIDTH && playerX + PLAYER_WIDTH > boss.getX()
                && playerY < boss.getY() + BOSS_HEIGHT && playerY + PLAYER_HEIGHT > boss.getY()) {
            // player has collided with boss, decrease score and reset position
            score -= 0.125;
            playerX = WIDTH / 2;
            playerY = HEIGHT / 2;
        }


        // check for power-up pickups
        List<PowerUp> pickedUp = new ArrayList<>();
        for (PowerUp powerUp : powerUps) {
            if (playerX < powerUp.getX() + PowerUp.WIDTH && playerX + PLAYER_WIDTH > powerUp.getX()
                    && playerY < powerUp.getY() + PowerUp.HEIGHT && playerY + PLAYER_HEIGHT > powerUp.getY()) {
                // player has picked up power-up, increase score and add to pickedUp list
                score += powerUp.getValue();
                pickedUp.add(powerUp);
            }
        }
        powerUps.removeAll(pickedUp);

        // check for level completion
        if (enemies.isEmpty() && boss.isDefeated()) {
            // all enemies have been defeated, move to next level
            level++;
            playerX = WIDTH / 2;
            playerY = HEIGHT / 2;

            // create new enemies
            enemies = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < NUM_ENEMIES; i++) {
                int x = rand.nextInt(WIDTH - ENEMY_WIDTH);
                int y = rand.nextInt(HEIGHT - ENEMY_HEIGHT);
                enemies.add(new Enemy(x, y));
            }

            // create new boss
            boss = new Boss(WIDTH / 2, 0);

            // create new power-ups
            powerUps = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int x = rand.nextInt(WIDTH - PowerUp.WIDTH);
                int y = rand.nextInt(HEIGHT - PowerUp.HEIGHT);
                powerUps.add(new PowerUp(x, y));
            }
        }

        // repaint the panel to draw the updated game state
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

        g.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            g.fillRect(enemy.getX(), enemy.getY(), ENEMY_WIDTH, ENEMY_HEIGHT);
        }

        g.setColor(Color.GREEN);
        g.fillRect(boss.getX(), boss.getY(), BOSS_WIDTH, BOSS_HEIGHT);

        g.setColor(Color.YELLOW);
        for (PowerUp powerUp : powerUps) {
            g.fillRect(powerUp.getX(), powerUp.getY(), PowerUp.WIDTH, PowerUp.HEIGHT);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Level: " + level, 10, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            playerDX = -2;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            playerDX = 2;
        } else if (keyCode == KeyEvent.VK_UP) {
            playerDY = -2;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            playerDY = 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            playerDX = 0;
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            playerDY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private class Enemy {
        private int x;
        private int y;
        private int dx;
        private int dy;

        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
            dx = 2;
            dy = 2;
        }

        public void update() {
            x += dx;
            y += dy;

            if (x < 0 || x > WIDTH - ENEMY_WIDTH) {
                dx *= -1;
            }
            if (y < 0 || y > HEIGHT - ENEMY_HEIGHT) {
                dy *= -1;
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private class Boss {
        private int x;
        private int y;
        private int dx;
        private int dy;
        private int health;

        public Boss(int x, int y) {
            this.x = x;
            this.y = y;
            dx = 0;
            dy = 2;
            health = 100;
        }

        public void update() {
            x += dx;
            y += dy;

            if (y > HEIGHT - BOSS_HEIGHT) {
                dy = 0;
                dx = 2;
            }
            if (x > WIDTH - BOSS_WIDTH) {
                dx = 0;
                dy = -2;
            }
            if (y < 0) {
                dy = 0;
                dx = -2;
            }
            if (x < 0) {
                dx = 0;
                dy = 2;
            }
        }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public boolean isDefeated() {
                return health <= 0;
            }

            public void damage(int amount) {
                health -= amount;
            }
        }

        private class PowerUp {
            public static final int WIDTH = 20;
            public static final int HEIGHT = 20;

            private int x;
            private int y;
            private int value;

            public PowerUp(int x, int y) {
                this.x = x;
                this.y = y;
                value = 20;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int getValue() {
                return value;
            }
        }

        public static void main(String[] args) {
            JFrame frame = new JFrame("Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GamePanelAdvanced());
            frame.pack();
            frame.setVisible(true);
        }
    }




