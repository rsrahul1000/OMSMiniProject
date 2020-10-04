package com.psl.oms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.psl.oms.model.Customer;
import com.psl.oms.model.OrderItem;
import com.psl.oms.model.PurchaseOrder;
import com.psl.oms.model.StockItem;
import com.psl.oms.model.Unit;
import com.psl.oms.service.CustomerService;
import com.psl.oms.service.OrderItemService;
import com.psl.oms.service.PurchaseOrderService;
import com.psl.oms.service.StockItemService;
import com.psl.oms.util.ConnectionManager;

/**
 * @author Rahul Sharma (rsrahul1000@gmail.com)
 *
 */
public class OMSTester {

	// Declaring The Model Classes
	static Customer customer = null;
	static PurchaseOrder purchaseOrder = null;
	static StockItem stockItem = null;
	static DateFormatSymbols dfs = new DateFormatSymbols();

	// Declaring Client Side Lists
	static List<Customer> custList = null;
	static List<StockItem> stockItemList = null;
	static List<PurchaseOrder> purchaseOrderList = null;
	static Set<Integer> purchaseOrderSet = null;

	// Initializing Service Classes Required
	static CustomerService custService = new CustomerService();
	static PurchaseOrderService poService = new PurchaseOrderService();
	static OrderItemService oiService = new OrderItemService();
	static StockItemService stockService = new StockItemService();

	static Scanner s = new Scanner(System.in);
	static int purchOrderForBill = 0;

