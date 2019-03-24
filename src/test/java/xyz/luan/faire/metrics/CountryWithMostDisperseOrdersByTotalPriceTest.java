package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.order.Address;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.processed.Country;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.model.processed.ProcessingItem;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CountryWithMostDisperseOrdersByTotalPriceTest {

	@Test
	public void testOneIsZeroAndTheOtherIsGreater() {
		List<ProcessedOrder> orders = asList(
				order("C1", 10),
				order("C1", 10),
				order("C1", 10),

				order("C2", 20),
				order("C2", 20),
				order("C2", 21)
		);
		Country state = new CountryWithMostDisperseOrdersByTotalPrice().process(orders);
		assertThat(state.getCode(), equalTo("C2"));
	}

	@Test
	public void testSingleOrderZero() {
		List<ProcessedOrder> orders = singletonList(
				order("C1", 10)
		);
		Country state = new CountryWithMostDisperseOrdersByTotalPrice().process(orders);
		assertThat(state.getCode(), equalTo("C1"));
	}

	@Test
	public void testOneClearlyGreaterThanTheOther() {
		List<ProcessedOrder> orders = asList(
				order("C1", 10),
				order("C1", 50),
				order("C1", 100),
				order("C1", 90),
				order("C1", 20),

				order("C2", 10),
				order("C2", 12),
				order("C2", 9),
				order("C2", 15)
		);
		Country state = new CountryWithMostDisperseOrdersByTotalPrice().process(orders);
		assertThat(state.getCode(), equalTo("C1"));
	}

	private ProcessedOrder order(String country, int price) {
		Order order = new Order();
		Address address = new Address();
		address.setCountryCode(country);
		order.setAddress(address);

		OrderItem item = new OrderItem();
		item.setPriceCents(price);

		ProcessingItem processingItem = new ProcessingItem(null, item, null, null);
		return new ProcessedOrder(order, singletonList(processingItem));
	}
}
