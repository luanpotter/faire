package xyz.luan.faire.model.product;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

	public Optional<ProductOption> findOptionById(String id) {
		return getOptions().stream().filter(o -> o.getId().equals(id)).findFirst();
	}
}