	public static void main(String[] args) {
		OMSTester omsTester = new OMSTester();
		int year = 0;
		int choice = 0, customerNo = 0;
		do {
			System.out.println("\n\n==================================================");
			System.out.println("::::::: Welcome to Order Management System :::::::");
			System.out.println("==================================================");
			System.out.println("Choose the following Option:");
			System.out.println("0.  Populate Customer And Stock Items Table From File");
			System.out.println("1.  Add New Customer");
			System.out.println("2.  Add New Purchase Order");
			System.out.println("3.  Add New Stock Item");
			System.out.println("4.  Display Customer By ID");
			System.out.println("5.  Display Orders For Customer");
			System.out.println("6.  Display orders placed for specific duration");
			System.out.println("7.  Update Order Status and Ship Date");
			System.out.println("8.  Display Delayed Orders");
			System.out.println("9.  Display All Stock Items");
			System.out.println("10. Display Monthwise Total Orders Shipped");
			System.out.println("11. Display Total Amount Collected Monthly");
			System.out.println("12. Display Customer with Maximum Orders");
			System.out.println("13. Generate Bill");
			System.out.println("14. Exit");
			System.out.print("\nEnter the option: ");
			choice = s.nextInt();
			switch (choice) {
			case 0:
				customerAndStockItemPopulateDBFromFile();
				break;
			case 1:
				s.nextLine();
				customer = new Customer();
				System.out.println("\n:::: Enter details of the New Customer ::::");
				System.out.println("-------------------------------------------");
				System.out.print("Name          : ");
				customer.setName(s.nextLine());
				System.out.print("Home Phone No.: ");
				customer.setHomePhone(s.nextLine());
				System.out.print("Cell Phone No.: ");
				customer.setCellPhone(s.nextLine());
				System.out.print("Work Phone No.: ");
				customer.setWorkPhone(s.nextLine());
				System.out.print("Street        : ");
				customer.setStreet(s.nextLine());
				System.out.print("City          : ");
				customer.setCity(s.nextLine());
				System.out.print("State         : ");
				customer.setState(s.nextLine());
				System.out.print("Zip           : ");
				customer.setZip(s.nextLine());
				customerNo = custService.addCustomer(customer);
				customer.setCustomerNo(customerNo);
				System.out.println("Customer No. for " + customer.getName() + " is " + customer.getCustomerNo());
				break;
			case 2:
				addNewPurchaseOrder();
				break;
			case 3:
				s.nextLine();
				try {
					stockItem = new StockItem();
					System.out.println("\n:::: Enter details New Stock Item ::::");
					System.out.println("--------------------------------------");
					System.out.print("Stock Item Description :");
					stockItem.setItemDescription(s.nextLine());
					System.out.print("Stock Item Price       :");
					stockItem.setItemPrice(s.nextDouble());
					s.nextLine();
					System.out.println("Available Units: KG, DOZEN, GALLONS, NUMBERS, GRAMS");
					System.out.print("Stock Item Unit        :");
					stockItem.setUnit(Unit.valueOf(s.nextLine().toUpperCase()));
					System.out.print("Stock Item Quantity    :");
					stockItem.setQuantity(s.nextInt());
					stockService.addStockItem(stockItem);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Unit Value Entered Invalid, Try Again!");
				}
				break;
			case 4:
				fetchCustomerByID();
				if (customer != null)
					displayCustomerDetails();
				break;
			case 5:
				s.nextLine();
				if (customer == null) {
					fetchCustomerByID();
					if (customer != null) {
						purchaseOrderList = poService.getPurchaseOrder(customer.getCustomerNo());
						displayPurchaseOrders("Purchase Orders for Customer No.: " + customer.getCustomerNo());
					}
				} else {
					System.out.print("Fetch Order for Customer No: " + customer.getCustomerNo() + " ? (Y/N): ");
					String addNextOrder = s.nextLine();
					if (addNextOrder.equalsIgnoreCase("Y")) {
						purchaseOrderList = poService.getPurchaseOrder(customer.getCustomerNo());
						displayPurchaseOrders("Purchase Orders for Customer No.: " + customer.getCustomerNo());
					} else if (addNextOrder.equalsIgnoreCase("N")) {
						fetchCustomerByID();
						if (customer != null) {
							purchaseOrderList = poService.getPurchaseOrder(customer.getCustomerNo());
							displayPurchaseOrders("Purchase Orders for Customer No.: " + customer.getCustomerNo());
						}
					} else {
						System.out.println("Please Enter a valid choice");
					}
				}
				break;
			case 6:
				s.nextLine();
				LocalDate startDate, endDate;
				System.out.println("\nChoose Option: ");
				System.out.println("--------------");
				System.out.println("1. Display Orders Between 2 Dates");
				System.out.println("2. Display Order for Given Date");
				System.out.print("\nEnter you Option: ");
				int chooseforSix = s.nextInt();
				try {
					switch (chooseforSix) {
					case 1:
						s.nextLine();
						System.out.print("Enter Start Date (YYYY-MM-DD) : ");
						startDate = LocalDate.parse(s.nextLine());
						System.out.print("Enter End Date   (YYYY-MM-DD) : ");
						endDate = LocalDate.parse(s.nextLine());
						purchaseOrderList = poService.getPurchaseOrderBetweenDates(startDate, endDate);
						displayPurchaseOrders("Purchase Orders From " + startDate + " to " + endDate + ": ");
						break;
					case 2:
						s.nextLine();
						System.out.print("Enter Order Date (YYYY-MM-DD) : ");
						startDate = LocalDate.parse(s.nextLine());
						purchaseOrderList = poService.getPurchaseOrderByDate(startDate);
						displayPurchaseOrders("Purchase Order For " + startDate + " : ");
						break;
					default:
						System.out.println("Enter Valid Option, Try Again!");
					}
				} catch (Exception e) {
					System.out.println("Invalid Date: " + e.getMessage());
				}
				break;
			case 7:
				s.nextLine();
				purchaseOrderList = poService.getAllPurchaseOrders();
				displayPurchaseOrders("Choose Purchase Order to Ship:  ");

				Set<Integer> purchaseOrderSet = purchaseOrderList.stream().map(PurchaseOrder::getPoNumber)
						.collect(Collectors.toCollection(LinkedHashSet::new));
				if (purchaseOrderSet.size() > 0) {
					System.out.print("\nEnter Purchase Order ID: ");
					int choosePurchOrder = s.nextInt();
					s.nextLine();
					if (!purchaseOrderSet.contains(choosePurchOrder)) {
						System.out.println("Entered Purchase Order ID is invalid, Try Again!");
						break;
					}
					poService.shipOrderOf(choosePurchOrder);
					System.out.println("Purchase Order " + choosePurchOrder + " Shipped Successfully.");
				}
				break;
			case 8:
				// Delayed Orders: Orders which are not Shipped and its shipDate < currentDate
				purchaseOrderList = poService.getDelayedOrders();
				displayPurchaseOrders("Delayed Purchase Orders: ");
				break;
			case 9:
				stockItemList = stockService.getAllStockItems();
				displayAllStockItems();
				break;
			case 10:
				System.out.print("Enter Year: ");
				year = s.nextInt();
				List<Entry<Integer, Integer>> totalShippedOrderMonthy = poService.getTotalShippedOrderMonthly(year);
				System.out.println("\n:::: Total Orders Shipped Monthly ::::");
				System.out.println("======================================");
				if (totalShippedOrderMonthy != null && totalShippedOrderMonthy.size() > 0) {
					System.out.printf("%-10s%15s\n", "Month", "Shipped Order");
					System.out.println("-------------------------");
					for (Entry<Integer, Integer> entry : totalShippedOrderMonthy) {
						System.out.printf("%-10s%15d\n", dfs.getMonths()[entry.getKey() - 1], entry.getValue());
					}
				} else {
					System.out.println("No Record Found");
				}
				break;
			case 11:
				System.out.print("Enter Year: ");
				year = s.nextInt();
				List<Entry<Integer, Double>> totalAmountCollectedMonthly = poService
						.getTotalAmountCollectedMonthly(year);
				System.out.println("\n:::: Total Amount Collected Monthly ::::");
				System.out.println("========================================");
				if (totalAmountCollectedMonthly != null && !totalAmountCollectedMonthly.isEmpty()) {
					System.out.printf("%-10s%16s\n", "Month", "Amount Collected");
					System.out.println("--------------------------");
					for (Entry<Integer, Double> entry : totalAmountCollectedMonthly) {
						System.out.printf("%-10s%16.2f\n", dfs.getMonths()[entry.getKey() - 1], entry.getValue());
					}
				} else {
					System.out.println("No Record Found");
				}
				break;
			case 12:
				List<Integer> custMaxOrder = custService.getCustomerByMaxPurchaseOrder();
				System.out.println("\nCustomer Details with Max Purchase Orders: ");
				System.out.print("==============================");
				// custMaxOrder.forEach((c) -> System.out.println(c));
				if (custMaxOrder.size() > 0) {
					for (int c : custMaxOrder) {
						customer = custService.getCustomer(c);
						displayCustomerDetails();
					}
				} else
					System.out.println("\nNo Customers With Max Orders Found!");
				break;
			case 13:
				s.nextLine();
				if (customer != null) {
					System.out.print("\nGenerate Bill for Customer No: " + customer.getCustomerNo() + " ? (Y/N): ");
					String yesOrNo = s.nextLine();
					if (yesOrNo.trim().equalsIgnoreCase("Y")) {
						if (fetchPurchaseOrderAndSelectPO())
							oiService.generateBillForCustomerPurchaseOrder(customer.getCustomerNo(), purchOrderForBill);
					} else if (yesOrNo.trim().equalsIgnoreCase("N")) {
						fetchCustomerByID();
						if (customer != null && fetchPurchaseOrderAndSelectPO()) {
							oiService.generateBillForCustomerPurchaseOrder(customer.getCustomerNo(), purchOrderForBill);
						}
					}
				} else {
					fetchCustomerByID();
					if (customer != null && fetchPurchaseOrderAndSelectPO()) {
						oiService.generateBillForCustomerPurchaseOrder(customer.getCustomerNo(), purchOrderForBill);
					}
				}
				break;
			case 14:
				System.out.println("Thank You");
				System.exit(0);
				break;
			default:
				System.out.println("Please Enter a Valid Choice!");
			}
		} while (choice != 14);

	}

