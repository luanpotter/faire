package xyz.luan.faire;

import xyz.luan.faire.model.Product;

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
		System.out.println(products.get(0));
	}
}
