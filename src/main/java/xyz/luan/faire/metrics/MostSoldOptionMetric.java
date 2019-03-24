package xyz.luan.faire.metrics;

import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.ProcessingItem;
import xyz.luan.faire.model.processed.ProductOptionWithProduct;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * The best selling product option
 * <p>
 * This selects the product option that had most sales (most quantity sold across all orders).
 */
public class MostSoldOptionMetric implements Metric<ProductOptionWithProduct> {

	public ProductOptionWithProduct process(List<ProcessedOrder> orders) {
		return orders.stream()
				.flatMap(e -> e.getItems().stream())
				.collect(groupingBy(e -> e.getOption().getId()))
				.values().stream()
				.map(Option::new)
				.reduce((a, b) -> a.totalAmount > b.totalAmount ? a : b)
				.map(e -> e.option)
				.orElse(null);
	}

	@Override
	public String print(ProductOptionWithProduct result) {
		if (result == null) {
			return "The best selling product option: not found, no orders with price";
		}
		return String.format("The best selling product option was option id %s (product option name: %s; product name: %s)",
				result.getOption().getId(), result.getOption().getName(), result.getProduct().getName());
	}

	private class Option {
		private ProductOptionWithProduct option;
		private int totalAmount;

		public Option(List<ProcessingItem> items) {
			ProductOption option = items.get(0).getOption();
			Product product = items.get(0).getProduct();
			this.option = new ProductOptionWithProduct(option, product);
			this.totalAmount = getSum(items);
		}

		private int getSum(List<ProcessingItem> e) {
			return e.stream().mapToInt(f -> f.getItem().getQuantity()).sum();
		}
	}
}
