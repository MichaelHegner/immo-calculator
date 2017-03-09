package ch.hemisoft.immo.aspect.logging;

import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.substring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

abstract class _AbstractLogger {
	private static final int STRING_MAX_LENGTH = 1000;

	protected String getAfterString(final ProceedingJoinPoint point, final Object object) {
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Exit ");
		stringBuffer.append(getLogString(point));
		stringBuffer.append(" Returns ");
		populateClassName(object, stringBuffer);
		return stringBuffer.toString();
	}

	protected String getBeforeString(final ProceedingJoinPoint point) {
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Enter ");
		stringBuffer.append(getLogString(point));
		stringBuffer.append(getLogArguments(point));
		return stringBuffer.toString();
	}

	private String getLogString(final ProceedingJoinPoint point) {
		final StringBuffer stringBuffer = new StringBuffer();
		final Signature signature = point.getSignature();
		stringBuffer.append(signature.getDeclaringType().getSimpleName());
		stringBuffer.append('.');
		stringBuffer.append(signature.getName());
		return stringBuffer.toString();
	}

	private String getLogArguments(final ProceedingJoinPoint point) {
		final StringBuffer stringBuffer = new StringBuffer();
		final Object[] args = point.getArgs();

		stringBuffer.append('(');
		int counter = 0; // for ',' control between parameters
		for (final Object object : args) {
			counter++;
			if (object == null) {
				stringBuffer.append("null:null");
			} else {
				populateClassName(object, stringBuffer);
			}

			if (counter < args.length) {
				stringBuffer.append(", ");
			}
		}
		stringBuffer.append(')');

		return stringBuffer.toString();
	}

	private void populateClassName(final Object object, final StringBuffer stringBuffer) {
		stringBuffer.append(object.getClass().getSimpleName());
		stringBuffer.append(':');
		stringBuffer.append(cutString(object.toString()));
	}

	private String cutString(final String objectString) {
		return (length(objectString) > STRING_MAX_LENGTH) 
				? substring(objectString, 0, STRING_MAX_LENGTH) + " ..."
				: objectString;
	}

}
