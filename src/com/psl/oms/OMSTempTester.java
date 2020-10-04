package com.psl.oms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.psl.oms.model.Customer;
import com.psl.oms.model.OrderItem;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.model.StockItem;
import com.psl.oms.model.Unit;
import com.psl.oms.service.CustomerService;
import com.psl.oms.service.OrderItemService;
import com.psl.oms.service.PurchaseOrderService;
import com.psl.oms.service.StockItemService;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class OMSTempTester {

	void storeStockItemsFromFile(File file) {
		StockItemService stockService = new StockItemService();
		try (FileReader in = new FileReader(file); BufferedReader reader = new BufferedReader(in);) {
			int data;
			if(!file.exists())
				throw new Exception("Stock File Does not exists");
			String line = null;
			List<StockItem> stockItems = new ArrayList<StockItem>();
			while ((line = reader.readLine()) != null) {
				String[] stockDetails = line.split(",");
				StockItem s = new StockItem();
				s.setItemNumber(Integer.parseInt(stockDetails[0]));
				s.setItemDescription(stockDetails[1]);
				s.setItemPrice(Double.parseDouble(stockDetails[2]));
				s.setUnit(Unit.valueOf(stockDetails[3]));
				s.setQuantity(Integer.parseInt(stockDetails[4]));
				stockItems.add(s);
			}
			stockService.addStockItem(stockItems);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		CustomerService custService = new CustomerService();
		System.out.println(custService.getCustomer(103));
		System.out.println(custService.getAllCustomers());
		OMSTempTester test = new OMSTempTester();
		test.storeStockItemsFromFile(new File("stock.txt"));

		// 1. Add Customer
		System.out.println("\n\n1. Add Customer");
		Customer customer = new Customer("Prerna Desai", "9564325978", "9654875321", "2229747452", "99, HarbhajanGunj",
				"Srinagar", "Jammu and Kashmir", "297490");
		int customerNo = custService.addCustomer(customer);
		System.out.println(customerNo);
		customer.setCustomerNo(customerNo);

//		// 2. Add Purchase Order
//		System.out.println("\n\n2. Purchase Order");
		PurchaseOrderService poService = new PurchaseOrderService();
		PurchaseOrder purchaseOrder = new PurchaseOrder(103, LocalDate.parse("2020-08-24"));
		int poNumber = poService.addPurchaseOrder(purchaseOrder);
		purchaseOrder.setPoNumber(poNumber);
		customer.getPoList().add(purchaseOrder);
//		
//		// Add Order item for purchase order
		OrderItemService oiService = new OrderItemService();
		OrderItem orderItem = new OrderItem(36, 2, poNumber);
		oiService.addOrderItem(orderItem);

		// 3. Add New Stock Item
		System.out.println("\n\n3. Add New Stock Item");
		StockItemService stockService = new StockItemService();
		StockItem s = new StockItem();
		s.setItemDescription("Maggie");
		s.setItemPrice(24);
		s.setUnit(Unit.valueOf("NUMBERS"));
		s.setQuantity(50);
		stockService.addStockItem(s);

		// 4. Fetch Customers based on ID
		System.out.println("\n\n4. Fetch Customers based on ID");
		System.out.println(custService.getCustomer(103)); //customer.getCustomerNo()));

		// 5. Fetch Orders placed by specific customers
		System.out.println("\n\n5. Fetch Orders placed by specific customers");
		System.out.println(poService.getPurchaseOrder(103)); //customer.getCustomerNo()));

		// 6.a. fetch all orders placed between from and to date inclusive of both date
		System.out.println("\n\n6.a. fetch all orders placed between from and to date inclusive of both date ");
		System.out.println(poService.getPurchaseOrderBetweenDates(LocalDate.parse("2020-04-01"), LocalDate.now()));

		// 6.b. fetch all the orders placed for given date
		System.out.println("\n\n6.b. fetch all the orders placed for given date ");
		System.out.println(poService.getPurchaseOrderByDate(LocalDate.parse("2020-08-24")));

		// 7. Update order status to ship and update ship date based on order id.
//		System.out.println("\n\n7. Update order status to ship and update ship date based on order id.");
//		poService.shipOrderOf(purchaseOrder.getPoNumber());

		// 8. Fetch delayed orders
		System.out.println("\n\n8. Fetch delayed orders ");
		System.out.println(poService.getDelayedOrders());

		// 9. Fetch All stock Items
		System.out.println("\n\n9. Fetch All stock Items");
		System.out.println(stockService.getAllStockItems());

		// 10. Find month-wise total orders shipped.
		System.out.println("\n\n10. Find month-wise total orders shipped.");
		List<Entry<Integer, Integer>> totalShippedOrderMonthy = poService.getTotalShippedOrderMonthly(2020);
		System.out.printf("%-10s%-10s\n", "Month", "Shipped Order");
		for (Entry<Integer, Integer> entry : totalShippedOrderMonthy) {
			System.out.printf("%-10d%-10d\n", entry.getKey(), entry.getValue());
		}

		// 11. Find the total amount collected based on months
		System.out.println("\n\n11. Find the total amount collected based on months");
		List<Entry<Integer, Double>> totalAmountCollectedMonthly = poService.getTotalAmountCollectedMonthly(2020);
		System.out.printf("%-10s%-13s\n", "Month", "Amount Collected");
		for (Entry<Integer, Double> entry : totalAmountCollectedMonthly) {
			System.out.printf("%-10d%-10.2f\n", entry.getKey(), entry.getValue());
		}

		// 12. Find the customer who has made maximum orders.
		System.out.println("\n\n12. Find the customer who has made maximum orders.");
		List<Integer> custMaxOrder = custService.getCustomerByMaxPurchaseOrder();
		System.out.println("Customer Number with Max order: ");
		custMaxOrder.forEach((c) -> System.out.println(c));
		for (int c : custMaxOrder) {
			System.out.println(custService.getCustomer(c));
		}
		
		//13. Generate bill for customer for specific order. 
		System.out.println("13. Generate bill for customer for specific order. ");
		oiService.generateBillForCustomerPurchaseOrder(103, 1/* purchase order number */);
		oiService.generateBillForCustomerPurchaseOrder(103, 10);
		oiService.generateBillForCustomerPurchaseOrder(104, 2);
		oiService.generateBillForCustomerPurchaseOrder(10, 2);
		oiService.generateBillForCustomerPurchaseOrder(104, 285);
	}
}
