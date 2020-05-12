/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {

	int value = readInt("Enter a number: ");
	int counter = 0;

	while (value != 1) {
		if (value % 2 == 0) {
			println(value + " is even, so I take half: " + value/2);
			value /= 2;
			counter++;
		} else {
			println(value + " is odd, so I make 3n + 1: " + (value * 3 + 1));
			value = 3 * value + 1;
			counter++;
		}
	}
	println("This process took " + counter + " to reach 1");
	}
}

