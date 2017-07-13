package ch.hemisoft.commons;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public final class CollectionUtils {

	public static <T> BigDecimal reduce(List<T> list, Function<T, BigDecimal> mapToBigdecimalFunction) {
		return list.stream().map(mapToBigdecimalFunction).reduce(ZERO, (l, r) -> l.add(r));
	}
	
	private CollectionUtils() {}
}
