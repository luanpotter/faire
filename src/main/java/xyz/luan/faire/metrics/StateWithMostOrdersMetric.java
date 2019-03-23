package xyz.luan.faire.metrics;

import lombok.Getter;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class StateWithMostOrdersMetric {

	public String run(List<ProcessedOrder> orders) {
		return orders.stream()
				.collect(groupingBy(o -> o.getOrder().getAddress().getStateCode()))
				.entrySet().stream()
				.map(State::new)
				.max(Comparator.comparing(State::getAmountOfOrders))
				.map(State::getState)
				.orElse(null);
	}

	@Getter
	class State {
		String state;
		int amountOfOrders;

		public State(Map.Entry<String, List<ProcessedOrder>> entry) {
			this.state = entry.getKey();
			this.amountOfOrders = entry.getValue().size();
		}
	}
}
