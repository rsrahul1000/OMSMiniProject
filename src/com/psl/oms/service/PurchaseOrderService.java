package com.psl.oms.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.psl.oms.dao.PurchaseOrderDAO;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class PurchaseOrderService {
	PurchaseOrderDAO poDAO = new PurchaseOrderDAO();

	public List<PurchaseOrder> getPurchaseOrder(int customerNo) {
		return poDAO.getPurchaseOrderByCustomerNo(customerNo);
	}

	public int addPurchaseOrder(PurchaseOrder purchaseOrder) {
		int key = 0;
		try {
			poDAO.getCon().setAutoCommit(false);
			poDAO.addPurchaseOrder(purchaseOrder);
			poDAO.getCon().commit();
			key = poDAO.getLastInsertID();
			poDAO.getCon().commit();
			poDAO.getCon().setAutoCommit(true);
		} catch (SQLException e1) {
			try {
				ConnectionManager.getConnection().rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
		return key;
	}

	public List<PurchaseOrder> getPurchaseOrderBetweenDates(LocalDate fromDate, LocalDate toDate) {
		return poDAO.getPurchaseOrderByFromAndToDate(fromDate, toDate);
	}

	public List<PurchaseOrder> getPurchaseOrderByDate(LocalDate date) {
		return poDAO.getPurchaseOrderByDate(date);
	}

	public List<PurchaseOrder> getPurchaseOrderByDate(LocalDate date, int customerNo) {
		return poDAO.getPurchaseOrderByDate(date, customerNo);
	}

	public List<PurchaseOrder> getDelayedOrders() {
		return poDAO.getDelayedOrders();
	}

	public void shipOrderOf(int poNumber) {
		poDAO.shipOrder(poNumber);
	}

	public List<Entry<Integer, Integer>> getTotalShippedOrderMonthly(int year) {
		if (year > 0 && year <= LocalDate.now().getYear())
			return poDAO.getTotalShippedOrderMonthly(year);
		else
			System.out.println("Please Enter a valid Year!");
		return null;
	}

	public List<Entry<Integer, Double>> getTotalAmountCollectedMonthly(int year) {
		if (year > 0 && year <= LocalDate.now().getYear())
			return poDAO.getTotalAmountCollectedMonthly(year);
		else
			System.out.println("Please Enter a valid Year!");
		return null;
	}

	public List<PurchaseOrder> getAllPurchaseOrders() {
		return poDAO.getAllPurchaseOrders();
	}
}
