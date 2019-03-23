package xyz.luan.faire.model.processed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.OrderItem;
import xyz.luan.faire.model.order.OrderState;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingItem {
	private Order order;
	private OrderItem item;
	private Product product;
	private ProductOption option;

	public boolean noStock() {
		return requiredQuantity() > availableQuantity();
	}

	private int availableQuantity() {
		return option.getAvailableQuantity();
	}

	private int requiredQuantity() {
		return item.getQuantity();
	}

	public void markNoStock() {
		option.setBackorderedUntil(Instant.now());
	}

	public void doProcess() {
		option.removeFromStock(requiredQuantity());
		order.setState(OrderState.PROCESSING);
	}
}
