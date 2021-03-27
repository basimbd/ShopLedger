import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;

public class Operations {
	//private List<Product> product_list;
	
	public Operations() {
		//this.product_list = new ArrayList<>();
	}
	
	public void addProduct(Product product) {
		Statement dbStatement = null;
		try {
			//dbConnection = DatabaseConnection.connectToDatabase();
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbStatement.executeUpdate("insert into products(product_name, buy_price, sell_price, available_quantity, profit) values('"
															+product.getProduct_name()+"',"+product.getBuy_price()+","
															+product.getSell_price()+","+product.getAvailable_quantity()+","
															+product.getProfit()+")");
			System.out.println("Product "+product.getProduct_name()+" added successfully.");
		} catch (SQLException e) {
			// Show message if requested product already exist.
			//e.printStackTrace();
			System.out.println("Product "+product.getProduct_name()+" already exists.");
		} finally { 
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	public void deleteProduct(int id, String product_name) {
		Statement dbStatement = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbStatement.executeUpdate("delete from products where id="+id);
			System.out.println("Product "+product_name+" deleted successfully.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally { 
		    try { 
		        if (dbStatement != null) 
		        	dbStatement.close(); 
		    } catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	public void buyProduct(Product product, int amount) {
		int buy_price = product.getBuy_price();
		Account acc = Account.getAccount();
		if((acc.getAvailable_balance()-(buy_price*amount)) >= 0){
			Statement dbStatement = null;
			try {
				dbStatement = DatabaseConnection.connectToDatabase().createStatement();
				dbStatement.executeUpdate("update products set available_quantity=available_quantity+"
						+amount+" where product_name='"+product.getProduct_name()+"'");
				acc.decrementAvailableBalance(buy_price*amount);
				System.out.println("Product "+product.getProduct_name()+" bought successfully.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally { 
			    try { 
			        if (dbStatement != null) 
			        	dbStatement.close(); 
			    } catch (SQLException e) {e.printStackTrace();} 
			}
		}else {
			System.out.println("Not suffient balance. Keep "+buy_price*amount+" BDT in balance and try again.");
		}
	}
	
	public void sellProduct(Product product, int amount) {
		int available_quantity = product.getAvailable_quantity();
		if(amount > available_quantity) {
			System.out.println("Not enough products in the inventory.\nAvailable Quantity: "+available_quantity
					+"\tRequested Quantity: "+amount+".");
		}else {
			int profit = product.getProfit();
			int selling_price = product.getSell_price();
			Account acc = Account.getAccount();
			acc.incrementProfit(profit*amount);
			acc.incrementAvailableBalance(selling_price*amount);
			
			Statement dbStatement = null;
			try {
				dbStatement = DatabaseConnection.connectToDatabase().createStatement();
				dbStatement.executeUpdate("update products set available_quantity=available_quantity-"
						+amount+" where product_name='"+product.getProduct_name()+"'");
				System.out.println("Product "+product.getProduct_name()+" sold successfully.");
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
	}
	
	public void seeProducts() {
		Statement dbStatement = null;
		ResultSet dbResultSet = null;
		try {
			dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			dbResultSet = dbStatement.executeQuery("select product_name, available_quantity, profit from products");
			
			if(!dbResultSet.isBeforeFirst()) {
				System.out.println("There are no products in the inventory. :(");
			}else {
				System.out.printf("%-20s%-25s%6s\n", "Product Name", "Available Quantity", "Profit");
				System.out.printf("%-20s%-25s%6s\n", "============", "==================", "======");
				while(dbResultSet.next()) {
					System.out.printf("%-20s%-25d%6d\n",dbResultSet.getString("product_name"), dbResultSet.getInt("available_quantity"), dbResultSet.getInt("profit"));
				}
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
	}

	public void seeAvailableBalance() {
		Account acc = Account.getAccount();
		System.out.println("Available Balance is "+acc.getAvailable_balance()+".");
	}
}
