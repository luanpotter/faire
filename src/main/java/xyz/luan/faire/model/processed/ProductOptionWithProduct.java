package xyz.luan.faire.model.processed;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

@Data
@AllArgsConstructor
public class ProductOptionWithProduct {

	private ProductOption option;

	private Product product;
}
