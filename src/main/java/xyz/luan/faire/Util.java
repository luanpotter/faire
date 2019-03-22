package xyz.luan.faire;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@UtilityClass
public class Util {

	public <T> Function<Optional<T>, Stream<? extends T>> nonEmpty() {
		return o -> o.map(Stream::of).orElseGet(Stream::empty);
	}
}
