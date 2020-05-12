/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {

	public static final int SENTINEL = 0;

	public void run() {
		int value = readInt("? ");
		if (value == SENTINEL) {
			println("No values have been entered");
		} else {
			int smallest = value;
			int largest = value;

			while (value != SENTINEL) {
				value = readInt("? ");
				if (value > largest) largest = value;
				else if (value < smallest) smallest = value;
			}
			println("smallest: " + smallest);
			println("largest: " + largest);
		}
	}
}

