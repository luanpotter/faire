package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.order.Address;
import xyz.luan.faire.model.order.Order;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateWithMostOrdersTest {

	@Test
	public void testStateWithMostOrders() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", "NY"),
				createOrder("o2", "CA"),
				createOrder("o3", "CA"),
				createOrder("o4", "NY"),
				createOrder("o5", "AK"),
				createOrder("o6", "NY")
		);

		String state = new StateWithMostOrders().run(orders);
		assertThat(state, equalTo("NY"));
	}

	private ProcessedOrder createOrder(String id, String state) {
		Order order = new Order();
		order.setId(id);
		Address address = new Address();
		address.setStateCode(state);
		order.setAddress(address);
		return new ProcessedOrder(order, emptyList());
	}
}
