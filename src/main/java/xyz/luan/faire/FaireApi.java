package xyz.luan.faire;

import com.google.gson.Gson;
import xyz.luan.facade.HttpFacade;
import xyz.luan.facade.Response;
import xyz.luan.faire.model.order.Order;
import xyz.luan.faire.model.order.Orders;
import xyz.luan.faire.model.product.Product;
import xyz.luan.faire.model.product.Products;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaireApi {

	private static final Gson GSON = GsonUtils.build();
	private static final String BASE_URL = "https://www.faire-stage.com/api/v1";

	private final String apiKey;

	public FaireApi(String apiKey) {
		this.apiKey = apiKey;
	}

	public List<Product> listProducts() throws IOException {
		return fetchAllPages(this::fetchSingleProductPage);
	}

	public List<Order> listOrders() throws IOException {
		return fetchAllPages(this::fetchSingleOrderPage);
	}

	private <T> List<T> fetchAllPages(Pager<T> p) throws IOException {
		int page = 1;
		List<T> allItems = new ArrayList<>();
		while (true) {
			List<T> list = p.fetch(page);
			if (list.isEmpty()) {
				break;
			}
			allItems.addAll(list);
			page++;
		}
		return allItems;
	}

	@FunctionalInterface
	private interface Pager<T> {
		List<T> fetch(int page) throws IOException;
	}

	private List<Product> fetchSingleProductPage(int page) throws IOException {
		Response response = request("/products").query("limit", "250").query("page", String.valueOf(page)).get();
		return GSON.fromJson(response.content(), Products.class).getProducts();
	}

	private List<Order> fetchSingleOrderPage(int page) throws IOException {
		Response response = request("/orders").query("limit", "50").query("page", String.valueOf(page)).get();
		return GSON.fromJson(response.content(), Orders.class).getOrders();
	}

	protected HttpFacade request(String path) throws IOException {
		return new HttpFacade(BASE_URL + path).header("X-FAIRE-ACCESS-TOKEN", apiKey);
	}
}
