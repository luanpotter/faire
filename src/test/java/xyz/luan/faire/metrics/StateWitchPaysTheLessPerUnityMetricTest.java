package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.order.Address;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.ProcessingItem;
import xyz.luan.faire.model.processed.State;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateWitchPaysTheLessPerUnityMetricTest {

	@Test
	public void testStateWitchPaysTheLessPerUnitySimpleCase() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", "S1", item(100, 10), item(1, 1)),
				createOrder("o2", "S2", item(10, 20), item(5, 15))
		);
		State state = new StateWitchPaysTheLessPerUnityMetric().process(orders);
		assertThat(state.getCode(), equalTo("S1"));
	}

	@Test
	public void testStateWitchPaysTheLessPerUnityComplexCase() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", "NY", item(100, 10)),
				createOrder("o2", "CA", item(1, 40), item(20, 1)),
				createOrder("o3", "CA", item(20, 20)),
				createOrder("o4", "NY", item(1, 10), item(1, 1000)),
				createOrder("o5", "AK", item(25, 4)),
				createOrder("o6", "NY", item(4, 25))
		);

		// NY: (100 * 10 + 1 * 10 + 1 * 1000 + 4 * 25) / (100 + 10 + 1 + 4) ~= 18.3
		// CA: (1 * 40 + 20 * 1 + 20 * 20) / (1 + 20 + 20) ~= 11.2
		// AK: 4 (far less!)

		State state = new StateWitchPaysTheLessPerUnityMetric().process(orders);
		assertThat(state.getCode(), equalTo("AK"));

		List<ProcessedOrder> ordersSecond = orders.stream().filter(e -> !e.getOrder().getId().equals("o5")).collect(toList());
		State stateSecond = new StateWitchPaysTheLessPerUnityMetric().process(ordersSecond);
		assertThat(stateSecond.getCode(), equalTo("CA"));

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
		List<ProcessingItem> orderItems = Arrays.stream(items).map(toItem(order)).collect(toList());
		return new ProcessedOrder(order, orderItems);
	}

	private Function<OrderItem, ProcessingItem> toItem(Order order) {
		return e -> new ProcessingItem(order, e, null, null);
	}
}
