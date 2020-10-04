package com.psl.oms.model;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public enum Unit {
	KG, DOZEN, GALLONS, NUMBERS, GRAMS(250);

	private int value;

	private Unit() {
		this.value = 1;
	}

	Unit(int value) {
		this.value = value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}