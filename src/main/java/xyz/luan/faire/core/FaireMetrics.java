package xyz.luan.faire.core;

import xyz.luan.faire.metrics.*;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class FaireMetrics {

	private static final List<Metric<?>> METRICS = Arrays.asList(
			new MostSoldOptionMetric(),
			new LargestOrderByMoneyMetric(),
			new StateWithMostOrdersMetric(),
			new CheapestCarrierPerQuantityMetrics(),
			new StateWitchPaysTheLessPerUnityMetric(),
			new CountryWithMostDisperseOrdersByTotalPrice()
	);

	private List<ProcessedOrder> orders;

	public FaireMetrics(List<ProcessedOrder> orders) {
		this.orders = orders;
	}

	public String process() {
		return METRICS.stream().map(m -> m.run(orders)).collect(joining("\n"));
	}

	public void run() {
		System.out.println(process());
	}
}
