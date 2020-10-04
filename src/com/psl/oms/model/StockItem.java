package com.psl.oms.model;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class StockItem {
	private int itemNumber;
	private String itemDescription;
	public double itemPrice = 0;
	public Unit unit;
	int quantity;

	public StockItem() {
		this.itemNumber = 0;
		this.itemDescription = "";
		this.itemPrice = 0;
		this.quantity = 0;
	}

	public StockItem(int itemNumber, String itemDescription, double itemPrice, Unit unit, int quantity) {
		super();
		this.itemNumber = itemNumber;
		this.itemDescription = itemDescription;
		this.itemPrice = itemPrice;
		this.unit = unit;
		this.quantity = quantity;
	}
	
	public StockItem(int itemNumber, String itemDescription, double itemPrice) {
		super();
		this.itemNumber = itemNumber;
		this.itemDescription = itemDescription;
		this.itemPrice = itemPrice;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
