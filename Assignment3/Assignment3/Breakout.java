/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

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
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

	private static final int DELAY = 5;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		playGame();
	}

	private void playGame() {
		for (int turn = 0; turn < NTURNS; turn++) {
			setupNewTurn();
			playSingleTurn();
			if (counter == 0) {
				announceStatus("Congratulations! You won!");
				return;
			}
		}
		announceStatus("You lost! Try again!");
	}

	private void setupNewTurn() {
		removeAll();
		initGame();
	}

	private void playSingleTurn() {
		waitForClick();
		while (!gameOver()) {
			moveBall();
		}
	}

	public void initGame(){
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = vx;
		counter = NBRICK_ROWS * NBRICKS_PER_ROW;
		double x = APPLICATION_WIDTH / 2.0;
		double y = APPLICATION_HEIGHT / 2.0;
		drawEverything(x, y);
	}

	private void drawEverything (double x, double y) {
		addBrickWall(x, y);
		addPaddle(x);
		addBall(x, y, BALL_RADIUS);
	}

	private void addBrickWall(double x, double y) {
		Color color = Color.WHITE;
		for (int row = 0; row < NBRICK_ROWS; row++) {
			if (row == 0 || row == 1){
				color = Color.RED;
			} else if (row == 2 || row ==3) {
				color = Color.ORANGE;
			} else if (row == 4 || row == 5) {
				color = Color.YELLOW;
			} else if (row == 6 || row == 7) {
				color = Color.GREEN;
			} else if (row == 8 || row == 9) {
				color = Color.CYAN;
			}
			addRow(x - (NBRICKS_PER_ROW / 2) * (BRICK_WIDTH + BRICK_SEP) + BRICK_SEP,
					BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * row,
					NBRICKS_PER_ROW, color);
		}
	}

	private void addRow (double x, double y, int nBricks, Color color) {
		for (int i = 0; i < nBricks; i++) {
			addBrick(x + i * (BRICK_WIDTH + BRICK_SEP), y, color);
		}
	}

	private void addBrick (double x, double y, Color color) {
		GRect brick = new GRect (x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setFillColor(color);
		add(brick);
	}

	private void addPaddle(double x) {
		paddle = new GRect(x - PADDLE_WIDTH / 2.0,
				APPLICATION_HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
		addMouseListeners();
	}

	public void mouseMoved (MouseEvent e) {
		if (e.getX() > PADDLE_WIDTH / 2.0 && e.getX() < APPLICATION_WIDTH - PADDLE_WIDTH / 2.0) {
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2.0,
					APPLICATION_HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		}
	}

	private void addBall(double x, double y, double radius) {
		ball = new GOval(x - radius, y - radius,
				radius * 2, radius * 2);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		add(ball);
	}

	private void moveBall() {
		ball.move(vx, vy);
		checkForCollisions();
		if (ball.getX() < 0 || ball.getX() + BALL_RADIUS * 2 > APPLICATION_WIDTH) {
			vx = -vx;
		}
		if (ball.getY() < 0) {
			vy = -vy;
		}
		pause(DELAY);
	}

	private void checkForCollisions() {
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			vy = -vy;
		} else if (collider != null) {
			vy = -vy;
			remove(collider);
			counter--;
		}
	}

	private GObject getCollidingObject() {
		GObject collider = null;
		if (getElementAt(ball.getX(), ball.getY()) != null) {
			collider = getElementAt(ball.getX(), ball.getY());
		}
		if (getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY()) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY());
		}
		if (getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2) != null) {
			collider = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2);
		}
		if (getElementAt(ball.getX() + BALL_RADIUS * 2,
				ball.getY() + BALL_RADIUS * 2) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS * 2,ball.getY() + BALL_RADIUS * 2);
		}
		return collider;
	}

	private boolean gameOver() {
		return (ball.getY() + BALL_RADIUS * 2 >= APPLICATION_HEIGHT || counter == 0);
	}

	private void announceStatus(String label) {
		GLabel message = new GLabel(label);
		message.setLocation((APPLICATION_WIDTH - message.getWidth()) / 2.0,
				(APPLICATION_HEIGHT - message.getAscent()) / 2.0);
		message.setColor(Color.MAGENTA);
		message.setFont("Courier-30");
		add(message);
	}

	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx, vy;
	private GRect paddle;
	private GOval ball;
	private int counter;
}
