package xyz.luan.faire.model;

import lombok.Data;

import java.util.List;

@Data
public class Products {

	private List<Product> products;
	private int page;
	private int limit;
}
