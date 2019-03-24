package xyz.luan.faire.core;

import org.junit.Test;
import xyz.luan.faire.core.FaireApi;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static xyz.luan.faire.util.TestUtils.mockFaireApi;

/**
 * This tests the FaireApi class using mocked data obtained directly from the real API.
 * <p>
 * It makes sure parsing is done properly and that the objects created actually reflect the reality.
 * Only the get() method from the HttpFacade is mocked (via mockito), so it's very close to the actual behavior.
 */
public class FaireApiTest {

	@Test
	public void testParseProducts() throws IOException {
		FaireApi api = mockFaireApi();

		List<Product> products = api.listProducts();
		assertThat(products.size(), equalTo(20));

		Product product = products.stream().min(Comparator.comparing(Product::getId)).orElseThrow(RuntimeException::new);
		assertThat(product.getId(), equalTo("p_109b160wkj"));
		assertThat(product.getName(), equalTo("New Product"));
		assertThat(product.getBrandId(), equalTo("b_d2481b88"));
		assertThat(product.getCreatedAt(), equalTo(Instant.parse("2019-02-21T16:06:06Z")));
		assertThat(product.getOptions().size(), equalTo(1));

		ProductOption option = product.getOptions().get(0);
		assertThat(option.getId(), equalTo("po_ew5a6faxdx"));
		assertThat(option.getName(), equalTo("default"));
	}

	@Test
	public void testParseOrders() throws IOException {
		FaireApi api = mockFaireApi();

		List<Order> orders = api.listOrders();
		assertThat(orders.size(), equalTo(93));

		Order order = orders.stream().min(Comparator.comparing(Order::getId)).orElseThrow(RuntimeException::new);
		assertThat(order.getId(), equalTo("bo_106svip454"));
		assertThat(order.getState(), equalTo(OrderState.PROCESSING));
		assertThat(order.getCreatedAt(), equalTo(Instant.parse("2019-03-04T22:23:42Z")));
		assertThat(order.getItems().size(), equalTo(2));

		OrderItem item = order.getItems().stream().min(Comparator.comparing(OrderItem::getId)).orElseThrow(RuntimeException::new);
		assertThat(item.getId(), equalTo("oi_9b8e275tab"));
		assertThat(item.getProductId(), equalTo("p_c2dny7bu"));
		assertThat(item.getQuantity(), equalTo(6));
	}
}
