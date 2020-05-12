/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter values to compute Pythagorean Theorem");
		int a = readInt("a: ");
		int b = readInt("b: ");
		println("c = " + computeResult(a, b));
	}

	private double computeResult(int a, int b) {
		int sumOfSquares = a * a + b * b;
		double c = Math.sqrt(sumOfSquares);
		return c;
	}
}
