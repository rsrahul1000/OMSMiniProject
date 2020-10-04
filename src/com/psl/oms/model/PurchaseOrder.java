package com.psl.oms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class PurchaseOrder {
	private int poNumber;
	private int customerNo;
	private LocalDate orderDate;
	private LocalDate shipDate;
	private boolean shipped = false;
	private List<OrderItem> oiList = new ArrayList<>();;
	
	public PurchaseOrder() {
		super();
	}

	public PurchaseOrder(int customerNo, LocalDate orderDate) {
		super();
		//this.poNumber = poNumber;
		this.customerNo = customerNo;
		this.orderDate = orderDate;
		this.shipDate = orderDate.plusDays(4);
		this.shipped = false;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [poNumber=" + poNumber + ", customerNo=" + customerNo + ", orderDate=" + orderDate
				+ ", shipDate=" + shipDate + ", shipped=" + shipped + "]";
	}

	public int getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(int poNumber) {
		this.poNumber = poNumber;
	}

	public int getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(int customerNo) {
		this.customerNo = customerNo;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDate getShipDate() {
		return shipDate;
	}

	public void setShipDate(LocalDate shipDate) {
		this.shipDate = shipDate;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public List<OrderItem> getOiList() {
		return oiList;
	}

	public void setOiList(List<OrderItem> oiList) {
		this.oiList = oiList;
	}
}
