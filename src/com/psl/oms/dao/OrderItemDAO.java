package com.psl.oms.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.psl.oms.model.OrderItem;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.model.StockItem;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class OrderItemDAO {
	private static final Logger logger = Logger.getLogger(StockItemDAO.class.getName());
	Connection con = ConnectionManager.getConnection();

	static {
		try {
			File dir = new File("logs/");
			if(!dir.exists())
				dir.mkdirs();
			logger.addHandler(new FileHandler("logs/omsLogOrderItemDAO"));
			logger.setUseParentHandlers(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	public int getLastInsertID() {
		int key = -1;
		String qry = "SELECT LAST_INSERT_ID()";
		try (PreparedStatement pstmt = con.prepareStatement(qry);) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				key = rs.getInt(1);
			logger.info("Order Item Primary Key = " + key);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return key;
	}
	
	public void addOrderItem(OrderItem orderItem) {
		String qry = "INSERT IGNORE INTO `orderitem` (`stock_item_number`, `number_of_items`, `po_number`) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, orderItem.getStockItemNumber());
			pstmt.setInt(2, orderItem.getNumberOfItems());
			pstmt.setInt(3, orderItem.getPoNumber());
			int i = pstmt.executeUpdate();
			System.out.println(i + " records inserted...");
			logger.info("Order Item saved successfully. Order Item details = " + orderItem);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isOrderPresent(OrderItem orderItem) {
		String qry = "SELECT order_item_number FROM orderitem oi "
				+ "INNER JOIN purchaseorder po ON oi.po_number = po.po_number "
				+ "WHERE po.is_shipped=0 AND oi.stock_item_number=? AND po.po_number=?";
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, orderItem.getStockItemNumber());
			pstmt.setInt(2, orderItem.getPoNumber());
			ResultSet rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst())
				return false;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public void updateExisitngOrder(OrderItem orderItem) {
		String qry = "UPDATE orderitem SET number_of_items=number_of_items+? "
				+ "WHERE stock_item_number=? AND po_number=?";
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, orderItem.getNumberOfItems());
			pstmt.setInt(2, orderItem.getStockItemNumber());
			pstmt.setInt(3, orderItem.getPoNumber());
			int i = pstmt.executeUpdate();
			System.out.println(i + " records updated...");
			logger.info("Updated the Repeated Order Item Quantity Successfully");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public PurchaseOrder getCustomerPurchaseOrders(int purchaseOrderNumber) {
//		String qry1 = "SELECT si.item_description, oi.number_of_items, si.item_price, (oi.number_of_items*si.item_price) AS SubTotal "
//				+ "FROM orderitem oi INNER JOIN stockitems si ON si.stock_item_number = oi.stock_item_number "
//				+ "where oi.po_number=?";
		String qry = "SELECT po.po_number, po.customer_no, po.order_date, po.ship_date, po.is_shipped, oi.order_item_number, oi.stock_item_number, oi.number_of_items, si.item_description, si.item_price "
				+ "FROM purchaseorder po " + "INNER JOIN orderitem oi ON oi.po_number = po.po_number "
				+ "INNER JOIN stockitems si ON si.stock_item_number = oi.stock_item_number " + "WHERE po.po_number=?";
		PurchaseOrder purchaseOrder = null;
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, purchaseOrderNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.isBeforeFirst()) {
				purchaseOrder = new PurchaseOrder();
				while (rs.next()) {
					purchaseOrder.setPoNumber(rs.getInt(1));
					purchaseOrder.setCustomerNo(rs.getInt(2));
					purchaseOrder.setOrderDate(rs.getDate(3).toLocalDate());
					purchaseOrder.setShipDate(rs.getDate(4).toLocalDate());
					purchaseOrder.setShipped(rs.getBoolean(5));
					purchaseOrder.getOiList().add(new OrderItem(rs.getInt(6), rs.getInt(7), rs.getInt(8),
							purchaseOrderNumber, new StockItem(rs.getInt(6), rs.getString(9), rs.getInt(10))));
				}
				logger.info("Purchase Order information with Order Items for Bill Generation Loaded Successfully");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return purchaseOrder;
	}
}
