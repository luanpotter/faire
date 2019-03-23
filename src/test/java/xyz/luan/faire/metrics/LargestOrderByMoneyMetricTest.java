package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.ProcessedOrder;
import xyz.luan.faire.ProcessingItem;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.product.ProductOption;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static xyz.luan.faire.setup.TestUtils.createSwitch;
import static xyz.luan.faire.setup.TestUtils.createToaster;

public class LargestOrderByMoneyMetricTest {

	@Test
	public void testLargestOrderByMoney() {
		ProductOption toaster = createToaster().getOptions().get(0);
		ProductOption nSwitch = createSwitch().getOptions().get(0);

		int switchPrice = 1000 * 100;
		int toasterPrice = 500 * 100;

		ProcessingItem item1 = item(nSwitch, 1, switchPrice);
		ProcessingItem item2 = item(toaster, 4, toasterPrice);

		Order o1 = new Order();
		o1.setId("o1");

		Order o2 = new Order();
		o2.setId("o2");

		ProcessedOrder po1 = new ProcessedOrder(o1, singletonList(item1));
		ProcessedOrder po2 = new ProcessedOrder(o2, singletonList(item2));

		List<ProcessedOrder> orders = asList(po1, po2);
		Order order = new LargestOrderByMoneyMetric().run(orders);

		assertThat(order.getId(), equalTo("o2"));
	}

	private ProcessingItem item(ProductOption option, int amount, int price) {
		OrderItem orderItem = new OrderItem();
		orderItem.setProductOptionId(option.getId());
		orderItem.setProductId(option.getProductId());
		orderItem.setQuantity(amount);
		orderItem.setPriceCents(price);

		ProcessingItem processingItem = new ProcessingItem();
		processingItem.setItem(orderItem);
		processingItem.setOption(option);
		return processingItem;
	}

}
