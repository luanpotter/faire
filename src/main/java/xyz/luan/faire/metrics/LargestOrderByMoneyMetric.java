package xyz.luan.faire.metrics;

import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Comparator;
import java.util.List;

/**
 * The largest order by dollar amount.
 * <p>
 * This fetches the order that has the largest price.
 */
public class LargestOrderByMoneyMetric implements Metric<Order> {

	@Override
	public Order process(List<ProcessedOrder> orders) {
		return orders.stream()
				.max(Comparator.comparing(c -> c.getItems().stream().mapToInt(e -> e.getItem().getPriceCents() * e.getItem().getQuantity()).sum()))
				.map(ProcessedOrder::getOrder)
				.orElse(null);
	}

	@Override
	public String print(Order order) {
		if (order == null) {
			return "The largest order by dollar amount: not found, no orders";
		}
		return "The largest order by dollar amount: " + order.getId();
	}
}
