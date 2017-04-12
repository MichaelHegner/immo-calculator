package ch.hemisoft.immo.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

public final class ListStreamUtils {
	public static Integer sumInteger(Supplier<List<Integer>> supplier) {
		return supplier.get().stream().reduce(0, (l, r) -> l + r);
	}

	public static BigDecimal sumBigDecimal(Supplier<List<BigDecimal>> supplier) {
		return supplier.get().stream().reduce(BigDecimalUtils.convert(0.0), (l, r) -> l.add(r));
	}

	private ListStreamUtils() {
	}
}
