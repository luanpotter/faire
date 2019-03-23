package xyz.luan.faire;

import org.junit.Test;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static xyz.luan.faire.setup.TestUtils.createToaster;

public class FaireTest {

	@Test
	public void testFaireRunNoProducts() throws IOException {
		Faire faire = mockFaire(Collections.emptyList(), Collections.emptyList());
		List<ProcessedOrder> list = faire.fetchAndProcessOrders();
		assertThat(list.size(), equalTo(0));
	}

	@Test
	public void testFaireProcessOnlyNewOrders() throws IOException {
		Product toaster = createToaster();

		Order o1 = new Order();
		o1.setState(OrderState.PROCESSING);

		OrderItem item = new OrderItem();
		item.setProductId("p_toaster");
		item.setQuantity(1);
		item.setProductOptionId("po_toaster");

		Order o2 = new Order();
		o2.setState(OrderState.NEW);
		o2.setItems(singletonList(item));

		Order o3 = new Order();
		o3.setState(OrderState.PROCESSING);

		Faire faire = mockFaire(singletonList(toaster), asList(o1, o2, o3));
		List<ProcessedOrder> orders = faire.fetchAndProcessOrders();

		assertThat(orders.size(), equalTo(1));

		ProcessedOrder order = orders.get(0);
		assertThat(order.getOrder().getState(), equalTo(OrderState.PROCESSING));
		assertThat(order.getItems().size(), equalTo(1));

		ProcessingItem processingItem = order.getItems().get(0);
		assertThat(processingItem.getOption().getAvailableQuantity(), equalTo(9));
	}

	@Test
	public void testFaireDontUpdateIncompleteOrders() throws IOException {
		Product toaster = createToaster();

		OrderItem item = new OrderItem();
		item.setProductId("p_toaster");
		item.setQuantity(20);
		item.setProductOptionId("po_toaster");

		Order order = new Order();
		order.setItems(singletonList(item));
		order.setState(OrderState.NEW);

		Faire faire = mockFaire(singletonList(toaster), singletonList(order));
		List<ProcessedOrder> orders = faire.fetchAndProcessOrders();

		assertThat(orders.size(), equalTo(0));

		ProductOption option = toaster.getOptions().get(0);
		assertThat(option.getAvailableQuantity(), equalTo(10));
		assertThat(option.getBackorderedUntil(), notNullValue());
	}

	private Faire mockFaire(List<Product> products, List<Order> orders) throws IOException {
		Faire faire = spy(new Faire());

		doReturn(orders).when(faire).fetchOrders();
		doReturn(products).when(faire).fetchProducts();

		return faire;
	}
}
