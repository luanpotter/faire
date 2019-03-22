package xyz.luan.faire;

import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.product.Product;

import java.io.IOException;
import java.util.List;

public class Faire {

	private final FaireApi api;

	public Faire(String apiKey) {
		this.api = new FaireApi(apiKey);
	}

	public void run() {
		System.out.println("Running...");
		try {
			doRun();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void doRun() throws IOException {
		List<Product> products = api.listProducts();
		List<Order> orders = api.listOrders();
		System.out.println(products.size());
		System.out.println(orders.size());
		System.out.println("---");
	}
}
