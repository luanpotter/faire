package xyz.luan.faire.core;

import lombok.NoArgsConstructor;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.ProcessingItem;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static xyz.luan.faire.util.Util.nonEmpty;

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
			System.out.println("Processed " + processed.size() + " entries.");

			System.out.println("Running metrics...");
			new FaireMetrics(processed).run();

			System.out.println("The end.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProcessedOrder> fetchAndProcessOrders() throws IOException {
		List<Product> products = fetchProducts();
		Map<String, Product> productsById = products.stream().collect(toMap(Product::getId, identity()));

		List<Order> orders = fetchOrders();

		return orders.stream()
				.filter(o -> o.getState().equals(OrderState.NEW))
				.map(order -> {
					List<ProcessingItem> processingItems = order.getItems().stream().map(item -> {
						Product product = productsById.get(item.getProductId());
						Optional<ProductOption> optional = product.findOptionById(item.getProductOptionId());
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

		// MOCK change state because there's no 'new' orders
		orders.forEach(o -> o.setState(OrderState.NEW));

		return orders;
	}

	public List<Product> fetchProducts() throws IOException {
		List<Product> products = api.listProducts();

		// MOCK add some products because they were mostly out of stock
		products.forEach(p -> p.getOptions().forEach(o -> o.setAvailableQuantity(10000)));

		return products;
	}
}
