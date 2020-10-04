package com.psl.oms.model;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class OrderItem {
	private int orderItemNumber;
	private int stockItemNumber;
	private int numberOfItems;
	private int poNumber;
	private StockItem stockItem = null;

	public OrderItem(int stockItemNumber, int numberOfItems, int poNumber) {
		super();
		//this.orderItemNumber = orderItemNumber;
		this.stockItemNumber = stockItemNumber;
		this.numberOfItems = numberOfItems;
		this.poNumber = poNumber;
	}
	
	public OrderItem(int orderItemNumber, int stockItemNumber, int numberOfItems, int poNumber, StockItem stockItem) {
		super();
		this.orderItemNumber = orderItemNumber;
		this.stockItemNumber = stockItemNumber;
		this.numberOfItems = numberOfItems;
		this.poNumber = poNumber;
		this.stockItem = stockItem;
	}
	
	public int getOrderItemNumber() {
		return orderItemNumber;
	}

	public void setOrderItemNumber(int orderItemNumber) {
		this.orderItemNumber = orderItemNumber;
	}

	public int getStockItemNumber() {
		return stockItemNumber;
	}

	public void setStockItemNumber(int stockItemNumber) {
		this.stockItemNumber = stockItemNumber;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public int getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(int poNumber) {
		this.poNumber = poNumber;
	}

	public StockItem getStockItem() {
		return stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
	}
	
	public double getTotal() {
		return this.getNumberOfItems()*this.getStockItem().getItemPrice();
	}
	
}
