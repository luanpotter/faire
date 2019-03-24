package xyz.luan.faire.metrics;

import lombok.Getter;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.State;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class StateWithMostOrdersMetric implements Metric<State> {

	@Override
	public State process(List<ProcessedOrder> orders) {
		return orders.stream()
				.collect(groupingBy(o -> new State(o.getOrder().getAddress())))
				.entrySet().stream()
				.map(StateOrders::new)
				.max(Comparator.comparing(StateOrders::getAmountOfOrders))
				.map(StateOrders::getState)
				.orElse(null);
	}

	@Override
	public String print(State state) {
		if (state == null) {
			return "The state with the most orders: not found, no orders";
		}
		return String.format("The state with the most orders: %s (%s)", state.getName(), state.getCode());
	}

	@Getter
	class StateOrders {
		private State state;
		private int amountOfOrders;

		public StateOrders(Map.Entry<State, List<ProcessedOrder>> entry) {
			this.state = entry.getKey();
			this.amountOfOrders = entry.getValue().size();
		}
	}
}
