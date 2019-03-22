package xyz.luan.faire.model.order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;

@Data
public class Shipment {

	private String id;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("maker_cost_cents")
	private int makerCostCents;

	private Carrier carrier;

	@SerializedName("tracking_code")
	private String tackingCode;

	@SerializedName("created_at")
	private Instant createdAt;

	@SerializedName("updated_at")
	private Instant updatedAt;
}
