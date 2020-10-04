package com.psl.oms.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.psl.oms.model.Customer;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class CustomerDAO {
	private static final Logger logger = Logger.getLogger(CustomerDAO.class.getName());
	Connection con = ConnectionManager.getConnection();

	static {
		try {
			File dir = new File("logs/");
			if(!dir.exists())
				dir.mkdirs();
			logger.addHandler(new FileHandler("logs/omsLogCustomerDAO"));
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
			logger.info("Customer Primary Key = " + key);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return key;
	}

	public void addCustomer(Customer customer) {
		try (PreparedStatement pstmt = con.prepareStatement(
				"insert IGNORE  into customer (name, home_phone, cell_phone, work_phone, street, city, state, zip) "
						+ "values(?, ?, ?, ?, ?, ?, ?, ?)")) {
			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getHomePhone());
			pstmt.setString(3, customer.getCellPhone());
			pstmt.setString(4, customer.getWorkPhone());
			pstmt.setString(5, customer.getStreet());
			pstmt.setString(6, customer.getCity());
			pstmt.setString(7, customer.getState());
			pstmt.setString(8, customer.getZip());
			int i = pstmt.executeUpdate();
			System.out.println(i + " records inserted...");
			logger.info("Customer saved successfully. Customer details = " + customer);
		} catch (SQLException e1) {
			e1.printStackTrace();
			// System.out.println(e1.getMessage());
		}
	}

	public List<Customer> getAllCustomers() {
		String qry = "select * from customer";
		List<Customer> custList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Customer c = new Customer();
				c.setCustomerNo(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setHomePhone(rs.getString(3));
				c.setCellPhone(rs.getString(4));
				c.setWorkPhone(rs.getString(5));
				c.setStreet(rs.getString(6));
				c.setCity(rs.getString(7));
				c.setState(rs.getString(8));
				c.setZip(rs.getString(9));
				custList.add(c);
			}
			logger.info("Customer List:: " + custList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return custList;
	}

	public Customer getCustomerById(int customerNo) {
		String qry = "select * from customer where customer_no=?";
		Customer c = null;
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, customerNo);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				c = new Customer();
				c.setCustomerNo(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setHomePhone(rs.getString(3));
				c.setCellPhone(rs.getString(4));
				c.setWorkPhone(rs.getString(5));
				c.setStreet(rs.getString(6));
				c.setCity(rs.getString(7));
				c.setState(rs.getString(8));
				c.setZip(rs.getString(9));
				logger.info("Customer loaded successfully. Customer details = " + c);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return c;
	}

	public List<Integer> getCustomerByMaxPurchaseOrder() {
		// String qry = "SELECT customer_no FROM purchaseorder GROUP BY customer_no
		// ORDER BY COUNT(*) DESC LIMIT 1";
		String qry = "SELECT DISTINCT P.customer_no " + "FROM `purchaseorder` P" + " WHERE P.customer_no"
				+ " IN (SELECT customer_no FROM `purchaseorder` " + "GROUP BY customer_no "
				+ "  HAVING COUNT(*) = (SELECT COUNT(*) " + "FROM `purchaseorder` PO" + "  GROUP BY PO.customer_no "
				+ "ORDER BY COUNT(*) DESC " + "  LIMIT 1))";
		List<Integer> custNoMaxOrderList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				custNoMaxOrderList.add(rs.getInt(1));
//				c = new Customer();
//				c.setCustomerNo(rs.getInt(1));
//				c.setName(rs.getString(2));
//				c.setHomePhone(rs.getString(3));
//				c.setCellPhone(rs.getString(4));
//				c.setWorkPhone(rs.getString(5));
//				c.setStreet(rs.getString(6));
//				c.setCity(rs.getString(7));
//				c.setState(rs.getString(8));
//				c.setZip(rs.getString(9));
				logger.info("Customer Number with maximum orders loaded successfully. Customer details = "
						+ custNoMaxOrderList);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return custNoMaxOrderList;
	}

}
