package xyz.luan.faire.metrics;

import lombok.Getter;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.State;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

/**
 * This returns the state that, on average, pays the less for each unity of item.
 */
public class StateWitchPaysTheLessPerUnityMetric implements Metric<State> {

	@Override
	public State process(List<ProcessedOrder> orders) {
		return orders.stream()
				.collect(groupingBy(o -> new State(o.getOrder().getAddress())))
				.entrySet().stream()
				.map(StateOrders::new)
				.min(Comparator.comparing(StateOrders::getAveragePerItem))
				.map(StateOrders::getState)
				.orElse(null);
	}

	@Override
	public String print(State state) {
		if (state == null) {
			return "The state witch pays the less per unity on average: not found, no orders with price";
		}
		return String.format("The state witch pays the less per unity on average: %s (%s)", state.getName(), state.getCode());
	}

	@Getter
	class StateOrders {
		private State state;
		private double averagePerItem;

		public StateOrders(Map.Entry<State, List<ProcessedOrder>> entry) {
			this.state = entry.getKey();
			this.averagePerItem = average(entry.getValue());
		}

		public double average(List<ProcessedOrder> orders) {
			int num = orders.stream().flatMapToInt(this::numerators).sum();
			int dem = orders.stream().flatMapToInt(this::denominators).sum();
			return (double) num / dem;
		}

		private IntStream numerators(ProcessedOrder order) {
			return order.getItems().stream().mapToInt(i -> i.getItem().getQuantity() * i.getItem().getPriceCents());
		}

		private IntStream denominators(ProcessedOrder order) {
			return order.getItems().stream().mapToInt(i -> i.getItem().getQuantity());
		}
	}
}
