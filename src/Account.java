import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Account {
	private static Account instance = null;
	private int available_balance=-1;
	private int profit;
	
	public static Account getAccount() {
		if(instance == null)
			instance = new Account();
		return instance;
	}
	
	private Account() {
		//super();
		try {
			Statement dbStatement = DatabaseConnection.connectToDatabase().createStatement();
			ResultSet dbResultSet = dbStatement.executeQuery("select * from account");
			
			if(!dbResultSet.isBeforeFirst()) {
				dbStatement.executeUpdate("insert into account values(0,0)");
				dbResultSet = dbStatement.executeQuery("select * from account");
			}
			while (dbResultSet.next()) {
				this.available_balance = dbResultSet.getInt("available_balance");
				this.profit = dbResultSet.getInt("profit");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAvailable_balance() {
		return available_balance;
	}

	public void setAvailable_balance(int available_balance) {
		this.available_balance = available_balance;
	}

	public int getProfit() {
		return profit;
	}

	public void setProfit(int profit) {
		this.profit = profit;
	}
	
	public void incrementAvailableBalance(int balance) {
		this.available_balance += balance;
	}
	
	public void decrementAvailableBalance(int balance) {
		if((this.available_balance - balance) >= 0) {
			this.available_balance -= balance;
		}else {
			System.out.println("Not Sufficient Balance");
		}
	}
	
	public void incrementProfit(int profit) {
		this.profit += profit;
	}
}
