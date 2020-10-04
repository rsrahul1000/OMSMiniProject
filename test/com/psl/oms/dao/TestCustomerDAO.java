package com.psl.oms.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.psl.oms.model.Customer;
import com.psl.oms.util.ConnectionManager;

public class TestCustomerDAO {

	CustomerDAO target = null;
	static Connection cn = null;
	Customer c = null;

	private int getRecordCountMethod() throws SQLException {
		Statement stmt = cn.createStatement();
		ResultSet rs = stmt.executeQuery("Select count(*) from customer");
		rs.next();
		return rs.getInt(1);

	}
	
	private int deleteTestData() throws SQLException {
		Statement stmt = cn.createStatement();
		int i = stmt.executeUpdate("DELETE FROM `customer` WHERE `name`='"+c.getName()+"'");
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
		target = new CustomerDAO();
		c =  new Customer("Test Name", "9999999999", "999999999", "9999999999", "Test Street",
				"Test City", "Test State", "999999");
	}

	@After
	public void tearDown() throws Exception {
		target = null;
		deleteTestData();
		c = null;
	}

	@Test
	public void testAddCustomer() throws SQLException {
		int totalRecBefore = getRecordCountMethod() + 1;
		target.addCustomer(c);
		int totalRecAfter = getRecordCountMethod();
		assertTrue("Record size not updated", totalRecAfter == totalRecBefore);		
	}

	@Test
	public void testGetAllCustomers() throws SQLException {
		List<Customer> custList = target.getAllCustomers();
		assertNotNull(custList);
		assertTrue("Fetched Wrong Number of Customers", custList.size()==getRecordCountMethod());
	}

}
