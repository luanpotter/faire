package xyz.luan.faire;

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

		// I need to mock some data in order to test
		mocks(products, orders);

		List<ProcessedOrder> processed = orders.stream()
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

		System.out.println(processed.size());
	}

	private void mocks(List<Product> products, List<Order> orders) {
		// change state because there's no 'new' orders
		orders.forEach(o -> o.setState(OrderState.NEW));
		// add some products because they were mostly out of stock
		products.forEach(p -> p.getOptions().forEach(o -> o.setAvailableQuantity(o.getAvailableQuantity() + 50)));
	}
}
