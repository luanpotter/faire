package xyz.luan.faire.core;

import org.junit.Test;
import xyz.luan.faire.core.Faire;
import xyz.luan.faire.core.FaireApi;
import xyz.luan.faire.core.FaireMetrics;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static xyz.luan.faire.util.TestUtils.fileContent;
import static xyz.luan.faire.util.TestUtils.mockFaireApi;

public class FaireMetricsTest {

	@Test
	public void testWithNoOrders() {
		List<ProcessedOrder> orders = emptyList();
		String process = new FaireMetrics(orders).process();

		boolean allNotFound = Arrays.stream(process.split("\n")).allMatch(s -> s.contains("not found"));
		assertThat(allNotFound, equalTo(true));
	}

	@Test
	public void testWithMockedData() throws IOException {
		FaireApi faireApi = mockFaireApi();
		Faire faire = new Faire(faireApi);

		List<ProcessedOrder> orders = faire.fetchAndProcessOrders();
		String result = new FaireMetrics(orders).process();

		String expected = fileContent("expected-results");
		assertThat(result, equalTo(expected));
	}
}
