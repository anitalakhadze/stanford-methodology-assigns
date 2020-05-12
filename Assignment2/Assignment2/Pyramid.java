/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		drawPyramid(getWidth()/2.0 - (BRICKS_IN_BASE * BRICK_WIDTH)/2.0, getHeight() - BRICK_HEIGHT);
	}

	private void drawPyramid(double x, double y) {
		for (int row = 0; row < BRICKS_IN_BASE; row++) {
			addRow(x + (BRICK_WIDTH/2.0) * row, y - BRICK_HEIGHT * row, BRICKS_IN_BASE - row);
		}
	}

	private void addRow(double x, double y, int nBricks) {
		for (int i = 0; i < nBricks; i++) {
			addBrick(x + i * BRICK_WIDTH, y);
		}
	}

	private void addBrick(double x, double y) {
		add (new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT));
	}
}

