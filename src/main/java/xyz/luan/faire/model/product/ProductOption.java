package xyz.luan.faire.model.product;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;

@Data
public class ProductOption {

	private String id;

	@SerializedName("product_id")
	private String productId;

	private boolean active;

	private String name;

	private String sku;

	@SerializedName("available_quantity")
	private int availableQuantity;

	@SerializedName("backordered_until")
	private Instant backorderedUntil;

	@SerializedName("created_at")
	private Instant createdAt;

	@SerializedName("updated_at")
	private Instant updatedAt;

	public void removeFromStock(int amount) {
		this.availableQuantity -= amount;
	}
}
