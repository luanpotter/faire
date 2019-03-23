package xyz.luan.faire.metrics;

import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.order.Order;

import java.util.Comparator;
import java.util.List;

public class LargestOrderByMoneyMetric {

	public Order run(List<ProcessedOrder> orders) {
		return orders.stream()
				.max(Comparator.comparing(c -> c.getItems().stream().mapToInt(e -> e.getItem().getPriceCents() * e.getItem().getQuantity()).sum()))
				.map(ProcessedOrder::getOrder)
				.orElse(null);
	}
}
