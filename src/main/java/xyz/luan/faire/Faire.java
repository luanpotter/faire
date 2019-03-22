package xyz.luan.faire;

public class Faire {

	private static final String BASE_URL = "https://www.faire-stage.com";
	private static final String BRAND_TOKEN = "b_d2481b88";

	private final String apiKey;

	public Faire(String apiKey) {
		this.apiKey = apiKey;
	}

	public void run() {
		System.out.println("Running for apiKey " + apiKey);
	}
}
