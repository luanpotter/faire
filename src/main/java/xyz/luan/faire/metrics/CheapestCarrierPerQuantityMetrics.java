package xyz.luan.faire.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.luan.faire.model.order.Carrier;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.Shipment;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * This Metric determines, on average, what is the cheapest carrier per unit amount of product.
 */
public class CheapestCarrierPerQuantityMetrics {

	public Carrier run(List<ProcessedOrder> orders) {
		return orders.stream()
				.flatMap(e -> e.getOrder().getShipments().stream().map(s -> new ShipmentOrder(s, e.getOrder())))
				.filter(e -> e.getShipment().getCarrier() != null)
				.collect(groupingBy(o -> o.getShipment().getCarrier()))
				.entrySet().stream()
				.map(CarrierGroup::new)
				.min(Comparator.comparing(CarrierGroup::getAveragePrice))
				.map(CarrierGroup::getCarrier)
				.orElse(null);
	}

	@Getter
	@AllArgsConstructor
	class ShipmentOrder {
		private Shipment shipment;
		private Order order;
	}

	@Getter
	class CarrierGroup {
		private Carrier carrier;
		private double averagePrice;

		public CarrierGroup(Map.Entry<Carrier, List<ShipmentOrder>> entries) {
			this.carrier = entries.getKey();
			this.averagePrice = entries.getValue().stream().mapToDouble(this::averagePrice).average().orElse(Double.MAX_VALUE);
		}

		private double averagePrice(ShipmentOrder e) {
			return e.getShipment().getMakerCostCents() / amount(e);
		}

		private double amount(ShipmentOrder shipmentOrder) {
			return shipmentOrder.getOrder().getItems().stream().mapToInt(OrderItem::getQuantity).sum();
		}
	}
}
