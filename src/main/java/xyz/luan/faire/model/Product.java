package xyz.luan.faire.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Product {

	private String id;

	@SerializedName("brand_id")
	private String brandId;

	@SerializedName("short_description")
	private String shortDescription;

	private String description;

	@SerializedName("wholesale_price_cents")
	private int wholesalePriceCents;

	@SerializedName("retail_price_cents")
	private int retailPriceCents;

	private boolean active;

	private String name;

	@SerializedName("unit_multiplier")
	private int unitMultiplier;

	private List<ProductOption> options;

	@SerializedName("created_at")
	private Instant createdAt;

	@SerializedName("updated_at")
	private Instant updatedAt;
}
