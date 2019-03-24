package xyz.luan.faire.metrics;

import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.List;

public interface Metric<T> {

	T process(List<ProcessedOrder> orders);

	String print(T result);

	default String run(List<ProcessedOrder> orders) {
		return print(process(orders));
	}
}
