package xyz.luan.faire;

import com.google.gson.Gson;
import xyz.luan.facade.HttpFacade;
import xyz.luan.facade.Response;
import xyz.luan.faire.model.Product;
import xyz.luan.faire.model.Products;

import java.io.IOException;
import java.net.MalformedURLException;
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
		int page = 1;
		List<Product> allProducts = new ArrayList<>();
		while (true) {
			List<Product> list = fetchSinglePage(page);
			if (list.isEmpty()) {
				break;
			}
			allProducts.addAll(list);
			page++;
		}
		return allProducts;
	}

	private List<Product> fetchSinglePage(int page) throws IOException {
		Response response = request("/products").query("limit", "250").query("page", String.valueOf(page)).get();
		String content = response.content();
		return GSON.fromJson(content, Products.class).getProducts();
	}

	private HttpFacade request(String path) throws MalformedURLException {
		return new HttpFacade(BASE_URL + path).header("X-FAIRE-ACCESS-TOKEN", apiKey);
	}
}
