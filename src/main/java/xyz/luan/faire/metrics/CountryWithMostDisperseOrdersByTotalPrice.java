package xyz.luan.faire.metrics;

import lombok.Getter;
import xyz.luan.faire.model.processed.Country;
import xyz.luan.faire.model.processed.ProcessedOrder;
import xyz.luan.faire.util.StDev;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * This returns which country has the greatest standard deviation over total order cost.
 * <p>
 * This might be an indirect reflex of countries that have more income inequality.
 * This uses the population (regular) standard deviation, not the sample stdev.
 */
public class CountryWithMostDisperseOrdersByTotalPrice implements Metric<Country> {
	@Override
	public Country process(List<ProcessedOrder> orders) {
		return orders.stream()
				.collect(groupingBy(o -> new Country(o.getOrder().getAddress())))
				.entrySet().stream()
				.map(CountryOrders::new)
				.max(Comparator.comparing(CountryOrders::getStdev))
				.map(CountryOrders::getCountry)
				.orElse(null);
	}

	@Override
	public String print(Country result) {
		if (result == null) {
			return "The country with the greatest stdev over total order cost is: not found, not enough data";
		}
		return String.format("The country with the greatest stdev over total order cost is %s (%s)",
				result.getName(), result.getCode());
	}

	@Getter
	class CountryOrders {
		private Country country;
		private double stdev;

		public CountryOrders(Map.Entry<Country, List<ProcessedOrder>> entry) {
			this.country = entry.getKey();
			this.stdev = entry.getValue().stream().map(this::totalCost).collect(StDev.collector());
		}

		public double totalCost(ProcessedOrder order) {
			return order.getItems().stream().mapToInt(e -> e.getItem().getPriceCents()).sum();
		}
	}
}
