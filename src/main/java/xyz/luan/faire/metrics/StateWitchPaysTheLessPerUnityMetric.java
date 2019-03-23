package xyz.luan.faire.metrics;

import lombok.Getter;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class StateWitchPaysTheLessPerUnityMetric {

	public String run(List<ProcessedOrder> orders) {
		return orders.stream()
				.collect(groupingBy(o -> o.getOrder().getAddress().getStateCode()))
				.entrySet().stream()
				.map(State::new)
				.min(Comparator.comparing(State::getAveragePerItem))
				.map(State::getState)
				.orElse(null);
	}

	@Getter
	class State {
		private String state;
		private double averagePerItem;

		public State(Map.Entry<String, List<ProcessedOrder>> entry) {
			this.state = entry.getKey();
			this.averagePerItem = entry.getValue().stream().map(this::foo).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(Double.MAX_VALUE);
		}

		private Double foo(ProcessedOrder order) {
			OptionalDouble optional = order.getItems().stream().mapToDouble(i -> i.getItem().getPriceCents()).average();
			if (optional.isPresent()) {
				return optional.getAsDouble();
			}
			return null;
		}
	}
}
