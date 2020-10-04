package com.psl.oms.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Locale;

import com.psl.oms.dao.CustomerDAO;
import com.psl.oms.dao.OrderItemDAO;
import com.psl.oms.dao.StockItemDAO;
import com.psl.oms.model.Customer;
import com.psl.oms.model.OrderItem;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class OrderItemService {
	OrderItemDAO oiDAO = new OrderItemDAO();
	CustomerDAO custDAO = new CustomerDAO();

	public void addOrderItem(OrderItem orderItem) {
		try {
			StockItemDAO stockDAO = new StockItemDAO();
			int quantity = stockDAO.getQuantity(orderItem.getStockItemNumber());
			if (quantity - orderItem.getNumberOfItems() < 0)
				throw new Exception("Order Unsuccessful, Insufficient Stock Quantity!");
			oiDAO.getCon().setAutoCommit(false);
			if (!oiDAO.isOrderPresent(orderItem)) {
				oiDAO.addOrderItem(orderItem);
				oiDAO.getCon().commit();
			} else {
				oiDAO.updateExisitngOrder(orderItem);
				oiDAO.getCon().commit();
			}
			stockDAO.updateStockQuantity(orderItem.getStockItemNumber(), quantity - orderItem.getNumberOfItems());
			stockDAO.getCon().commit();
			oiDAO.getCon().setAutoCommit(true);
		} catch (Exception e1) {
			try {
				ConnectionManager.getConnection().rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
	}

	public void generateBillForCustomerPurchaseOrder(int customerNo, int purchaseOrderNumber) {

		Customer customer = custDAO.getCustomerById(customerNo);
		File dir = new File("bills/" + customerNo);
		File file = new File(dir, purchaseOrderNumber + ".txt");
		if (!dir.exists())
			dir.mkdirs();
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try (PrintWriter out = new PrintWriter(file)) {
			if (customer == null)
				throw new Exception("Invalid Customer Number");
			PurchaseOrder purchaseOrder = oiDAO.getCustomerPurchaseOrders(purchaseOrderNumber);
			if (purchaseOrder == null)
				throw new Exception("Invalid Purchase Order Number");

			out.print("=========================================================");
			out.printf(Locale.US, "\r\n Invoice for Customer Number : %d\r\n", customerNo);
			out.print("=========================================================");
			out.printf(Locale.US, "\r\n:::::::::::::::::::: Billing Address :::::::::::::::::::: \r\n");
			out.print("---------------------------------------------------------");

			out.printf(Locale.US, "\r\n %-14s: %s", "Name", customer.getName());
			out.printf(Locale.US, "\r\n %-14s: %s", "Street", customer.getStreet());
			out.printf(Locale.US, "\r\n %-14s: %s", "City", customer.getCity());
			out.printf(Locale.US, "\r\n %-14s: %s", "State", customer.getState());
			out.printf(Locale.US, "\r\n %-14s: %s", "Zip", customer.getZip());
			out.printf(Locale.US, "\r\n Cell Phone No.: %s", customer.getCellPhone());
			if (customer.getHomePhone() != null)
				out.printf(Locale.US, "\r\n Home Phone No.: %s", customer.getHomePhone());
			if (customer.getWorkPhone() != null)
				out.printf(Locale.US, "\r\n Work Phone No.: %s", customer.getWorkPhone());
			out.print("\r\n\r\n=========================================================");
			out.printf(Locale.US, "\r\n Purchase Order Number : %d\r\n", purchaseOrderNumber);
			out.print("=========================================================");
			out.println();
			out.printf(Locale.US, " Order Date: %s\r\n", purchaseOrder.getOrderDate());
			out.printf(Locale.US, " Ship Date: %s\r\n", purchaseOrder.getShipDate());
			// out.print("\r\n-------------------------Details-------------------------\r\n");
			out.print("\r\n===================== Order Details =====================\r\n");
			out.printf(Locale.US, " %-6s%-19s%-10s%-10s%-10s\r\n", "SNo.", "Description", "Quantity", "Price",
					"SubTotal");
			out.print("---------------------------------------------------------\r\n");
			double sum = 0;
			int sno = 0;
			for (OrderItem i : purchaseOrder.getOiList()) {
				if (i.getStockItem() != null) {
					sum += i.getTotal();
					out.printf(Locale.US, " %-6s%-19s%-10s%-10s%-10s\n", ++sno, i.getStockItem().getItemDescription(),
							i.getNumberOfItems(), i.getStockItem().getItemPrice(), i.getTotal());
				}
			}
			out.print("---------------------------------------------------------\r\n");
			out.printf(Locale.US, " %-45s%-10.1f\n", "Total Sum", sum);
			// out.print("---------------------------------------------------------");
			out.print("=========================================================");
			System.out.println("Bill Generated Successfully for customer Number " + customerNo
					+ " with Purchase Order Number " + purchaseOrderNumber);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
