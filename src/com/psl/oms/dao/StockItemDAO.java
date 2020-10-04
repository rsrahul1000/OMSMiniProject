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

import com.psl.oms.model.StockItem;
import com.psl.oms.model.Unit;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class StockItemDAO {
	private static final Logger logger = Logger.getLogger(StockItemDAO.class.getName());
	Connection con = ConnectionManager.getConnection();

	static {
		try {
			File dir = new File("logs/");
			if(!dir.exists())
				dir.mkdirs();
			logger.addHandler(new FileHandler("logs/omsLogStockItemDAO"));
			logger.setUseParentHandlers(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	public void addStockItem(StockItem stockItem) {
		try (PreparedStatement pstmt = con.prepareStatement(
				"insert ignore into stockitems (item_description, item_price, unit, quantity) values(?, ?, ?, ?)")) {
			pstmt.setString(1, stockItem.getItemDescription());
			pstmt.setDouble(2, stockItem.getItemPrice());
			pstmt.setString(3, stockItem.getUnit().toString());
			pstmt.setInt(4, stockItem.getQuantity());
			int i = pstmt.executeUpdate();
			System.out.println(i + " records inserted...");
			logger.info("Stock Item saved successfully. Stock Item details = " + stockItem);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public List<StockItem> getAllStockItems() {
		String qry = "select * from stockitems";
		List<StockItem> stocktList = new ArrayList<>();
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				StockItem stock = new StockItem();
				stock.setItemNumber(rs.getInt(1));
				stock.setItemDescription(rs.getString(2));
				stock.setItemPrice(rs.getDouble(3));
				stock.setUnit(Unit.valueOf(rs.getString(4)));
				stock.setQuantity(rs.getInt(5));
				stocktList.add(stock);
			}
			logger.info("Stock Item List:: " + stocktList);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return stocktList;
	}

	public int getQuantity(int stockItemNumber) {
		String qry = "select quantity from stockitems where stock_item_number=?";
		int quantity = 0;
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, stockItemNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				quantity = rs.getInt(1);
			}
			logger.info("Quantity for Stock Item " + stockItemNumber + " :: " + quantity);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return quantity;
	}

	public void updateStockQuantity(int stockItemNumber, int newQuantity) {
		String qry = "UPDATE `stockitems` SET `quantity`=? WHERE `stock_item_number`=?";
		try (PreparedStatement pstmt = con.prepareStatement(qry)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setInt(2, stockItemNumber);
			int i = pstmt.executeUpdate();
			System.out.println(i + " records updated...");
			logger.info("Quantity Updated for Stock Item " + stockItemNumber + " :: " + newQuantity);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
