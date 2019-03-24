package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.order.Address;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.State;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateWithMostOrdersMetricTest {

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

		State state = new StateWithMostOrdersMetric().process(orders);
		assertThat(state.getCode(), equalTo("NY"));
	}

	@Test
	public void testOrdersInSingleState() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", "AL"),
				createOrder("o2", "AL"),
				createOrder("o3", "AL")
		);

		State state = new StateWithMostOrdersMetric().process(orders);
		assertThat(state.getCode(), equalTo("AL"));
	}

	@Test
	public void testEmptyList() {
		State state = new StateWithMostOrdersMetric().process(emptyList());
		assertThat(state, nullValue());
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
