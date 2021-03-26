public class Product {
	private String product_name;
	private int buy_price, sell_price, available_quantity, profit;
	
	public Product(String product_name, int buy_price, int sell_price, int available_quantity, int profit) {
		//super();
		this.product_name = product_name;
		this.buy_price = buy_price;
		this.sell_price = sell_price;
		this.available_quantity = available_quantity;
		this.profit = profit;
		//addToDB(this);
	}
	
	/*private void addToDB(Product product) {
		// TODO Auto-generated method stub
		
	}*/

	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getBuy_price() {
		return buy_price;
	}
	public void setBuy_price(int buy_price) {
		this.buy_price = buy_price;
	}
	public int getSell_price() {
		return sell_price;
	}
	public void setSell_price(int sell_price) {
		this.sell_price = sell_price;
	}
	public int getAvailable_quantity() {
		return available_quantity;
	}
	public void setAvailable_quantity(int available_quantity) {
		this.available_quantity = available_quantity;
	}
	public int getProfit() {
		return profit;
	}
	public void setProfit(int profit) {
		this.profit = profit;
	}
}
