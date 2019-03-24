package xyz.luan.faire.model.processed;

import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.luan.faire.model.order.Address;

@Data
@NoArgsConstructor
public class State {

	private String name;

	private String code;

	public State(Address address) {
		this.name = address.getState();
		this.code = address.getStateCode();
	}
}
