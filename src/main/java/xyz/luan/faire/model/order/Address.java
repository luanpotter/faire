package xyz.luan.faire.model.order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Address {

	private String name;

	private String address1;

	private String address2;

	@SerializedName("postal_code")
	private String postalCode;

	private String city;

	@SerializedName("state_code")
	private String stateCode;

	@SerializedName("phone_number")
	private String phoneNumber;

	private String country;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("company_name")
	private String companyName;
}
