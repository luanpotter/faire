package xyz.luan.faire.model.processed;

import lombok.Data;
import xyz.luan.faire.model.order.Address;

@Data
public class Country {

	private String code;

	private String name;

	public Country(Address address) {
		this.code = address.getCountryCode();
		this.name = address.getCountry();
	}
}
