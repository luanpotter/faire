package xyz.luan.faire.metrics;

import org.junit.Test;
import xyz.luan.faire.model.order.Carrier;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.Shipment;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheapestCarrierPerQuantityMetricsTest {

	@Test
	public void testCheapestCarrierSimpleScenario() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", Carrier.DHL_ECOMMERCE, 100, item(10)),
				createOrder("o2", Carrier.DHL_EXPRESS, 200, item(40))
		);

		Carrier carrier = new CheapestCarrierPerQuantityMetrics().process(orders);
		assertThat(carrier, equalTo(Carrier.DHL_EXPRESS));
	}

	@Test
	public void testCheapestCarrierComplexScenario() {
		List<ProcessedOrder> orders = Arrays.asList(
				createOrder("o1", Carrier.CANADA_POST, 3000, item(100), item(200)),
				createOrder("o2", Carrier.FEDEX, 20, item(1)),
				createOrder("o3", Carrier.CANADA_POST, 12, item(1)),
				createOrder("o4", Carrier.FEDEX, 200, item(10))
		);

		Carrier carrier = new CheapestCarrierPerQuantityMetrics().process(orders);
		assertThat(carrier, equalTo(Carrier.CANADA_POST));
	}

	private OrderItem item(int amount) {
		OrderItem item = new OrderItem();
		item.setQuantity(amount);
		return item;
	}

	private ProcessedOrder createOrder(String id, Carrier carrier, int shipmentPrice, OrderItem... items) {
		Shipment shipment = new Shipment();
		shipment.setCarrier(carrier);
		shipment.setMakerCostCents(shipmentPrice);

		Order order = new Order();
		order.setId(id);
		order.setShipments(singletonList(shipment));
		order.setItems(Arrays.asList(items));

		return new ProcessedOrder(order, emptyList());
	}
}
