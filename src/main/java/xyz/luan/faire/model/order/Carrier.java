package xyz.luan.faire.model.order;

import com.google.gson.annotations.SerializedName;

public enum Carrier {

	@SerializedName("canada_post")
	CANADA_POST,

	@SerializedName("dhl_ecommerce")
	DHL_ECOMMERCE,

	@SerializedName("dhl_express")
	DHL_EXPRESS,

	@SerializedName("fedex")
	FEDEX,

	@SerializedName("purolator")
	PUROLATOR,

	@SerializedName("ups")
	UPS,

	@SerializedName("usps")
	USPS,

	@SerializedName("postnl")
	POSTNL
}
