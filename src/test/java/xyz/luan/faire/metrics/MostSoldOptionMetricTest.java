package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.ProcessedOrder;
import xyz.luan.faire.ProcessingItem;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static xyz.luan.faire.setup.TestUtils.createSwitch;
import static xyz.luan.faire.setup.TestUtils.createToaster;

public class MostSoldOptionMetricTest {

	@Test
	public void testMostSoldOption() {
		ProductOption toaster = createToaster().getOptions().get(0);
		Product productSwitch = createSwitch();
		ProductOption neon = productSwitch.getOptions().get(0);
		ProductOption grey = productSwitch.getOptions().get(1);

		ProcessingItem item1 = item(neon, 1);
		ProcessingItem item2 = item(toaster, 1);
		ProcessingItem item3 = item(grey, 2);
		ProcessingItem item4 = item(neon, 2);

		ProcessedOrder o1 = new ProcessedOrder(null, asList(item1, item2));
		ProcessedOrder o2 = new ProcessedOrder(null, singletonList(item3));
		ProcessedOrder o3 = new ProcessedOrder(null, singletonList(item4));

		List<ProcessedOrder> orders = asList(o1, o2, o3);
		ProductOption option = new MostSoldOptionMetric().run(orders);

		assertThat(option.getId(), equalTo("po_switch_neon"));
	}

	private ProcessingItem item(ProductOption option, int amount) {
		OrderItem orderItem = new OrderItem();
		orderItem.setProductOptionId(option.getId());
		orderItem.setProductId(option.getProductId());
		orderItem.setQuantity(amount);

		ProcessingItem processingItem = new ProcessingItem();
		processingItem.setItem(orderItem);
		processingItem.setOption(option);
		return processingItem;
	}
}
