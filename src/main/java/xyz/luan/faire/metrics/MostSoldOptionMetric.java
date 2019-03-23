package xyz.luan.faire.metrics;

import xyz.luan.faire.ProcessedOrder;
import xyz.luan.faire.ProcessingItem;
import xyz.luan.faire.model.product.ProductOption;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class MostSoldOptionMetric {

	public ProductOption run(List<ProcessedOrder> orders) {
		return orders.stream()
				.flatMap(e -> e.getItems().stream())
				.collect(groupingBy(e -> e.getOption().getId()))
				.values().stream()
				.map(Option::new)
				.reduce((a, b) -> a.totalAmount > b.totalAmount ? a : b)
				.map(e -> e.option)
				.orElse(null);
	}

	private class Option {
		private ProductOption option;
		private int totalAmount;

		public Option(List<ProcessingItem> items) {
			this.option = items.get(0).getOption();
			this.totalAmount = getSum(items);
		}

		private int getSum(List<ProcessingItem> e) {
			return e.stream().mapToInt(f -> f.getItem().getQuantity()).sum();
		}
	}
}
