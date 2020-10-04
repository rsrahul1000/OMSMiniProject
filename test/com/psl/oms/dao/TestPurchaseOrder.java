package com.psl.oms.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.util.ConnectionManager;

public class TestPurchaseOrder {

	PurchaseOrderDAO target = null;
	static Connection cn = null;
	PurchaseOrder po = null;
	
	private int getRecordCountMethod() throws SQLException {
		Statement stmt = cn.createStatement();
		ResultSet rs = stmt.executeQuery("Select count(*) from purchaseorder");
		rs.next();
		return rs.getInt(1);

	}
	
	private int deleteTestData() throws SQLException {
		Statement stmt = cn.createStatement();
		int i = stmt.executeUpdate("DELETE FROM `purchaseorder` WHERE `po_number`="+po.getPoNumber());
		return i;
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cn = ConnectionManager.getConnection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManager.CloseConnection();
	}

	@Before
	public void setUp() throws Exception {
		target = new PurchaseOrderDAO();
		po = new PurchaseOrder(1, LocalDate.now());
	}

	@After
	public void tearDown() throws Exception {
		target = null;
		deleteTestData();
		po = null;
	}

	@Test
	public void testAddPurchaseOrder() throws SQLException {
		int totalRecBefore = getRecordCountMethod() + 1;
		target.addPurchaseOrder(po);
		po.setPoNumber(target.getLastInsertID());
		int totalRecAfter = getRecordCountMethod();
		assertTrue("Record size not updated", totalRecAfter == totalRecBefore);	
	}

	@Test
	public void testGetAllPurchaseOrders() throws SQLException {
		List<PurchaseOrder> purchaseOrderList = target.getAllPurchaseOrders();
		assertNotNull(purchaseOrderList);
		assertTrue("Fetched Wrong Number of Customers", purchaseOrderList.size()==getRecordCountMethod());
	}

}
