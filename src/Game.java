import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import static java.lang.System.exit;

class GameTetris extends JFrame {

    private final String TITLE_OF_PROGRAM = "Tetris";
    private final int BLOCK_SIZE = 25;      // size of one block
    private final int ARC_RADIUS = 6;
    private final int FIELD_WIDTH = 11;     // size of game's field in blocks
    private final int FIELD_HEIGHT = 18;
    private final int START_LOCATION = 180;
    private final int FIELD_DX = 7;         // determined experimentally
    private final int FIELD_DY = 53;
    // key codes
    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int DOWN = 40;

    private int SHOW_DELAY = 400;     // animation delay
    private final int[][][] SHAPES = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {4, 0x00f0f0}}, // I
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {4, 0xf0f000}}, // O
            {{1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x0000f0}}, // J
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf0a000}}, // L
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x00f000}}, // S
            {{1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xa000f0}}, // T
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf00000}}  // Z
    };
    private final int[] SCORES = {100, 300, 700, 1500};
    private int gameScore = 0;
    private int[][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH]; // mine/glass
    private Canvas canvas = new Canvas();
    private Random random = new Random();
    private Figure figure = new Figure();
    private boolean gameOver = false;
    private final int[][] GAME_OVER_MSG = {
            {0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0},
            {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0}};
    private Color backGroundColor;

    private JMenuBar jMenuBar = new JMenuBar();
    private JMenu jMenu = new JMenu("Menu");
    private JMenu jColorMenu = new JMenu("Background color");
    //a group of JMenuItems
    private JMenuItem restartItem = new JMenuItem("Restart", KeyEvent.VK_T);
    private JMenuItem showRulesItem = new JMenuItem("Game's rules", KeyEvent.VK_T);
    private JMenuItem exitItem = new JMenuItem("Exit");
    //background colors to choose from
    private JMenuItem blackItem = new JMenuItem("Black");
    private JMenuItem pinkItem = new JMenuItem("Pink");
    private JMenuItem blueItem = new JMenuItem("Blue");
    private JMenuItem cyanItem = new JMenuItem("Cyan");
    private JMenuItem greenItem = new JMenuItem("Green");
    private JMenuItem darkGrayItem = new JMenuItem("Dark Gray");
    private JMenuItem lightGrayItem = new JMenuItem("Light Gray");
    private JMenuItem magentaItem = new JMenuItem("Magenta");
    private JMenuItem orangeItem = new JMenuItem("Orange");
    private JMenuItem grayItem = new JMenuItem("Gray");
    private JMenuItem redItem = new JMenuItem("Red");
    private JMenuItem whiteItem = new JMenuItem("White");
    private JMenuItem yellowItem = new JMenuItem("Yellow");

    public static void main(String[] args) {
        while (true) {
            new GameTetris().go();
        }
    }

    private GameTetris() {
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, FIELD_WIDTH * BLOCK_SIZE + FIELD_DX, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY);
        setResizable(false);

        backGroundColor = Color.BLACK;
        canvas.setBackground(backGroundColor); // define the background color

        canvas.add(jMenuBar);
        canvas.add(jMenu);
        canvas.add(jColorMenu);
        canvas.add(restartItem);
        canvas.add(showRulesItem);
        canvas.add(blackItem);
        canvas.add(pinkItem);
        canvas.add(blueItem);
        canvas.add(cyanItem);
        canvas.add(greenItem);
        canvas.add(darkGrayItem);
        canvas.add(lightGrayItem);
        canvas.add(magentaItem);
        canvas.add(orangeItem);
        canvas.add(grayItem);
        canvas.add(redItem);
        canvas.add(whiteItem);
        canvas.add(yellowItem);
        canvas.add(exitItem);

        setJMenuBar(jMenuBar);

        jMenuBar.add(jMenu);

        jMenu.add(jColorMenu);
        jMenu.add(restartItem);
        jMenu.add(showRulesItem);
        jMenu.add(jColorMenu);
        jMenu.add(exitItem);

        jColorMenu.add(blackItem);
        jColorMenu.add(pinkItem);
        jColorMenu.add(blueItem);
        jColorMenu.add(cyanItem);
        jColorMenu.add(greenItem);
        jColorMenu.add(darkGrayItem);
        jColorMenu.add(lightGrayItem);
        jColorMenu.add(magentaItem);
        jColorMenu.add(orangeItem);
        jColorMenu.add(grayItem);
        jColorMenu.add(redItem);
        jColorMenu.add(whiteItem);
        jColorMenu.add(yellowItem);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == DOWN)
                        figure.drop();
                    if (e.getKeyCode() == UP)
                        figure.rotate();
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT)
                        figure.move(e.getKeyCode());
                } else {
                    canvas.repaint();
                }
            }
        });

        restartItem.addActionListener(e -> {
            gameOver = true;
            dispose();
        });

        exitItem.addActionListener(e -> {
            setVisible(false);
            dispose();
            exit(0);
        });

        blackItem.addActionListener(e -> {
            backGroundColor = Color.BLACK;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        pinkItem.addActionListener(e -> {
            backGroundColor = Color.PINK;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        blueItem.addActionListener(e -> {
            backGroundColor = Color.BLUE;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        cyanItem.addActionListener(e -> {
            backGroundColor = Color.CYAN;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        greenItem.addActionListener(e -> {
            backGroundColor = Color.GREEN;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        darkGrayItem.addActionListener(e -> {
            backGroundColor = Color.DARK_GRAY;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        lightGrayItem.addActionListener(e -> {
            backGroundColor = Color.LIGHT_GRAY;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        magentaItem.addActionListener(e -> {
            backGroundColor = Color.MAGENTA;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        orangeItem.addActionListener(e -> {
            backGroundColor = Color.ORANGE;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        grayItem.addActionListener(e -> {
            backGroundColor = Color.GRAY;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        redItem.addActionListener(e -> {
            backGroundColor = Color.RED;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        whiteItem.addActionListener(e -> {
            backGroundColor = Color.WHITE;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        yellowItem.addActionListener(e -> {
            backGroundColor = Color.YELLOW;
            canvas.setBackground(backGroundColor);
            canvas.repaint();
        });

        showRulesItem.addActionListener(e -> {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "Rules of the game:\n" +
                    "\n" +
                    "The player has a rectangular field with a width of 10 and a height of 25 cells.\n From above to down figures fall. Each figure can be rotated \\nthrough 90° (\"Up\" key) and also move horizontally (\"Right\" or \"Left\" key).\n" +
                    "You can also \"dump\" the figure (ie, accelerate its fall), when the \nplayer has already decided where the figure should fall (\"Down\" key). The figure \nflies until it rests another figure or it will not hit the bottom of the glass.\nIf at the same time a horizontal row of 10 cells is filled, then this series disappears, \\nand the player receives a certain number of points. All the rows above the missing \nare dropped down one square. The game ends when a new figure can not fit in \na play field.\n" +
                    "Thus, the player's task is to fill the ranks without filling the play \nfield (vertically) as long as possible to get more points.\n" +
                    "The game has no time limits, it all depends only on the vertical filling \nof the field and the availability of space for new figures.\n\n" +
                    "Scoring:\n" +
                    "1 line - 100 points,\n" +
                    "2 lines - 300 points,\n" +
                    "3 lines - 700 points,\n" +
                    "4 lines (that is, make Tetris) - 1500 points.\n");
        });

        //set some keyboard shortcuts
        restartItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        showRulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        add(BorderLayout.CENTER, canvas);
        setVisible(true);
        Arrays.fill(mine[FIELD_HEIGHT], 1); // create a ground for mines
    }

    // main loop of game
    private void go() {
        while (!gameOver) {
            try {
                Thread.sleep(SHOW_DELAY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            canvas.repaint();
            checkFilling();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                figure = new Figure();
                gameOver = figure.isCrossGround(); // Is there space for a new figure?
            } else
                figure.stepDown();
        }
        //delay after gameOver
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dispose();
    }

    // check filling rows
    private void checkFilling() {
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++)
                filled *= Integer.signum(mine[row][col]);
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(mine[i - 1], 0, mine[i], 0, FIELD_WIDTH);
            } else
                row--;
        }
        if (countFillRows > 0) {
            gameScore += SCORES[countFillRows - 1];
            SHOW_DELAY -= 5;
            setTitle(TITLE_OF_PROGRAM + " : " + gameScore);
        }
    }

    class Figure {
        private ArrayList<Block> figure = new ArrayList<>();
        private int[][] shape = new int[4][4];
        private int type, size, color;
        private int x = 3, y = 0; // starting left up corner

        Figure() {
            type = random.nextInt(SHAPES.length);
            size = SHAPES[type][4][0];
            color = SHAPES[type][4][1];
            if (size == 4) y = -1;
            for (int i = 0; i < size; i++)
                System.arraycopy(SHAPES[type][i], 0, shape[i], 0, SHAPES[type][i].length);
            createFromShape();
        }

        void createFromShape() {
            for (int x = 0; x < size; x++)
                for (int y = 0; y < size; y++)
                    if (shape[y][x] == 1) figure.add(new Block(x + this.x, y + this.y));
        }

        boolean isTouchGround() {
            for (Block block : figure) if (mine[block.getY() + 1][block.getX()] > 0) return true;
            return false;
        }

        boolean isCrossGround() {
            for (Block block : figure) if (mine[block.getY()][block.getX()] > 0) return true;
            return false;
        }

        void leaveOnTheGround() {
            for (Block block : figure) mine[block.getY()][block.getX()] = color;
        }

        boolean isTouchWall(int direction) {
            for (Block block : figure) {
                if (direction == LEFT && (block.getX() == 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
                if (direction == RIGHT && (block.getX() == FIELD_WIDTH - 1 || mine[block.getY()][block.getX() + 1] > 0))
                    return true;
            }
            return false;
        }

        void move(int direction) {
            if (!isTouchWall(direction)) {
                int dx = direction - 38; // LEFT = -1, RIGHT = 1
                for (Block block : figure) block.setX(block.getX() + dx);
                x += dx;
            }
        }

        void stepDown() {
            for (Block block : figure) block.setY(block.getY() + 1);
            y++;
        }

        void drop() {
            while (!isTouchGround()) stepDown();
        }

        boolean isWrongPosition() {
            for (int x = 0; x < size; x++)
                for (int y = 0; y < size; y++)
                    if (shape[y][x] == 1) {
                        if (y + this.y < 0) return true;
                        if (x + this.x < 0 || x + this.x > FIELD_WIDTH - 1) return true;
                        if (mine[y + this.y][x + this.x] > 0) return true;
                    }
            return false;
        }

        void rotateShape(int direction) {
            for (int i = 0; i < size / 2; i++)
                for (int j = i; j < size - 1 - i; j++)
                    if (direction == RIGHT) {
                        // clockwise
                        int tmp = shape[size - 1 - j][i];
                        shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                        shape[size - 1 - i][size - 1 - j] = shape[j][size - 1 - i];
                        shape[j][size - 1 - i] = shape[i][j];
                        shape[i][j] = tmp;
                    } else {
                        // counterclockwise
                        int tmp = shape[i][j];
                        shape[i][j] = shape[j][size - 1 - i];
                        shape[j][size - 1 - i] = shape[size - 1 - i][size - 1 - j];
                        shape[size - 1 - i][size - 1 - j] = shape[size - 1 - j][i];
                        shape[size - 1 - j][i] = tmp;
                    }
        }

        void rotate() {
            rotateShape(RIGHT);
            if (!isWrongPosition()) {
                figure.clear();
                createFromShape();
            } else
                rotateShape(LEFT);
        }

        void paint(Graphics g) {
            for (Block block : figure) block.paint(g, color);
        }
    }

    // building element for Figure
    class Block {
        private int x, y;

        Block(int x, int y) {
            setX(x);
            setY(y);
        }

        void setX(int x) {
            this.x = x;
        }

        void setY(int y) {
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        void paint(Graphics g, int color) {
            g.setColor(new Color(color));
            g.drawRoundRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2, ARC_RADIUS, ARC_RADIUS);
        }
    }

    // my canvas for painting
    class Canvas extends JPanel {

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < FIELD_WIDTH; x++)
                for (int y = 0; y < FIELD_HEIGHT; y++) {
                    if (x < FIELD_WIDTH - 1 && y < FIELD_HEIGHT - 1) {
                        g.setColor(Color.lightGray);
                        g.drawLine((x + 1) * BLOCK_SIZE - 2, (y + 1) * BLOCK_SIZE, (x + 1) * BLOCK_SIZE + 2, (y + 1) * BLOCK_SIZE);
                        g.drawLine((x + 1) * BLOCK_SIZE, (y + 1) * BLOCK_SIZE - 2, (x + 1) * BLOCK_SIZE, (y + 1) * BLOCK_SIZE + 2);
                    }
                    if (mine[y][x] > 0) {
                        g.setColor(new Color(mine[y][x]));
                        g.fill3DRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1, true);
                    }
                }
            if (gameOver) {
                g.setColor(Color.white);
                for (int y = 0; y < GAME_OVER_MSG.length; y++)
                    for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                        if (GAME_OVER_MSG[y][x] == 1) g.fill3DRect(x * 11 + 18, y * 11 + 160, 10, 10, true);
            } else
                figure.paint(g);
        }
    }
}