package xyz.luan.faire.util;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static xyz.luan.faire.util.StDev.collector;

public class StDevTest {

	@Test
	public void testStdevEqualElementsIsZero() {
		Stream<Double> stream = Stream.of(1d, 1d, 1d, 1d);
		double stDev = stream.collect(collector());
		assertThat(stDev, equalTo(0d));
	}

	@Test
	public void testStdevEmptyListIsNaN() {
		double result = Stream.<Double>empty().collect(collector());
		assertThat(Double.isNaN(result), equalTo(true));
	}

	@Test
	public void testStdevFirst10Numbers() {
		checkStdevInFirstNNumbers(10);
	}

	@Test
	public void testStdevFirst20Numbers() {
		checkStdevInFirstNNumbers(20);
	}

	@Test
	public void testStdevFirst100Numbers() {
		checkStdevInFirstNNumbers(100);
	}

	@Test
	public void testStdevOnPresetNumbers() {
		Stream<Double> stream = Stream.of(4, 8, 15, 16, 23, 42).map(Double::new);
		double result = stream.collect(collector());
		assertThat(result, closeTo(12.31, 0.1));
	}

	private void checkStdevInFirstNNumbers(int n) {
		Stream<Double> stream = IntStream.rangeClosed(1, n).mapToObj(Double::new);
		double result = stream.collect(collector());
		double expected = Math.sqrt((n * n - 1.0) / 12);
		assertThat(result, equalTo(expected));
	}
}
