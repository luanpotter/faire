package xyz.luan.faire.setup;

import lombok.experimental.UtilityClass;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.ProductOption;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

@UtilityClass
public class TestUtils {

	public static String fileContent(String mockFileName) {
		InputStream stream = TestUtils.class.getResourceAsStream("/" + mockFileName);
		return new BufferedReader(new InputStreamReader(stream)).lines().collect(joining("\n"));
	}

	public static Product createToaster() {
		ProductOption option = new ProductOption();
		option.setId("po_toaster");
		option.setAvailableQuantity(10);
		option.setActive(true);
		option.setProductId("p_toaster");

		Product product = new Product();
		product.setId("p_toaster");
		product.setName("Toaster Bonanza");
		product.setOptions(singletonList(option));
		return product;
	}

	public static Product createSwitch() {
		ProductOption neon = new ProductOption();
		neon.setId("po_switch_neon");
		neon.setAvailableQuantity(2);
		neon.setActive(true);
		neon.setProductId("p_switch");

		ProductOption grey = new ProductOption();
		grey.setId("po_switch_grey");
		grey.setAvailableQuantity(3);
		grey.setActive(true);
		grey.setProductId("p_switch");

		Product product = new Product();
		product.setId("p_switch");
		product.setName("Switch");
		product.setOptions(asList(neon, grey));
		return product;
	}
}
