package xyz.luan.faire;

import org.junit.Test;
import xyz.luan.faire.core.FaireMetrics;
import xyz.luan.faire.model.processed.ProcessedOrder;

import java.util.List;

import static java.util.Collections.emptyList;

public class FaireMetricsTest {

	@Test
	public void testWithNoOrders() {
		List<ProcessedOrder> orders = emptyList();
		new FaireMetrics(orders).run();
	}
}
