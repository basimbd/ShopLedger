import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CommandLineInteraction {
	
	private Scanner sc;
	private Operations op;
	private static Boolean menuStatus = true;
	
	public CommandLineInteraction() {
		this.sc = new Scanner(System.in);
		this.op = new Operations();
	}
	
	private int menu() {
		if(menuStatus) {
			System.out.println("----Main Menu Options----");
			System.out.printf("%-8s%s\n", "Button", "Options");
			System.out.printf("%-8s%s\n", "1", "Add a product.");
			System.out.printf("%-8s%s\n", "2", "Delete a product.");
			System.out.printf("%-8s%s\n", "3", "Buy a product.");
			System.out.printf("%-8s%s\n", "4", "Sell a product.");
			System.out.printf("%-8s%s\n", "5", "See the list of products.");
			System.out.printf("%-8s%s\n", "6", "See available balance.");
			System.out.printf("%-8s%s\n", "0", "Exit.");
		}else {
			System.out.println("Press 7 to return to the main menu.");
		}
		
		int selected_option = selectOption();
		return selected_option;
	}
	
	private int selectOption() {
		int selected_option;
		while(true) {
			try {
				selected_option = Integer.parseInt(sc.nextLine());
				if(selected_option >= 0 && selected_option <= 7)
					break;
				else
					System.out.println("Please press a number between 0 to 7 to select one of the options.");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selected_option;
	}
	
	public void openCLI() {
		while (true) {
			int selected_option = menu();
			switch(selected_option) {
			case 0:
				writeAccountToDB();
				System.exit(0);
				break;
			case 1:
				addProduct();
				break;
			case 2:
				deleteProduct();
				break;
			case 3:
				buyProduct();
				break;
			case 4:
				sellProduct();
				break;
			case 5:
				seeProducts();
				break;
			case 6:
				seeBalance();
				break;
			case 7:
				menuStatus = true;
				break;
			default:
				throw new AssertionError();
			}
		}
	}
	
	private void writeAccountToDB() {
		// TODO Auto-generated method stub
		Statement dbStatement = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			Account acc = Account.getAccount();
			dbStatement.executeUpdate("insert into values("+acc.getAvailable_balance()+","+acc.getProfit()+")");	//Query to Store account to DB
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
	}

	private void addProduct() {
		// TODO Auto-generated method stub
		System.out.println("Enter Product Name: ");
		String product_name = sc.nextLine();
		System.out.println("Enter Buying Price: ");
		int buy_price = Integer.parseInt(sc.nextLine());
		System.out.println("Enter Selling Price: ");
		int sell_price = Integer.parseInt(sc.nextLine());
		System.out.println("Enter Available Quantity: ");
		int available_quantity = Integer.parseInt(sc.nextLine());
		
		System.out.println("Are you you want to add "+product_name+"? Press y to confirm, n to cancel");
		String confirmation = sc.nextLine();
		if(confirmation.equalsIgnoreCase("y")) {
			Product product = new Product(product_name, buy_price, sell_price, available_quantity, sell_price-buy_price);
			op.addProduct(product);
			menuStatus = false;
		}else if(confirmation.equalsIgnoreCase("n")) {
			menuStatus = false;
			//start();
		}else {
			System.out.println("Please press either y or n.");
		}
		//menuStatus = false;
	}

	private void deleteProduct() {
		// TODO Auto-generated method stub
		Statement dbStatement = null;
		ResultSet dbResultSet = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbResultSet = dbStatement.executeQuery("select id, product_name from products"); //Query to Show available products
			
			System.out.println("Available Products are-");
			
			//Show the Available Products
			System.out.printf("%-6s%s\n", "id", "Product Name");
			while(dbResultSet.next()) {
				System.out.printf("%-6d%s\n", dbResultSet.getInt("id"), dbResultSet.getString("product_name"));
			}
			
			//Get id of Product and run Query to get that product from DB
			System.out.println("\nEnter the id of the product you want to delete from the above list.");
			int id = Integer.parseInt(sc.nextLine());
			String product_name = dbStatement.executeQuery("select product_name from products where id="+id).getString("product_name");
			
			//If product_name exist, proceed with the rest of the procedures
			if(product_name != null) {
				System.out.println("Are you you want to delete "+product_name+"? Press y to confirm, n to cancel");
				String confirmation = sc.nextLine();
				
				//If confirmed, proceed with the rest of the procedures
				if(confirmation.equalsIgnoreCase("y")) {
					op.deleteProduct(id, product_name);		//delete from Database
					menuStatus = false;
				}else if(confirmation.equalsIgnoreCase("n")) {
					menuStatus = false;
				}else {
					System.out.println("Please press either y or n.");
				}
			}else {
				System.out.println("This product doesn't exist. Please check the id you provided.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
		    try { 
		        if (dbResultSet != null) 
		        	dbResultSet.close(); 
		    } catch (SQLException e) {e.printStackTrace();} 
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
		//menuStatus = false;
	}
	
	private void buyProduct() {
		// TODO Auto-generated method stub
		Statement dbStatement = null;
		ResultSet dbResultSet = null;
		ResultSet dbResultSet2 = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbResultSet = dbStatement.executeQuery("select id, product_name from products"); //Query to Show available products
			
			System.out.println("Available Products are-");

			//Show the Available Products
			System.out.printf("%-6s%s\n", "id", "Product Name");
			while(dbResultSet.next()) {
				System.out.printf("%-6d%s\n", dbResultSet.getInt("id"), dbResultSet.getString("product_name"));
			}
			
			//Get id of Product and run Query to get that product from DB
			System.out.println("\nEnter the id of the product you want to buy from the above list.");
			int id = Integer.parseInt(sc.nextLine());
			dbResultSet2 = dbStatement.executeQuery("select product_name, buy_price, sell_price, available_quantity, profit from products where id="
					+id);
			
			//If the id is correct, proceed with the rest of the procedures
			if(dbResultSet2.isBeforeFirst()) {
				dbResultSet2.next();	//next so that DB cursors points to row
				
				//get Product name and How many to buy
				String product_name = dbResultSet2.getString("product_name");
				System.out.println("How many of this product do you want to buy?");
				int amount = Integer.parseInt(sc.nextLine());
				
				//ask if sure
				System.out.println("Are you you want to buy "+product_name+"? Press y to confirm, n to cancel");
				String confirmation = sc.nextLine();
				
				//If confirmed, proceed with the rest of the procedures
				if(confirmation.equalsIgnoreCase("y")) {
					//create Product object
					Product product = new Product(product_name,
							dbResultSet2.getInt("buy_price"), dbResultSet2.getInt("sell_price"),
							dbResultSet2.getInt("available_quantity"), dbResultSet2.getInt("profit"));
					op.buyProduct(product, amount);		//to write product to database
					menuStatus = false;
				}else if(confirmation.equalsIgnoreCase("n")) {
					menuStatus = false;
				}else {
					System.out.println("Please press either y or n.");
				}
			}else {//If the id is incorrect/doesn't exist
				System.out.println("This product doesn't exist. Please check the id you provided.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
		    try { 
		        if (dbResultSet != null) 
		        	dbResultSet.close(); 
		    } catch (SQLException e) {e.printStackTrace();} 
		    try { 
		        if (dbResultSet2 != null) 
		        	dbResultSet2.close(); 
		    } catch (SQLException e) {e.printStackTrace();} 
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	private void sellProduct() {
		// TODO Auto-generated method stub
		Statement dbStatement = null;
		ResultSet dbResultSet = null;
		ResultSet dbResultSet2 = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbResultSet = dbStatement.executeQuery("select id, product_name from products");	//Query to Show available products
			
			//Show available products
			System.out.println("Available Products are-");
			System.out.printf("%-6s%s\n", "id", "Product Name");
			while(dbResultSet.next()) {
				System.out.printf("%-6d%s\n", dbResultSet.getInt("id"), dbResultSet.getString("product_name"));
			}
			
			
			//Get id of Product and run Query to get that product from DB
			System.out.println("\nEnter the id of the product you want to sell from the above list.");
			int id = Integer.parseInt(sc.nextLine());
			dbResultSet2 = dbStatement.executeQuery("select product_name, buy_price, sell_price, available_quantity, profit from products where id="
					+id);
			
			//If the given id exists, continue with the rest of the procedure
			if (dbResultSet2.isBeforeFirst()) {
				dbResultSet2.next(); // next so that DB cursors points to row

				// get Product name and How many to buy
				String product_name = dbResultSet2.getString("product_name");
				System.out.println("How many of this product do you want to sell?");
				int amount = Integer.parseInt(sc.nextLine());
				
				System.out.println("Are you you want to sell "+product_name+"? Press y to confirm, n to cancel");
				String confirmation = sc.nextLine();
				
				//If confirmed, proceed with the rest of the procedures
				if (confirmation.equalsIgnoreCase("y")) {
					Product product = new Product(product_name,
							dbResultSet2.getInt("buy_price"), dbResultSet2.getInt("sell_price"),
							dbResultSet2.getInt("available_quantity"), dbResultSet2.getInt("profit"));
					op.sellProduct(product, amount);	//update DB
					menuStatus = false;
				}else if(confirmation.equalsIgnoreCase("n")) {
					menuStatus = false;
				}else {
					System.out.println("Please press either y or n.");
				}
			}else {//If the id is incorrect/doesn't exist
				System.out.println("The product doesn't exist. Please check the id you provided.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
		    try { 
		        if (dbResultSet != null) 
		        	dbResultSet.close(); 
		    } catch (SQLException e) {e.printStackTrace();} 
		    try { 
		        if (dbResultSet2 != null) 
		        	dbResultSet2.close(); 
		    } catch (SQLException e) {e.printStackTrace();} 
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	private void seeProducts() {
		// TODO Auto-generated method stub
		op.seeProducts();
		menuStatus = false;
	}
	
	private void seeBalance() {
		// TODO Auto-generated method stub
		op.seeAvailableBalance();
		menuStatus = false;
	}
}
