/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	public void run() {
		addCircle(72, Color.RED);
		addCircle(46.8, Color.WHITE);
		addCircle(21.6, Color.RED);
	}

	private void addCircle(double radius, Color color) {
		double x = getWidth() / 2.0 - radius;
		double y = getHeight() / 2.0 - radius;
		GOval circle = new GOval(x, y, radius * 2, radius * 2);
		circle.setFilled(true);
		circle.setColor(color);
		add(circle);
	}

}
