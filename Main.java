
/**
* Culminating Task - ICS3U1
*	Author: Johnny Li  
* Date Created: January 8, 2024
* Date Last Modified: January 15, 2024
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel {

  static Random random = new Random();

  static int tileSize = 12; // Size of each tile on the screen
  static int screenWidth = 29;
  static int screenHeight = 39;
 
  // The way Pac-Man faces
  static int pacManArc = 45;

  // Pac-Man's default X and Y positions
  static int defaultPacManPosX = (tileSize * screenWidth) / 2 - tileSize;
  static int defaultPacManPosY = (tileSize * screenHeight) - (tileSize * 5);

  // Pac-Man's X and Y positions
  static int pacManPosX = defaultPacManPosX;
  static int pacManPosY = defaultPacManPosY;

  // Pac-Man's new X and Y positions
  static int newPacManPosX;
  static int newPacManPosY;

  // How much Pac-Man moves in the X and Y direction
  static int pacMoveX = 0;
  static int pacMoveY = 0;

  static int pacManSpeed = 2;

  static Rectangle pacManHitbox = new Rectangle(pacManPosX, pacManPosY, tileSize, tileSize); // Pac-Man's hitbox

  // The ghosts' default position
  static int defaultGhostPosX = (tileSize * screenWidth) / 2 - tileSize + 5;
  static int defaultGhostPosY = (tileSize * screenHeight) / 2 - (tileSize * 2);

  // Position of a single ghost
  static int ghostPosX;
  static int ghostPosY;

  // New position of a single ghost
  static int newGhostPosX;
  static int newGhostPosY;

  // Arrays storing the ghosts' information (positions, the direction they move
  // in, color, and hitboxes)
  static int[][] ghostsPos = {
      { defaultGhostPosX, defaultGhostPosY },
      { defaultGhostPosX, defaultGhostPosY },
      { defaultGhostPosX, defaultGhostPosY },
      { defaultGhostPosX, defaultGhostPosY }
  };

  static int[] ghostsDirection = { 0, 0, 0, 0 };

  static Color[] ghostsColor = { Color.RED, Color.CYAN, Color.PINK, Color.ORANGE };

  static Rectangle[] ghostsHitbox = {
      new Rectangle(defaultGhostPosX, defaultGhostPosY, tileSize - 5, tileSize - 5),
      new Rectangle(defaultGhostPosX, defaultGhostPosY, tileSize - 5, tileSize - 5),
      new Rectangle(defaultGhostPosX, defaultGhostPosY, tileSize - 5, tileSize - 5),
      new Rectangle(defaultGhostPosX, defaultGhostPosY, tileSize - 5, tileSize - 5),
  };

  // The ghosts' movement in the X and Y direction
  static int ghostMoveX = 0;
  static int ghostMoveY = 0;

  static int ghostDirection; // The direction that the ghosts will be moving in

  static int ghostSpeed = 1;

  static boolean ghostScared = false; // State of the ghost

  // Power pellet variables
  static boolean powerPelletActive = false;
  static int powerPelletTimer = 0;

  static int score;
  static int speedIncreasePoint = 200; // Used to keep track of when to increase ghost speed depending on the score

  static int lives;

  static int highscore = 0;

  static String message = "How to Play:\n- Avoid the ghosts\n- Collect white pellets to increase your score\n- Obtain power pellets to scare the ghosts \nand have a chance to catch them\nControls:\n- W (UP)\n- S (DOWN)\n- A (LEFT)\n- D (RIGHT)";

  // Needed components of the game
  static JFrame frame;
  static JPanel panel;
  static JButton playButton;
  static JButton howButton;
  static JButton quitButton;
  static JButton againButton;
  static JButton returnButton;
  static JLabel title;
  static JLabel userHighscore;
  static JLabel endMessage;
  static JLabel finalScore;
  static Timer timer;

  /**
   * Data for the maze
   * - 0 = Black background
   * - 1 = Blue walls
   * - 2 = Pellet
   * - 3 = Power pellet
   * - 4 = Teleportation pads
   * - 5 = Ghosts' spawn border
   */
  static int[][] mazeData = {
      { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
      { 1, 3, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 2, 1, 1, 2, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 3, 1 },
      { 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 },
      { 1, 2, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 2, 1 },
      { 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1 },
      { 1, 0, 1, 0, 0, 0, 1, 2, 0, 0, 2, 0, 0, 1, 1, 0, 0, 2, 0, 0, 2, 1, 0, 0, 0, 1, 0, 1 },
      { 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1 },
      { 1, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 2, 1, 1, 2, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 1 },
      { 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1 },
      { 1, 2, 1, 2, 1, 2, 0, 0, 1, 0, 1, 2, 0, 1, 1, 0, 2, 1, 0, 1, 0, 0, 2, 1, 2, 1, 2, 1 },
      { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 1, 0, 1, 0, 1, 0, 0, 2, 0, 0, 2, 1, 1, 2, 0, 0, 2, 0, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 2, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 2, 1 },
      { 1, 0, 1, 2, 1, 2, 1, 3, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 3, 1, 2, 1, 2, 1, 0, 1 },
      { 1, 0, 1, 0, 1, 0, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 2, 0, 0, 2, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 2, 0, 0, 2, 1 },
      { 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 5, 5, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1 },
      { 4, 0, 0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 1, 0, 0, 1, 0, 0, 2, 1, 0, 2, 0, 0, 0, 0, 0, 4 },
      { 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1 },
      { 1, 3, 0, 0, 0, 2, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 2, 0, 0, 0, 3, 1 },
      { 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1 },
      { 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1 },
      { 1, 2, 1, 2, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 2, 1, 2, 1 },
      { 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1 },
      { 1, 0, 1, 0, 2, 1, 0, 2, 1, 2, 0, 0, 2, 1, 1, 2, 0, 0, 2, 1, 2, 0, 1, 2, 0, 1, 0, 1 },
      { 1, 2, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 2, 1 },
      { 1, 0, 1, 1, 0, 1, 0, 0, 2, 0, 2, 0, 0, 1, 1, 0, 0, 2, 0, 2, 0, 0, 1, 0, 1, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
      { 1, 2, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 2, 1 },
      { 1, 0, 1, 2, 0, 1, 0, 1, 2, 1, 2, 0, 2, 1, 1, 2, 0, 2, 1, 2, 1, 0, 1, 0, 2, 1, 0, 1 },
      { 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
      { 1, 2, 0, 0, 2, 0, 0, 2, 0, 0, 2, 1, 0, 1, 1, 0, 1, 2, 0, 0, 2, 0, 0, 2, 0, 0, 2, 1 },
      { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
      { 1, 3, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 3, 1 },
      { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
  };

  static int[][] mazeDataCopy = new int[36][28];

  public static void main(String[] args) {

    // Copying array mazeData into mazeDataCopy
    for (int i = 0; i < mazeData.length; i++) {
      for (int j = 0; j < mazeData[i].length; j++) {
        mazeDataCopy[i][j] = mazeData[i][j];
      }
    }

    /**
     * Game Loop
     * The number 16 represents the delay in milliseconds between each tick
     * This creates 60 frames per second
     */
    timer = new Timer(16, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // If Pac-Man has no lives left, end the game
        if (lives < 0) {

          if (score > highscore) { // Changing the highscore
            highscore = score;
          }

          gameOver();
        } else {
          // Calling functions to update the screen
          movePacMan();

          for (int i = 0; i < 4; i++) {
            moveGhost(i);
          }

          checkEntityCollisions();
          checkScore();

          if (!checkPelletsLeft()) { // If there are no pellets left in the maze, reset it
            resetMaze();
          }

          frame.repaint(); // Calling the repaint() method automatically calls the paint() method
        }
      }
    });

    // Initializing the game window
    frame = new JFrame("Pac-Man");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(screenWidth * tileSize, screenHeight * tileSize);
    frame.setBackground(Color.BLACK);
    frame.setFocusable(true);
    frame.setVisible(true);

    // Adding a panel to place buttons and labels
    panel = new JPanel();
    panel.setBackground(Color.BLACK);
    panel.setSize(screenWidth * tileSize, screenHeight * tileSize);
    panel.setLayout(null);
    panel.setVisible(true);
    frame.add(panel);

    // Initializing the labels
    title = new JLabel();
    userHighscore = new JLabel();

    // Title of the game
    title.setText("PAC-MAN");
    title.setForeground(Color.YELLOW);
    title.setFont(new Font("TimesRoman", Font.BOLD, 20));
    title.setBounds(125, 30, 200, 20);
    panel.add(title);
       
    // The highscore
    userHighscore.setText("Highscore: " + Integer.toString(highscore));
    userHighscore.setForeground(Color.YELLOW);
    userHighscore.setFont(new Font("TimesRoman", Font.BOLD, 10));
    userHighscore.setBounds(140, 375, 100, 40);
    panel.add(userHighscore);

    // Initializing the buttons
    playButton = new JButton("Play");
    howButton = new JButton("How to Play");
    quitButton = new JButton("Quit");

    // "Play" button to start the game
    playButton.setBounds(125, 100, 100, 40);
    playButton.setFocusable(false);
    playButton.setVisible(true);
    playButton.addActionListener(new ActionListener() { // Implementing user input for the button
      public void actionPerformed(ActionEvent click) {
        if (click.getSource() == playButton) {
          resetGame();

          // Hiding the menu
          playButton.setVisible(false);
          howButton.setVisible(false);
          quitButton.setVisible(false);
          title.setVisible(false);
          userHighscore.setVisible(false);
          panel.setVisible(false);

          // Freeze the game for three seconds
          try {
            Thread.sleep(3000); // Using Thread.sleep() to produce a 3-second delay
          } catch (InterruptedException e) { // Handles InterruptedException
            e.printStackTrace();
          }

          timer.start(); // Starting the game loop
        }
      }
    });
    panel.add(playButton);

    // "How to Play" button for intructions on how to play the game
    howButton.setBounds(125, 200, 100, 40);
    howButton.setFocusable(false);
    howButton.setVisible(true);
    howButton.addActionListener(new ActionListener() { // Implementing user input for the button
      public void actionPerformed(ActionEvent click) {
        if (click.getSource() == howButton) { // Creating a new panel with intructions
          JOptionPane.showMessageDialog(null, message, "How to Play", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    panel.add(howButton);

    // "Quit Game" button to exit the game
    quitButton.setBounds(125, 300, 100, 40);
    quitButton.setFocusable(false);
    quitButton.setVisible(true);
    quitButton.addActionListener(new ActionListener() { // Implementing user input for the button
      public void actionPerformed(ActionEvent click) {
        if (click.getSource() == quitButton) {
          frame.dispose(); // Closes the frame
        }
      }
    });
    panel.add(quitButton);

    frame.addKeyListener(new KeyListener() { // Implementing key input for the game
      public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Player inputs WASD to control Pac-Man
        if (key == KeyEvent.VK_W) { // UP
          /**
           * When pacMoveY is changed, so does pacMoveX. Setting pacMoveX to 0 prevents
           * this. The same goes for when pacMoveX is changed; pacMoveY has to be set to 0
           * Also changes the direction that Pac-Man is facing
           */
          pacMoveY = pacManSpeed * -1;
          pacMoveX = 0;
          pacManArc = 135;
        } else if (key == KeyEvent.VK_S) { // DOWN
          pacMoveY = pacManSpeed;
          pacMoveX = 0;
          pacManArc = 310;
        } else if (key == KeyEvent.VK_A) { // LEFT
          pacMoveX = pacManSpeed * -1;
          pacMoveY = 0;
          pacManArc = 220;
        } else if (key == KeyEvent.VK_D) { // RIGHT
          pacMoveX = pacManSpeed;
          pacMoveY = 0;
          pacManArc = 45;
        }
      }

      // Adding these functions for KeyListener to function
      public void keyTyped(KeyEvent e) {
      }

      public void keyReleased(KeyEvent e) {
      }
    });

    // Adding the game to the screen
    frame.add(new Main());
  }

  // Drawing graphics for the game
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    // Drawing the maze
    for (int i = 0; i < mazeData.length; i++) {
      for (int j = 0; j < mazeData[i].length; j++) {
        if (mazeData[i][j] == 1) { // Draws the wall tiles
          g2d.setColor(new Color(0, 0, 128)); // Sets color
          g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize); // Draws a rectangle (x, y, width, height)
        } else if (mazeData[i][j] == 2) { // Draws the pellets
          g2d.setColor(Color.WHITE);
          g2d.fillOval(j * tileSize + tileSize / 2 - 5, i * tileSize + tileSize / 2 - 5, 6, 6); // Draws a circle
        } else if (mazeData[i][j] == 3) { // Draws the power pellets
          g2d.setColor(Color.WHITE);
          g2d.fillOval(j * tileSize + tileSize / 2 - 7, i * tileSize + tileSize / 2 - 5, 9, 9);
        } else if (mazeData[i][j] == 4) { // Draws the teleport tiles
          g2d.setColor(Color.RED);
          g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
        } else if (mazeData[i][j] == 5) { // Draws the ghost spawn border tiles
          g2d.setColor(Color.DARK_GRAY);
          g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
        }
      }
    }

    // Drawing Pac-Man
    g2d.setColor(Color.YELLOW);
    g2d.fillArc(pacManPosX, pacManPosY, tileSize, tileSize, pacManArc, 270);

    // Drawing the ghosts
    for (int i = 0; i < ghostsPos.length; i++) {
      if (ghostScared) { // If the ghosts are scared, set their color to blue
        g2d.setColor(Color.BLUE);
      } else {
        g2d.setColor(ghostsColor[i]);
      }
      g2d.fillRect(ghostsPos[i][0], ghostsPos[i][1], tileSize - 5, tileSize - 5);
    }

    // Drawing the score and the amount of lives
    g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
    g2d.setColor(Color.WHITE);
    g2d.drawString("Score: " + score, 5, 10);
    g2d.drawString("Lives: " + lives, 295, 430);
  }

  // Checks for Pac-Man's collisions with maze elements
  public static void pacManMazeCollide() {
    // A hitbox for Pac-Man's new position
    Rectangle newPacManHitbox = new Rectangle(newPacManPosX, newPacManPosY, tileSize, tileSize);

    // Looping through each tile in mazeData
    for (int i = 0; i < mazeData.length; i++) {
      for (int j = 0; j < mazeData[i].length; j++) {

        if (mazeData[i][j] == 1 || mazeData[i][j] == 5) { // Checks if the tile is a wall or spawn border
          // Setting a hitbox for the wall
          Rectangle wallHitbox = new Rectangle(j * tileSize, i * tileSize, tileSize, tileSize);

          // Moves Pac-Man to the edge of the wall if the player hits one
          if (newPacManHitbox.intersects(wallHitbox)) {
            if (pacMoveX > 0) {
              newPacManPosX = j * tileSize - tileSize;
            } else if (pacMoveX < 0) {
              newPacManPosX = (j + 1) * tileSize;
            } else if (pacMoveY > 0) {
              newPacManPosY = i * tileSize - tileSize;
            } else if (pacMoveY < 0) {
              newPacManPosY = (i + 1) * tileSize;
            }
          }
        } else if (mazeData[i][j] == 4) { // Checks if the tile is a teleport
          // Setting a hitbox for the teleport
          Rectangle teleportHitbox = new Rectangle(j * tileSize, i * tileSize, tileSize, tileSize);

          if (newPacManHitbox.intersects(teleportHitbox)) {
            // If the player's X position is less than the screen width, this means they are
            // on the left teleporter
            if (newPacManPosX < (tileSize * screenWidth) / 2) {
              newPacManPosX = (mazeData[0].length - 1) * tileSize - tileSize; // Teleport to the right
            } else {
              newPacManPosX = tileSize; // Teleport to the left
            }
          }
        }
      }
    }
  }

  // Function to move Pac-Man
  public static void movePacMan() {

    // Changes Pac-Man to a power pellet state
    if (powerPelletActive) {
      powerPelletTimer--;

      // Reverts Pac-Man to his original state when the power pellet runs out
      if (powerPelletTimer == 0) {
        powerPelletActive = false;
        pacManSpeed = 2;
        ghostScared = false;
      }
    }

    // Pac-Man's new position
    newPacManPosX = pacManPosX + pacMoveX;
    newPacManPosY = pacManPosY + pacMoveY;

    // Without this method, most of Pac-Man's body will be able to pass through walls
    pacManMazeCollide();

    // Updating Pac-Man's X and Y position and hitbox location
    pacManPosX = newPacManPosX;
    pacManPosY = newPacManPosY;
    pacManHitbox.setLocation(pacManPosX, pacManPosY);
    pelletEaten();
  }

  // Determines how the ghost will move
  public static void ghostDirect(int direction) {

    // Uses the same logic for key inputs for Pac-Man
    if (direction == 0) { // UP
      ghostMoveY = ghostSpeed * -1;
      ghostMoveX = 0;
    } else if (direction == 1) { // DOWN
      ghostMoveY = ghostSpeed;
      ghostMoveX = 0;
    } else if (direction == 2) { // LEFT
      ghostMoveX = ghostSpeed * -1;
      ghostMoveY = 0;
    } else if (direction == 3) { // RIGHT
      ghostMoveX = ghostSpeed;
      ghostMoveY = 0;
    }
  }

  // Checks for the ghosts' collisions with maze elements (Uses same logic as
  // pacManMazeCollide())
  public static void ghostMazeCollide() {
    // A hitbox for the ghost's new position
    Rectangle newGhostHitbox = new Rectangle(newGhostPosX, newGhostPosY, tileSize - 5, tileSize - 5);

    // Looping through each tile in mazeData
    for (int i = 0; i < mazeData.length; i++) {
      for (int j = 0; j < mazeData[i].length; j++) {

        if (mazeData[i][j] == 1) { // Checks if the tile is a wall or teleport
          Rectangle wallHitbox = new Rectangle(j * tileSize, i * tileSize, tileSize, tileSize);

          // If a ghost hits a wall/teleport, move them back to the middle of the tile
          // they are on
          if (newGhostHitbox.intersects(wallHitbox)) {
            newGhostPosX = j * tileSize + tileSize / 2 - tileSize / 4;
            newGhostPosY = i * tileSize + tileSize / 2 - tileSize / 4;
          }
        } else if (mazeData[i][j] == 4) {
          Rectangle teleportHitbox = new Rectangle(j * tileSize, i * tileSize, tileSize, tileSize);
          if (newGhostHitbox.intersects(teleportHitbox)) {
            newGhostPosX = j * tileSize - tileSize;
          }
        } else { // If the ghost is not encountering a wall or teleport, move it
          ghostDirect(ghostDirection);
        }
      }
    }
  }

  // Function to move the ghost
  public static void moveGhost(int index) {

    // Grabbing the an individual ghost's direction and position
    ghostDirection = ghostsDirection[index];
    ghostPosX = ghostsPos[index][0];
    ghostPosY = ghostsPos[index][1];

    // If the ghost is scared, reverses their movements
    if (ghostScared) {
      ghostMoveX *= -1;
      ghostMoveY *= -1;
    }

    // Ghost's new positions
    newGhostPosX = ghostPosX + ghostMoveX;
    newGhostPosY = ghostPosY + ghostMoveY;

    ghostMazeCollide();

    // If the next tile is able to move in, change the ghost's position and hitboxe
    if (validMove(newGhostPosX, newGhostPosY)) {
      ghostsPos[index][0] = newGhostPosX;
      ghostsPos[index][1] = newGhostPosY;
      ghostsHitbox[index].setLocation(ghostPosX, ghostPosY);
    } else { // If the next tile is not valid, change the ghost's direction randomly
      ghostsDirection[index] = random.nextInt(4);
    }
  }

  // Checks if the next tile is valid to move in
  public static boolean validMove(int entityX, int entityY) {
    // Calculating grid coordinates of the entity's position
    int row = entityY / tileSize;
    int col = entityX / tileSize;

    // Returns if the position is within bounds and not a wall
    return row >= 0 && row <= mazeData.length && col >= 0 && col <= mazeData[0].length && mazeData[row][col] != 1;
  }

  // Checks if a pellet was consumed
  public static void pelletEaten() {
    int row = pacManPosY / tileSize;
    int col = pacManPosX / tileSize;

    // If Pac-Man eats a pellet, change that tile to a black background
    if (mazeData[row][col] == 2) {
      mazeData[row][col] = 0;
      score += 10; // Increment the score by ten for each pellet eaten
    }
    // If Pac-Man eats a power pellet
    else if (mazeData[row][col] == 3) {
      mazeData[row][col] = 0;
      score += 50;
      activatePowerPellet();
    }
  }

  // Changes Pac-Man into a power pellet state
  public static void activatePowerPellet() {
    powerPelletActive = true;
    powerPelletTimer = 200;
    pacManSpeed = 3;
    ghostScared = true;
  }

  // Checks speed and increases ghost speed every 200 points
  public static void checkScore() {
    if (score >= speedIncreasePoint) {
      ghostSpeed++;
      speedIncreasePoint += 200; // Increases the threshold for the ghostSpeed increase
    }
  }

  // Checks for collisions between Pac-Man and the ghosts
  public static void checkEntityCollisions() {
    for (int i = 0; i < 4; i++) {
      if (pacManHitbox.intersects(ghostsHitbox[i])) {
        // If the player hits a ghost with a power pellet active
        if (powerPelletActive) {
          resetGhost(i);
          score += 100;
        }
        // Player loses a life and has their position reset when caught by a ghost
        else {
          lives--;
          resetPacMan();
        }
      }
    }
  }

  // Resets Pac-Man if the player gets caught by ghosts
  public static void resetPacMan() {
    // Reset Pac-Man
    pacManPosX = defaultPacManPosX;
    pacManPosY = defaultPacManPosY;
    pacMoveX = 0;
    pacMoveY = 0;
    powerPelletActive = false;
    powerPelletTimer = 0;
    pacManSpeed = 2;

    // Freeze the game for three seconds
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // Resets the ghosts' positions if they are caught by Pac-Man in a power pellet
  // state
  public static void resetGhost(int index) {
    ghostsPos[index][0] = defaultGhostPosX;
    ghostsPos[index][1] = defaultGhostPosY;
    ghostsHitbox[index] = new Rectangle(defaultGhostPosX, defaultGhostPosY, tileSize - 5, tileSize - 5);
  }

  // Checks if there are any pellets left in the maze
  public static boolean checkPelletsLeft() {
    for (int i = 0; i < mazeData.length; i++) {
      for (int j = 0; j < mazeData[i].length; j++) {
        if (mazeData[i][j] == 2 || mazeData[i][j] == 3) { // Checks if the tile is a pellet or power pelle
          return true;
        }
      }
    }
    return false;
  }

  // Resets the maze
  public static void resetMaze() {
    for (int i = 0; i < mazeData.length; i++) { // Resetting map
      for (int j = 0; j < mazeData[i].length; j++) {
        mazeData[i][j] = mazeDataCopy[i][j];
      }
    }
  }

  // Resets the game
  public static void resetGame() {
    for (int i = 0; i < 4; i++) {
      resetGhost(i);
    }
    resetPacMan();
    resetMaze();
    ghostSpeed = 1;
    ghostScared = false;
    score = 0;
    lives = 3;
  }

  public static void gameOver() {
    timer.stop(); // Stopping the game loop

    panel.setVisible(true);

    // Game over message
    endMessage = new JLabel();
    endMessage.setText("Game Over");
    endMessage.setForeground(Color.RED);
    endMessage.setFont(new Font("TimesRoman", Font.BOLD, 20));
    endMessage.setBounds(130, 30, 200, 20);
    endMessage.setVisible(true);
    panel.add(endMessage);

    // Final score
    finalScore = new JLabel();
    finalScore.setText("Final Score: " + Integer.toString(score));
    finalScore.setForeground(Color.RED);
    finalScore.setFont(new Font("TimesRoman", Font.BOLD, 15));
    finalScore.setBounds(130, 60, 200, 20);
    panel.add(finalScore);

    // "Play again" button to restart the game
    againButton = new JButton("Play Again");
    againButton.setBounds(125, 100, 100, 40);
    againButton.setFocusable(false);
    againButton.setVisible(true);
    againButton.addActionListener(new ActionListener() { // Implementing user input for the button
      public void actionPerformed(ActionEvent click) {
        if (click.getSource() == againButton) {

          // Hiding the endscreen
          againButton.setVisible(false);
          returnButton.setVisible(false);
          endMessage.setVisible(false);
          finalScore.setVisible(false);
          panel.setVisible(false);

          resetGame();

          // Freeze the game for three seconds
          try {
            Thread.sleep(3000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          timer.start(); // Starting the game loop
        }
      }
    });
    panel.add(againButton);

    // "Return" button to return to the menu
    returnButton = new JButton("Return");
    returnButton.setBounds(125, 200, 100, 40);
    returnButton.setFocusable(false);
    returnButton.setVisible(true);
    returnButton.addActionListener(new ActionListener() { // Implementing user input for the button
      public void actionPerformed(ActionEvent click) {
        if (click.getSource() == returnButton) {

          // Hiding the endscreen
          againButton.setVisible(false);
          returnButton.setVisible(false);
          endMessage.setVisible(false);
          finalScore.setVisible(false);

          userHighscore.setText("Highscore: " + Integer.toString(highscore)); // Updating the highscore

          // Setting the menu to be visible again
          playButton.setVisible(true);
          howButton.setVisible(true);
          quitButton.setVisible(true);
          title.setVisible(true);
          userHighscore.setVisible(true);
        }
      }
    });
    panel.add(returnButton);
  }
}