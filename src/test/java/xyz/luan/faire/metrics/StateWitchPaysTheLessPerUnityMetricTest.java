package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.order.Address;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateWitchPaysTheLessPerUnityMetricTest {

	@Test
	public void testStateWitchPaysTheLessPerUnity() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", "NY", item(100, 10)),
				createOrder("o2", "CA", item(1, 40), item(20, 1)),
				createOrder("o3", "CA", item(20, 20)),
				createOrder("o4", "NY", item(1, 10), item(1, 1000)),
				createOrder("o5", "AK", item(25, 4)),
				createOrder("o6", "NY", item(4, 25))
		);

		String state = new StateWitchPaysTheLessPerUnityMetric().run(orders);
		assertThat(state, equalTo("AK"));
	}

	private OrderItem item(int amount, int price) {
		OrderItem item = new OrderItem();
		item.setQuantity(amount);
		item.setPriceCents(price);
		return item;
	}

	private ProcessedOrder createOrder(String id, String state, OrderItem... items) {
		Order order = new Order();
		order.setId(id);
		Address address = new Address();
		address.setStateCode(state);
		order.setAddress(address);
		order.setItems(Arrays.asList(items));
		return new ProcessedOrder(order, emptyList());
	}
}
