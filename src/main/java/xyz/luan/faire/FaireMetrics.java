package xyz.luan.faire;

import xyz.luan.faire.metrics.LargestOrderByMoneyMetric;
import xyz.luan.faire.metrics.MostSoldOptionMetric;
import xyz.luan.faire.model.order.Order;
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
	}
}
