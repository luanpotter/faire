package xyz.luan.faire.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Collections.emptySet;

/**
 * Can be used to generate a Collector that calculates the (population) standard deviation of a Stream.
 * <p>
 * This works online and uses the Welford's algorithm.
 */
public class StDev {

	private int count;
	private double mean;
	private double dSquared;

	public void add(double newValue) {
		count++;
		double meanDifferential = (newValue - mean) / count;
		double newMean = mean + meanDifferential;
		double dSquaredIncrement = (newValue - newMean) * (newValue - mean);
		double newDSquared = dSquared + dSquaredIncrement;
		mean = newMean;
		dSquared = newDSquared;
	}

	public StDev add(StDev n2) {
		StDev n1 = this;

		StDev result = new StDev();
		result.count = n1.count + n2.count;
		result.mean = (n1.count * n1.mean + n2.count * n2.mean) / result.count;

		double variance = n1.getVarianceIncrement(result.mean) + n2.getVarianceIncrement(result.mean);
		result.dSquared = result.count * variance;

		return result;
	}

	private double getVarianceIncrement(double totalMean) {
		return count * (getVariance() + square(mean - totalMean));
	}

	private static double square(double x) {
		return Math.pow(x, 2);
	}

	double getVariance() {
		return this.dSquared / this.count;
	}

	double getStDev() {
		return Math.sqrt(getVariance());
	}

	public static Collector<Double, StDev, Double> collector() {
		return new Collector<Double, StDev, Double>() {
			@Override
			public Supplier<StDev> supplier() {
				return StDev::new;
			}

			@Override
			public BiConsumer<StDev, Double> accumulator() {
				return StDev::add;
			}

			@Override
			public BinaryOperator<StDev> combiner() {
				return StDev::add;
			}

			@Override
			public Function<StDev, Double> finisher() {
				return StDev::getStDev;
			}

			@Override
			public Set<Characteristics> characteristics() {
				return emptySet();
			}
		};
	}

}
