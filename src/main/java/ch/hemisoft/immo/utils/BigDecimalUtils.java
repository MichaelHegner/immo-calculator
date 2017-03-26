package ch.hemisoft.immo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalUtils {
	public static BigDecimal convert(double number) {
		BigDecimal temp = BigDecimal.valueOf(number);
		return temp.setScale(2, RoundingMode.HALF_UP);
	}
	
	private BigDecimalUtils(){}
}
