/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {

	public static final int BOX_WIDTH = 150;
	public static final int BOX_HEIGHT = 60;

	public void run() {
		double width = 600;
		double height = 180;
		double x = (getWidth() - width) / 2.0;
		double y = (getHeight() - height) / 2.0;
		drawEverything(x, y, width, height);
		addLine(x + BOX_WIDTH / 2.0, y + height - BOX_HEIGHT, height);
		addLine(getWidth() / 2.0, y + height - BOX_HEIGHT, height);
		addLine(x + width - BOX_WIDTH / 2.0, y + height - BOX_HEIGHT, height);
	}

	private void drawEverything(double x, double y, double width, double height) {
		drawLabeledBox(x + width / 2.0 - BOX_WIDTH / 2.0, y,
				BOX_WIDTH, BOX_HEIGHT, "Program");
		drawLabeledBox(x + width / 2.0 - BOX_WIDTH / 2.0,
				y + height - BOX_HEIGHT,
				BOX_WIDTH, BOX_HEIGHT, "Console Program");
		drawLabeledBox(x, y + height - BOX_HEIGHT, BOX_WIDTH,
				BOX_HEIGHT, "GraphicsProgram");
		drawLabeledBox(x + width - BOX_WIDTH, y + height - BOX_HEIGHT,
				BOX_WIDTH, BOX_HEIGHT, "DialogProgram");
	}

	private void drawLabeledBox(double x, double y, double width, double height, String text) {
		GRect box = new GRect (x, y, width, height);
		add(box);
		GLabel label = new GLabel(text);
		add(label, x + width / 2.0 - label.getWidth() / 2.0,
				y + height/2.0 + label.getAscent()/2.0);
	}

	private void addLine(double x2, double y2, double height) {
		double x1 = getWidth() / 2.0;
		double y1 = (getHeight() - height) / 2.0 + BOX_HEIGHT;
		GLine line = new GLine (x1, y1, x2, y2);
		add(line);
	}

}

