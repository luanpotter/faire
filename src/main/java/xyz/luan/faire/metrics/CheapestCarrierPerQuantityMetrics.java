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
 * <p>
 * For each Carrier, it makes an average of every shipment price divided by the amount of goods that were shipped.
 * Then, it gets the Carrier with the smallest average.
 * Of course it assumes all unity of items are similar in size and weight to each other, but that's just an estimate.
 */
public class CheapestCarrierPerQuantityMetrics implements Metric<Carrier> {

	@Override
	public Carrier process(List<ProcessedOrder> orders) {
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

	@Override
	public String print(Carrier carrier) {
		if (carrier == null) {
			return "The on average cheapest carrier per quantity: not found, no orders with carriers";
		}
		return "The on average cheapest carrier per quantity: " + carrier;
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
