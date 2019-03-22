package xyz.luan.faire.model.order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;

@Data
public class OrderItem {

	private String id;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("product_option_id")
	private String productOptionId;

	private int quantity;

	private String sku;

	@SerializedName("price_cents")
	private int priceCents;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("product_option_name")
	private String productOptionName;

	@SerializedName("includes_tester")
	private boolean includesTester;

	@SerializedName("created_at")
	private Instant createdAt;

	@SerializedName("updated_at")
	private Instant updatedAt;
}
