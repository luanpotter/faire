package xyz.luan.faire;

import org.junit.Test;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.product.Product;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class FaireTest {

	@Test
	public void testFaireRun() throws IOException {
		Faire faire = mockFaire(Arrays.asList(), Arrays.asList());
		faire.run();
	}

	private Faire mockFaire(List<Product> products, List<Order> orders) throws IOException {
		Faire faire = spy(new Faire());

		FaireApi api = spy(new FaireApi(null));
		when(api.listOrders()).thenReturn(orders);
		when(api.listProducts()).thenReturn(products);

		when(faire.getApi()).thenReturn(api);
		return faire;
	}
}
