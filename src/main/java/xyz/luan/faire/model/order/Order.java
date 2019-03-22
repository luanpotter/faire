package xyz.luan.faire.model.order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Order {

	private String id;

	private OrderState state;

	@SerializedName("ship_after")
	private Instant shipAfter;

	private List<OrderItem> items;

	private List<Shipment> shipments;

	private Address address;

	@SerializedName("created_at")
	private Instant createdAt;

	@SerializedName("updated_at")
	private Instant updatedAt;
}
