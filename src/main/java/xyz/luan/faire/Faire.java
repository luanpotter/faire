package xyz.luan.faire;

import lombok.NoArgsConstructor;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static xyz.luan.faire.Util.nonEmpty;

@NoArgsConstructor
public class Faire {

	private FaireApi api;

	public Faire(String apiKey) {
		this.api = new FaireApi(apiKey);
	}

	public void run() {
		System.out.println("Running...");
		try {
			List<ProcessedOrder> processed = fetchAndProcessOrders();
			System.out.println(processed.size());
			System.out.println("The end.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProcessedOrder> fetchAndProcessOrders() throws IOException {
		List<Product> products = fetchProducts();
		List<Order> orders = fetchOrders();

		Map<String, Product> productsById = products.stream().collect(toMap(Product::getId, identity()));

		return orders.stream()
				.filter(o -> o.getState().equals(OrderState.NEW))
				.map(order -> {
					List<ProcessingItem> processingItems = order.getItems().stream().map(item -> {
						Product product = productsById.get(item.getProductId());
						Optional<ProductOption> optional = product.getOptions().stream().filter(o -> item.getProductOptionId().equals(o.getId())).findFirst();
						return optional.map(option -> new ProcessingItem(order, item, product, option));
					}).flatMap(nonEmpty()).collect(toList());

					List<ProcessingItem> noStockedItems = processingItems.stream().filter(ProcessingItem::noStock).collect(toList());
					if (noStockedItems.isEmpty()) {
						processingItems.forEach(ProcessingItem::doProcess);
						return Optional.of(new ProcessedOrder(order, processingItems));
					} else {
						noStockedItems.forEach(ProcessingItem::markNoStock);
						return Optional.<ProcessedOrder>empty();
					}
				}).flatMap(nonEmpty()).collect(toList());
	}

	public List<Order> fetchOrders() throws IOException {
		List<Order> orders = api.listOrders();

		// change state because there's no 'new' orders
		orders.forEach(o -> o.setState(OrderState.NEW));

		return orders;
	}

	public List<Product> fetchProducts() throws IOException {
		List<Product> products = api.listProducts();

		// add some products because they were mostly out of stock
		products.forEach(p -> p.getOptions().forEach(o -> o.setAvailableQuantity(o.getAvailableQuantity() + 50)));


		return products;
	}
}
