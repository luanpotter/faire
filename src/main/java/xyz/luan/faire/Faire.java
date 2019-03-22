package xyz.luan.faire;

import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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

		Map<String, Product> productsById = products.stream().collect(toMap(Product::getId, identity()));

		orders.stream().filter(o -> o.getState().equals(OrderState.NEW)).forEach(order -> {
			order.getItems().forEach(item -> {
				Product product = productsById.get(item.getProductId());
				ProductOption option = product.getOptions().stream().filter(o -> item.getProductOptionId().equals(o.getId())).findFirst().get();

				int requiredQuantity = item.getQuantity();
				int availableQuantity = option.getAvailableQuantity();

				System.out.println(requiredQuantity);
				System.out.println(availableQuantity);
			});
		});
		System.out.println(products.size());
		System.out.println(orders.size());
		System.out.println(orders.stream().map(Order::getState).distinct().collect(toList()));
		System.out.println("---");
	}
}
