package com.psl.oms.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.psl.oms.model.Customer;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class PurchaseOrderDAO {
	private static final Logger logger = Logger.getLogger(PurchaseOrderDAO.class.getName());
	Connection con = ConnectionManager.getConnection();

	static {
		try {
			File dir = new File("logs/");
			if(!dir.exists())
				dir.mkdirs();
			logger.addHandler(new FileHandler("logs/omsLogPurchaseOrderDAO"));
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
			logger.info("Purchase Order Primary Key = " + key);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return key;
	}

	public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
		try (PreparedStatement pstmt = con.prepareStatement(
				"insert ignore into purchaseorder (customer_no, order_date, ship_date, is_shipped) values(?, ?, ?, ?)")) {
			pstmt.setInt(1, purchaseOrder.getCustomerNo());
			pstmt.setDate(2, Date.valueOf(purchaseOrder.getOrderDate())); // Date.valueOf(LocalDate.now()));
			pstmt.setDate(3, Date.valueOf(purchaseOrder.getShipDate())); // Date.valueOf(LocalDate.now().plusDays(4)));
			pstmt.setBoolean(4, purchaseOrder.isShipped());
			int i = pstmt.executeUpdate();
			System.out.println(i + " records inserted...");
			logger.info("Purchase Order saved successfully. Purchase Order details = " + purchaseOrder);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public List<PurchaseOrder> getPurchaseOrderByCustomerNo(int customerNo) {
		String qry = "select * from purchaseorder where customer_no=?";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, customerNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
				logger.info("Purchase Order List:: " + p);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

	// 6.a. fetch all orders placed between from and to date inclusive of both date
	public List<PurchaseOrder> getPurchaseOrderByFromAndToDate(LocalDate fromDate, LocalDate toDate) {
		String qry = "select * from purchaseorder where order_date between ? and ?";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setDate(1, Date.valueOf(fromDate));
			pstmt.setDate(2, Date.valueOf(toDate.plusDays(1))); // for inclusive of both dates
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
			}
			logger.info("Purchase Order List from " + fromDate + " to " + toDate + " :: " + poList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

	// 6.b. Fetch all orders placed for given date
	public List<PurchaseOrder> getPurchaseOrderByDate(LocalDate date) {
		String qry = "select * from purchaseorder where order_date=?";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setDate(1, Date.valueOf(date));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
			}
			logger.info("Purchase Order List for Date " + date + " :: " + poList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

	// Fetch all orders placed for given date for given customer
	public List<PurchaseOrder> getPurchaseOrderByDate(LocalDate date, int customerNo) {
		String qry = "select * from purchaseorder where customer_no=? and order_date=?";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, customerNo);
			pstmt.setDate(2, Date.valueOf(date));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
			}
			logger.info("Purchase Order List for Date " + date + " for CustomerNo " + customerNo + " :: " + poList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

	// 8. Fetch Delayed Orders
	public List<PurchaseOrder> getDelayedOrders() {
		String qry = "select * from purchaseorder where ship_date<? and is_shipped=0";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setDate(1, Date.valueOf(LocalDate.now()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
			}
			logger.info("List of Delayed Orders :: " + poList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

	public void shipOrder(int poNumber) {
		String qry = "UPDATE purchaseorder SET is_shipped=1 , ship_date=? WHERE po_number=?";
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setDate(1, Date.valueOf(LocalDate.now()));
			pstmt.setInt(2, poNumber);
			int i = pstmt.executeUpdate();
			System.out.println(i + " records Updated...");
			logger.info("Purchase Order Shipped successfully");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public List<Entry<Integer, Integer>> getTotalShippedOrderMonthly(int year) {
		String qry = "SELECT MONTH(ship_date),  COUNT(ship_date) FROM `purchaseorder` WHERE YEAR(ship_date) = ? AND is_shipped=1 GROUP BY MONTH(ship_date)";
		List<Entry<Integer, Integer>> totalShippedOrderMonthly = new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, year);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt(1), rs.getInt(2));

			}
			totalShippedOrderMonthly.addAll(map.entrySet());
			logger.info("Total Shipped Orders Monthly :: " + totalShippedOrderMonthly);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return totalShippedOrderMonthly;
	}

	public List<Entry<Integer, Double>> getTotalAmountCollectedMonthly(int year) {
		String qry = "SELECT MONTH(po.order_date) AS order_month, SUM(oi.number_of_items*si.item_price) AS total_amount_collected  FROM purchaseorder po "
				+ "INNER JOIN orderitem oi ON po.po_number = oi.po_number "
				+ "INNER JOIN stockitems si ON oi.stock_item_number = si.stock_item_number "
				+ "WHERE YEAR(po.order_date)=? " + "GROUP BY MONTH(po.order_date)";
		List<Entry<Integer, Double>> totalAmountCollectedMonthly = new ArrayList<>();
		Map<Integer, Double> map = new HashMap<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, year);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt(1), rs.getDouble(2));

			}
			totalAmountCollectedMonthly.addAll(map.entrySet());
			logger.info("Total Amount Collectes Monthly :: " + totalAmountCollectedMonthly);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return totalAmountCollectedMonthly;
	}

	public List<PurchaseOrder> getAllPurchaseOrders() {
		String qry = "select * from purchaseorder";
		List<PurchaseOrder> poList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseOrder p = new PurchaseOrder();
				p.setPoNumber(rs.getInt(1));
				p.setCustomerNo(rs.getInt(2));
				p.setOrderDate(rs.getDate(3).toLocalDate());
				p.setShipDate(rs.getDate(4).toLocalDate());
				p.setShipped(rs.getBoolean(5));
				poList.add(p);
			}
			logger.info("List of Delayed Orders :: " + poList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return poList;
	}

}
