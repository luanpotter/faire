package xyz.luan.faire.model.processed;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.luan.faire.model.order.Order;

import java.util.List;

@Data
@AllArgsConstructor
public class ProcessedOrder {

	private Order order;

	private List<ProcessingItem> items;
}
