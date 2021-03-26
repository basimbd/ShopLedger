import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private static String dbURL = "jdbc:mysql://localhost:3306/shop-ledger";
	private static String userName = "root";
	private static String password = "password";
	
	static Connection dbConnection = null;
	
	public static Connection connectToDatabase() {
		if(dbConnection != null)
			return dbConnection;
		return connectToDatabase(dbURL, userName, password);
	}
	
	private static Connection connectToDatabase(String dbURL, String userName, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbConnection=DriverManager.getConnection(dbURL, userName, password);
             //= DriverManager.getConnection(dbURL, userName, password);
			//ResultSet dbResultSet = dbStatement.executeQuery("select * from products");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dbConnection;
	}
}
