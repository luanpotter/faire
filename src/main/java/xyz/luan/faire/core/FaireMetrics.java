package xyz.luan.faire.core;

import xyz.luan.faire.metrics.*;
import xyz.luan.faire.model.order.Carrier;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.product.ProductOption;

import java.util.List;

public class FaireMetrics {

	private List<ProcessedOrder> orders;

	public FaireMetrics(List<ProcessedOrder> orders) {
		this.orders = orders;
	}

	public void run() {
		ProductOption mostSoldOption = new MostSoldOptionMetric().run(orders);
		System.out.println("The best selling product option: " + mostSoldOption.getId());

		Order largestOrderByMoney = new LargestOrderByMoneyMetric().run(orders);
		System.out.println("The largest order by dollar amount: " + largestOrderByMoney.getId());

		String state = new StateWithMostOrdersMetric().run(orders);
		System.out.println("The state with the most orders: " + state);

		Carrier carrier = new CheapestCarrierPerQuantityMetrics().run(orders);
		System.out.println("The on average cheapest carrier per quantity: " + carrier);

		String statePayLess = new StateWitchPaysTheLessPerUnityMetric().run(orders);
		System.out.println("The state witch pays the less per unity on average: " + statePayLess);
	}
}