	private static void customerAndStockItemPopulateDBFromFile() {
		try {
			File dir = new File("resources/");
			if (dir.exists()) {
				storeCustomerFromFile(new File("resources/customer.txt"));
				System.out.println("Populating Customers Table in DB Successful");
				storeStockItemsFromFile(new File("resources/stock.txt"));
				System.out.println("Populating Stock Items Table in DB Successful");
			} else {
				System.out.println("File Not present, Cannot Populate DB");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private static void storeStockItemsFromFile(File file) throws Exception {
		StockItemService stockService = new StockItemService();
		try (FileReader in = new FileReader(file); BufferedReader reader = new BufferedReader(in);) {
			if (!file.exists())
				throw new Exception("Stock File Does not exists");
			String line = null;
			List<StockItem> stockItems = new ArrayList<StockItem>();
			while ((line = reader.readLine()) != null) {
				String[] stockDetails = line.split(",");
				StockItem stock = new StockItem();
				stock.setItemNumber(Integer.parseInt(stockDetails[0]));
				stock.setItemDescription(stockDetails[1]);
				stock.setItemPrice(Double.parseDouble(stockDetails[2]));
				stock.setUnit(Unit.valueOf(stockDetails[3]));
				stock.setQuantity(Integer.parseInt(stockDetails[4]));
				stockItems.add(stock);
			}
			stockService.addStockItem(stockItems);
		} catch (Exception e) {
			throw e;
		}
	}

	private static void storeCustomerFromFile(File file) throws Exception {
		try (FileReader in = new FileReader(file); BufferedReader reader = new BufferedReader(in);) {
			if (!file.exists())
				throw new Exception("Stock File Does not exists");
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] customerDetails = line.split(",");
				Customer c = new Customer();
				c.setCustomerNo(Integer.parseInt(customerDetails[0]));
				c.setName(customerDetails[1]);
				c.setHomePhone(customerDetails[2]);
				c.setCellPhone(customerDetails[3]);
				c.setWorkPhone(customerDetails[4]);
				c.setStreet(customerDetails[5]);
				c.setCity(customerDetails[6]);
				c.setState(customerDetails[7]);
				c.setZip(customerDetails[8]);
				custService.addCustomer(c);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// Purchase Order Display and take purchase order ID from user
	private static boolean fetchPurchaseOrderAndSelectPO() {
		purchaseOrderList = poService.getPurchaseOrder(customer.getCustomerNo());
		displayPurchaseOrders("Purchase Orders for Customer No.: " + customer.getCustomerNo());
		purchaseOrderSet = purchaseOrderList.stream().map(PurchaseOrder::getPoNumber)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (purchaseOrderSet.size() > 0) {
			System.out.print("\nEnter Purchase Order ID: ");
			purchOrderForBill = s.nextInt();
			s.nextLine();
			if (!purchaseOrderSet.contains(purchOrderForBill)) {
				System.out.println("Entered Purchase Order ID is invalid, Try Again!");
				return false;
			}
			return true;
		}
		return false;
	}

	// Display All stock Items
	private static void displayAllStockItems() {
		if (stockItemList == null || stockItemList.isEmpty()) {
			System.out.println("No Stock Items available.");
		} else {
			System.out.println("\n::::::::::::::::::: Details of Stock :::::::::::::::::::");
			System.out.println("========================================================");
			System.out.printf("%-10s%-12s%-12s%-12s%-12s\n", "Item No.", "Description", "Price", "Unit", "Quantity");
			System.out.println("--------------------------------------------------------");
			stockItemList.stream()
					.forEach((stk) -> System.out.printf("%-10s%-12s%-12s%-12s%-12s\n", stk.getItemNumber(),
							stk.getItemDescription(), stk.getItemPrice(), stk.getUnit(), stk.getQuantity()));
		}

	}

	private static void displayPurchaseOrders(String str) {
		System.out.println("\n" + str);
		System.out.println("==============================================");
		if (purchaseOrderList == null || purchaseOrderList.isEmpty()) {
			System.out.println("No Order Items available.");
		} else {
			System.out.printf("%-8s%-12s%-12s%-10s\n", "PO No.", "Order Date", "Ship Date", "Shipped");
			System.out.println("----------------------------------------");
			for (PurchaseOrder po : purchaseOrderList) {
				System.out.printf("%-8s%-12s%-12s%-10s\n", po.getPoNumber(), po.getOrderDate(), po.getShipDate(),
						po.isShipped());
			}
		}
	}

	// For Case 2
	static void addNewPurchaseOrder() {
		s.nextLine();
		// If customer is already assigned
		if (customer != null) {
			System.out.print("\nPlace Order for Customer No: " + customer.getCustomerNo() + " ? (Y/N): ");
			String yesOrNo = s.nextLine();
			if (yesOrNo.trim().equalsIgnoreCase("Y")) { // Place order for same assigned customer
				purchaseOrder = new PurchaseOrder(customer.getCustomerNo(), LocalDate.now());

				String addNextOrder = "Y";
				while (addNextOrder.equalsIgnoreCase("Y")) {
					if (displayAndAddOrderItem()) {
						System.out.print("\nAdd Order Item ? (Y/N) : ");
						addNextOrder = s.nextLine();
					} else {
						break;
					}
				}
				// Adding to the Database
				if (!purchaseOrder.getOiList().isEmpty()) {
					purchaseOrder.setPoNumber(poService.addPurchaseOrder(purchaseOrder));
					for (OrderItem orderItem : purchaseOrder.getOiList()) {
						orderItem.setPoNumber(purchaseOrder.getPoNumber());
						oiService.addOrderItem(orderItem);
					}
					System.out.println("Purchase Order Successfull.");
				}

			} else if (yesOrNo.trim().equalsIgnoreCase("N")) {
				placeOrderForAnotherCustomer();
			} else {
				System.out.println("Enter a Valid Input, Try Again!");
			}
		} else { // If there is no customer known or assigned then get customerList and assign
					// customer and add purchase order
			placeOrderForAnotherCustomer();
		}
	}

	// For Case 2, Place order for another customer with different customer ID
	static void placeOrderForAnotherCustomer() {
		fetchCustomerByID();
		if (customer != null) {
			purchaseOrder = new PurchaseOrder(customer.getCustomerNo(), LocalDate.now());

			String addNextOrder = "Y";
			while (addNextOrder.equalsIgnoreCase("Y")) {
				if (displayAndAddOrderItem()) {
					System.out.print("\nAdd Order Item ? (Y/N) : ");
					addNextOrder = s.nextLine();
				} else {
					break;
				}
			}

			// Adding to the Database
			// If No Order Items are added to the purchase Order then Don't save the
			// purchase order in DB
			if (!purchaseOrder.getOiList().isEmpty()) {
				purchaseOrder.setPoNumber(poService.addPurchaseOrder(purchaseOrder));
				for (OrderItem orderItem : purchaseOrder.getOiList()) {
					orderItem.setPoNumber(purchaseOrder.getPoNumber());
					oiService.addOrderItem(orderItem);
				}
				System.out.println("Purchase Order Successfull.");
			}
		}

	}

	// Case 4, fetch customer by ID
	static void fetchCustomerByID() {
		custList = custService.getAllCustomers();
		if (!custList.isEmpty()) {
			System.out.println("\nChoose From Following Customer ID : ");
			System.out.println("===================================");
			System.out.printf("%-5s%20s\n", "CNo.", "Customer Name");
			System.out.println("-------------------------");
			custList.stream().forEach((c) -> System.out.printf("%-5s%20s\n", c.getCustomerNo(), c.getName()));
			Set<Integer> custIdSet = custList.stream().map(Customer::getCustomerNo)
					.collect(Collectors.toCollection(LinkedHashSet::new));
			System.out.print("\nEnter Customer ID: ");
			int chooseCustId = s.nextInt();
			s.nextLine();
			if (!custIdSet.contains(chooseCustId)) {
				System.out.println("Entered Customer ID is invalid, Try Again!");
				customer = null;
				return;
			}
			customer = custService.getCustomer(chooseCustId); // Assigning the chosen customer
		} else {
			System.out.println("No Customers Exists!, Please Add New Customer.");
		}
	}

	// Case 4, display customer detail after fetching by cust_id
	static void displayCustomerDetails() {
		System.out.println("\n:::: Details of Customer ::::");
		System.out.println("==============================");
		System.out.printf(" %-14s: %s", "Customer No.", customer.getCustomerNo());
		System.out.printf("\r\n %-14s: %s", "Name", customer.getName());
		System.out.printf("\r\n %-14s: %s", "Street", customer.getStreet());
		System.out.printf("\r\n %-14s: %s", "City", customer.getCity());
		System.out.printf("\r\n %-14s: %s", "State", customer.getState());
		System.out.printf("\r\n %-14s: %s", "Zip", customer.getZip());
		System.out.printf("\r\n Cell Phone No.: %s", customer.getCellPhone());
		if (customer.getHomePhone() != null)
			System.out.printf(Locale.US, "\r\n Home Phone No.: %s", customer.getHomePhone());
		if (customer.getWorkPhone() != null)
			System.out.printf(Locale.US, "\r\n Work Phone No.: %s", customer.getWorkPhone());
		System.out.println();
	}

	// For Case 2, Display Repeated Order Items for User to Choose Order Item
	static boolean displayAndAddOrderItem() {
		System.out.println("\nChoose Your Order Item: ");
		System.out.println("=======================");
		System.out.printf("%-10s%-20s%-15s%-10s\n", "Item No.", "Item Description", "Item Price", "Unit");
		System.out.println("-----------------------------------------------------");
		stockItemList = stockService.getAllStockItems();
		stockItemList.stream()
				.forEach((stockItem) -> System.out.printf("%-10s%-20s%-15s%-10s\n", stockItem.getItemNumber(),
						stockItem.getItemDescription(), stockItem.getItemPrice(), stockItem.getUnit()));
		Set<Integer> stockIdSet = stockItemList.stream().map(StockItem::getItemNumber)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		System.out.print("\nEnter Order Item Number to Purchase: ");
		int stockItemNumberInput = s.nextInt();
		if (!stockIdSet.contains(stockItemNumberInput)) {
			System.out.println("Entered Order Item Number is Invalid, Try Again!");
			return false;
		}
		System.out.print("Enter Quantity: ");
		int quantityForOrder = s.nextInt();
		if (quantityForOrder <= 0) {
			System.out.println("Invalid Quantity Entered, Try Again!");
			return false;
		}
		purchaseOrder.getOiList()
				.add(new OrderItem(stockItemNumberInput, quantityForOrder, purchaseOrder.getPoNumber()));
		s.nextLine();
		return true;
	}

}
