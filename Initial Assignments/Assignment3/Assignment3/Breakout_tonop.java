
/*
 * File: Breakout.java
 * -------------------
 * Name: TORNIKE ONOPRISHVILI
 * Section Leader: ELENE KAVTELADZE
 * 
 * IMPORTANT NOTICE: BEFORE FULFILLING THIS PROJECT, I TOOK SOME ADVICE FROM MY FELLOWS AND ALSO,
 * LOOKED THROUGH SOME EXAMPLES OF SIMILAR PROJECTS ON THE INTERNET. ALTHOUGH I NEVER EXPLICITLY COPIED
 * THEIR WORK, I WAS HEAVILY INFLUENCED BY THEIR PROGRAMING METHODOLOGY. 
 * SOME SIMILARITIES MAY ARISE FROM THIS SOURCE: https://gist.github.com/NatashaTheRobot/1375730
 * AND THAT IS BECAUSE I LEARNED FROM THERE SOME OF THE DECOMPOSITION PRINCIPLES.
 * 
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout_tonop extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10; //size can be changed. from 1 to 100 game is still playable

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10; //from 1 to 50 game is playable

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Ball velocity */
	private double vx, vy;
	
	/** next frame delay */
	private static final int DELAY = 10;

	/** Random number generator for Speed */
	private RandomGenerator random = RandomGenerator.getInstance();

	/** Runs the Breakout program. */
	public void run() {
		int currentGame = 0;
		while (currentGame < NTURNS) { // you can only play limited amount of
										// games.
			setupEnviroment();
			play();
			currentGame++;
			if (brickCounter == 0) { // if you broke all bricks, you won.
				removeAll();
				winScreen();
				break;
			} else {
				removeAll(); // start the game anew.
			}
		}
		if (brickCounter > 0) { // if you have no lives left and bricks are left,
								// you lose
			loseScreen();
		}
	}

	private void setupEnviroment() { //draws bricks, paddle and a ball.
		addBricks(); //these arguments are to get the bricks aligned to the center of the screen
		addPaddle();
		addBall();
	}


	/*
	 * to make decomposition possible, i had to make this variable available to all methods in my program
	 */
	private GRect brick;

	/*
	 *  draws all the bricks
	 *
	 *  this method implements two nested for cycles,
	 *  the first one cycles through rows, where the second one through individual bricks
	 *
	 */
	private void addBricks() {
		for (int row = 0; row < NBRICK_ROWS; row++) { //loops through rows
			for (int column = 0; column < NBRICKS_PER_ROW; column++) { //loops through bricks (columns)

				/*
				 * There was a problem with an alignment of bricks to the center of the screen,
				 *
				 * the x coordinate is the coordinate of the first brick to the left of the screen
				 * and it is calculated as follows, get the middle of screen, subract half of the row size
				 * (number of bricks times their width, plus separation times number of bricks PLUS ONE
				 * that PLUS ONE was especially hard to catch)
				 * for each next brick, a brick size and a separation size is added to the x coordinate.
				 */
				double x = getWidth()/2 - (NBRICKS_PER_ROW * BRICK_WIDTH) / 2
						- ((NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2	+ column * BRICK_WIDTH + column * BRICK_SEP;
				double y = BRICK_Y_OFFSET + row * BRICK_HEIGHT + (row + 1 ) * BRICK_SEP; //same here, brick offset plus bricks and their separation
				brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
				brick.setFilled(true);
				colorPicker(row); //get colors for bricks
			}
		}
	}

	// to make decomposition possible i had to make this variable available to all methods in my program
	private GOval ball;


		// ball set-up
	private void addBall() {
		ball = new GOval(getWidth() / 2 - BALL_RADIUS,
				getHeight() / 2 - BALL_RADIUS
						+ (BRICK_HEIGHT + BRICK_SEP) * NBRICK_ROWS / 2,
				2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}

	// paddle object that will be called from other methods
	private GRect paddle;

	// draws and adds black rectangle, namely paddle
	private void addPaddle() {
		paddle = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2,
				getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		addMouseListeners();
		add(paddle);
	}

	//this method makes sure that paddle moves with the mouse AND it never changes it's y coordinate.
	public void mouseMoved(MouseEvent e) {
		if (e.getX() > PADDLE_WIDTH / 2 && e.getX() < getWidth() - PADDLE_WIDTH / 2 ) { //TRUE if mouse is in range of the screen
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2,
					getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT); //moves with mouse. the y coordinate is constant
		}
	}


	//sets colors for any number bricks
	private void colorPicker(int row) {
		if (row % 10 < 2) {
			brick.setColor(Color.RED);
		}
		else if (row % 10 < 4) {
			brick.setColor(Color.ORANGE);
		}
		else if (row % 10 < 6) {
			brick.setColor(Color.YELLOW);
		}
		else if (row % 10 < 8) {
			brick.setColor(Color.GREEN);
		}
		else {
			brick.setColor(Color.CYAN);
		}
	}

	/*
	 * this method lets us play the game
	 * 
	 * it waits for mouse click, randomly selects ball speed,
	 * it has an endless loop, that only breaks when:
	 * 1) ball hits the bottom;
	 * 2) all bricks are gone.
	 */
	private void play() {
		waitForClick();
		rollTheBall();
		while (true) {
			moveBall();
			if (ball.getY() >= getHeight())
				break;
			if (brickCounter == 0)
				break;
		}
	}

	//keeps track of how many bricks we have. we need this variable to know when to stop the game.
	private int brickCounter = NBRICK_ROWS * NBRICK_ROWS;
	
	/*
	 * THIS IS THE MOST IMPORTANT METHOD
	 * 
	 * this method moves the ball around with a predefined speed,
	 * and is also responsible for all In-game physics.
	 * wall collisions, ball-brick collisions, and ball-paddle collisions
	 * are all written in this method.
	 * 
	 * this method is called the most often, so it is crucial to make it as optimal as possible
	 */
	private void moveBall() {
		ball.move(vx, vy);
		// this checks for left and right wall collisions
		if ((ball.getX() - vx <= 0 && vx < 0) || (ball.getX() + vx >= (getWidth() - BALL_RADIUS * 2) && vx > 0)) {
			vx = -vx;
		}
		// this checks for top wall collision
		if ((ball.getY() - vy <= 0 && vy < 0)) {
			vy = -vy;
		}
		//NOTICE that we are not implementing the bottom wall collision. because, we want to break the cycle if that happens and draw the board anew.

		// check for collisions with paddle and bricks
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			/*
			 * THE HARDEST THREE LINES IN THIS PROGRAM
			 *
			 * it basically goes like this: if ball is within the paddle movement line ( height minus paddle offset)
			 * AND a collision occurred, you are guaranteed that a paddle hit the ball and not a brick.
			 * this was especially hard to come up with.
			 * first i tried to calculate the distance between the centers of the paddle and the ball and
			 * if that distance was shorter than their widths, ball was reflected.
			 * but that didn't work out well, so i had to search the Internet for the solution
			 * I tried to use gObject.Intersects() method, but found that we are not allowed to.
			 * then i found https://gist.github.com/NatashaTheRobot/1375730 code and looked through it
			 * these three lines are written by me, but, they are the same exact solution to the problem.
			 *
			 * reflect ball if it is in range of the paddle and we detect collision.
			 */
			if (ball.getY() < getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * (1.4) //where the 1.4 is the error margin
					&& ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2) { //the error margin was calculated by trial and error.
				vy = -vy;
				AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
				bounceClip.play();
				vy += vy /20; //game gets
				vx += vx /20;
			}
		}
		// if the collider is not the paddle, and it is not null, it should be the brick, so we delete it.
		else if (collider != null) {
			vy = -vy;
			remove(collider);
			brickCounter--;
			//AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
//			bounceClip.play();
		}
		pause(DELAY);
	}
	
	//this method checks the four points around the ball
	//it returns the colliding object.
	//if it finds no such objects, it return null.
	private GObject getCollidingObject() {
		/*
		 * this is an array of four points.
		 * all of the points are checked for collisions and
		 * then the for cycles through them to find the colliding object
		 */
		GObject[] colliders = new GObject[] { getElementAt(ball.getX(), ball.getY()),
				getElementAt((ball.getX() + BALL_RADIUS * 2), ball.getY()),
				getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2),
				getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2) };
		for (GObject col : colliders) {
			if (col != null)
				return col;
		}
		return null;
	}
	// this method is used when we want to get the initial speed
	private void rollTheBall() {
		vy = random.nextDouble(2, 4);
		vx = random.nextDouble(3, 4);
		if (random.nextBoolean()) {
			vx = -vx;
		}
	}
	
	// when you win, this method writes a victory message on screen
	private void winScreen() {
		GLabel winlbl = new GLabel("You are awesome!", getWidth() / 2, getHeight() / 2);
		winlbl.move(-winlbl.getWidth() / 2, -winlbl.getHeight() / 2);
		add(winlbl);
		AudioClip winMusic = MediaTools.loadAudioClip("odeToJoy.wav");
		winMusic.play();
		GRect background = new GRect(0, 0, getWidth(), getHeight());
		background.setColor(Color.RED);
		background.setFilled(true);
		add(background);
		background.sendToBack();
		for (int i = 0; i < 100; i++) { //100 is the number at which the song ends. after a much calculation
				background.setColor(Color.green);
			pause(480);
				background.setColor(Color.blue);
			pause(480);
				background.setColor(Color.white);
			pause(480);
				background.setColor(Color.yellow);
			pause(480);
				background.setColor(Color.pink);
			pause(480);
		}
	}
	
	// if your ball touches the bottom more than NTURNs you lose
	private void loseScreen() {	//NO SONGS FOR LOSERS
		GLabel gameOverlbl = new GLabel("GAME OVER!", getWidth() / 2, getHeight() / 2);
		gameOverlbl.move(-gameOverlbl.getWidth() / 2, -gameOverlbl.getHeight() / 2);
		add(gameOverlbl);
	}
}