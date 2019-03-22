package xyz.luan.faire;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import xyz.luan.facade.HttpFacade;
import xyz.luan.facade.mock.MockedResponse;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class FaireApiTest {

	@Test
	public void testParseProducts() throws IOException {
		FaireApi api = mockedApi();

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
		FaireApi api = mockedApi();

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

	private FaireApi mockedApi() {
		return new FaireApi(null) {
			protected HttpFacade request(String path) throws IOException {
				HttpFacade request = spy(new HttpFacade(path));
				when(request.get()).then((InvocationOnMock obj) -> {
					String url = ((HttpFacade) obj.getMock()).getUrl();
					String mockFileName = url
							.replaceAll("^http:///", "")
							.replaceAll("[?&]", "-");
					String content = fileContent(mockFileName);
					return new MockedResponse().withStatus(200).withContent(content);
				});
				return request;
			}
		};
	}

	private String fileContent(String mockFileName) {
		InputStream stream = FaireApiTest.class.getResourceAsStream("/" + mockFileName);
		return new BufferedReader(new InputStreamReader(stream)).lines().collect(joining("\n"));
	}
}
