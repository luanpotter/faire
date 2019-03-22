package xyz.luan.faire;

public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Invalid usage, you must provide exactly one argument. E.g.:");
			System.err.println("faire <api_key>");
			System.exit(1);
		}
		String apiKey = args[0];
		new Faire(apiKey).run();
	}
}
