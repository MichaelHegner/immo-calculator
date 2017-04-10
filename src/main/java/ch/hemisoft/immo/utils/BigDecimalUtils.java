package ch.hemisoft.immo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalUtils {
	public static BigDecimal convert(double number) {
		if (Double.isInfinite(number) || Double.isNaN(number)) {
			return BigDecimal.ZERO;
		} else {
			BigDecimal temp = BigDecimal.valueOf(number);
			return temp.setScale(2, RoundingMode.HALF_UP);
		}
	}
	
	private BigDecimalUtils(){}
}
