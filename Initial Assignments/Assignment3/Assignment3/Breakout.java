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
import acm.util.*;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/**
	 * Width and height of application window in pixels
	 */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/**
	 * Dimensions of game board (usually the same)
	 */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/**
	 * Dimensions of the paddle
	 */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 40;

	/**
	 * Offset of the paddle up from the bottom
	 */
	private static final int PADDLE_Y_OFFSET = 30;

	/**
	 * Number of bricks per row
	 */
	private static final int NBRICKS_PER_ROW = 10;

	/**
	 * Number of rows of bricks
	 */
	private static final int NBRICK_ROWS = 10;

	/**
	 * Separation between bricks
	 */
	private static final int BRICK_SEP = 4;

	/**
	 * Width of a brick
	 */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/**
	 * Height of a brick
	 */
	private static final int BRICK_HEIGHT = 8;

	/**
	 * Radius of the ball in pixels
	 */
	private static final int BALL_RADIUS = 10;

	/**
	 * Offset of the top brick row from the top
	 */
	private static final int BRICK_Y_OFFSET = 70;

	/**
	 * Number of turns
	 */
	private static final int NTURNS = 3;

	/**
	 * Animation delay or pause time between ball moves
	 */
	private static final int DELAY = 5;

	/**
	 * Runs the Breakout program.
	 */
	public void run() {
		for (int i = 0; i < NTURNS; i++) {
			setupEnvironment();
			if (playGame()) {
				announceWinner();
				return;
			}
		}
		announceLoser();
	}

	private void setupEnvironment() {
		removeAll();
//		removeAllComponents();
		setSize(WIDTH, HEIGHT);
		addBricks(getWidth() / 2.0);
		addPaddle();
		addBall();
	}

	private void addBricks(double cx) {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				double x = cx - (NBRICKS_PER_ROW * BRICK_WIDTH) / 2.0 - ((NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2.0
						+ j * (BRICK_WIDTH + BRICK_SEP);
				double y = (double) Breakout.BRICK_Y_OFFSET + i * (BRICK_HEIGHT + BRICK_SEP);
				GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				if (i < 2) {
					brick.setFillColor(Color.RED);
				} else if (i < 4) {
					brick.setFillColor(Color.ORANGE);
				} else if (i < 6) {
					brick.setFillColor(Color.YELLOW);
				} else if (i < 8) {
					brick.setFillColor(Color.GREEN);
				} else {
					brick.setFillColor(Color.CYAN);
				}
				add(brick);
			}
		}
		bricks = NBRICKS_PER_ROW * NBRICK_ROWS;
	}

	private void addPaddle() {
		paddle = new GRect(getWidth() / 2.0 - PADDLE_WIDTH / 2.0,
				getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		addMouseListeners();
		add(paddle);
	}

	private void addBall() {
		double x = getWidth() / 2.0 - BALL_RADIUS;
		double y = getHeight() / 2.0 - BALL_RADIUS;
		ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		add(ball);
	}


	public void mouseMoved(MouseEvent e) {
		if (e.getX() > PADDLE_WIDTH / 2
				&& e.getX() < getWidth() - PADDLE_WIDTH / 2 ) { //TRUE if mouse is in range of the screen
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2,
					getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT); //moves with mouse. the y coordinate is constant
		}
	}

	private boolean playGame() {
		waitForClick();
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
		vy = vx;
		while (!gameIsOver()) {
			moveBall();
		}
		return playerWon();
	}

	private boolean playerWon() {
		return bricks <= 0;
	}

	private boolean gameIsOver() {
		return ((ball.getY() - (BALL_RADIUS * 2)) > getHeight()) || (bricks == 0);
	}

	private void moveBall() {
		ball.move(vx, vy);
		if ((ball.getX() + 2*BALL_RADIUS) > getWidth() || ball.getX() < 0){
			vx = -vx;
		}
		if (ball.getY() < 0) {
			vy = -vy;
		}
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			vy = -vy;
			vy *= 1.05; //game gets
			vx *= 1.05;
		} else if (collider != null) {
			remove(collider);
			vy = -vy;
			bricks--;
		}

		pause(DELAY);

//		CHEAT!
		paddle.setLocation(ball.getX(), paddle.getY());
	}

	private void nextFrame() {
		ball.move(vx, vy);
		// this checks for left and right wall collisions
		if ((ball.getX()  <= 0 && vx < 0)
				|| (ball.getX() + vx >= (getWidth() - BALL_RADIUS * 2) && vx > 0)) {
			vx = -vx;
		}
		// this checks for top wall collision
		if ((ball.getY()  <= 0 && vy < 0)) {
			vy = -vy;
		}
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			vy = vy < 0 ? vy : -vy;
		}
		// if the collider is not the paddle, and it is not null, it should be the brick, so we delete it.
		else if (collider != null) {
			vy = -vy;
			remove(collider);
			bricks--;
			//AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
//			bounceClip.play();
		}
		pause(DELAY);
	}

//	private GObject getCollidingObject() {
//		if (getElementAt(ball.getX(), ball.getY()) != null) {
//			return getElementAt(ball.getX(), ball.getY());
//		}
//		if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null) {
//			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
//		}
//
//		if (getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null) {
//			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
//		}
//		if (getElementAt(ball.getX() + 2 * BALL_RADIUS,
//				ball.getY() + 2 * BALL_RADIUS) != null) {
//			return getElementAt(ball.getX() + 2 * BALL_RADIUS,
//					ball.getY() + 2 * BALL_RADIUS);
//		} else {
//			return null;
//		}
//	}

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

	private void announceLoser() {
		ball.setVisible(false);
		GLabel loser = new GLabel("Fuck you loser! " +
				"Try Again Later!");
		loser.setLocation(getWidth() / 2.0 - loser.getWidth() / 2,
				getHeight() / 2.0 + loser.getAscent() / 2);
		loser.setColor(Color.BLACK);
		add(loser);
	}

	private void announceWinner() {
		ball.setVisible(false);
		GLabel winner = new GLabel("Congratulations! You are a winner!");
		winner.setLocation(getWidth() / 2.0 - winner.getWidth() / 2,
				getHeight() / 2.0 + winner.getAscent() / 2);
		winner.setColor(Color.green);
		add(winner);
	}

	private int bricks = NBRICKS_PER_ROW * NBRICK_ROWS;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
}