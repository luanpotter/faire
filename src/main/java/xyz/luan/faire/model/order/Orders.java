package xyz.luan.faire.model.order;

import lombok.Data;

import java.util.List;

@Data
public class Orders {

	private List<Order> orders;

	private int page;

	private int limit;
}
