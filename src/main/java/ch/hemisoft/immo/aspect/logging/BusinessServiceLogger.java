/**
 *
 */
package ch.hemisoft.immo.aspect.logging;

import java.util.Date;
import java.util.NoSuchElementException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hemisoft.immo.aspect.ExecutionKey;

@Aspect
public class BusinessServiceLogger extends _AbstractLogger{
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceLogger.class);
	private static final String ALL = ExecutionKey.BUSINESS_SERVICE_EXECUTION;

	@Around(ALL)
	public Object logMethod(final ProceedingJoinPoint point) throws Throwable {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(getBeforeString(point));
		}

		final long date = new Date().getTime();
		final Object object = point.proceed();
		final long duration = new Date().getTime() - date;
		
		if (object != null && LOGGER.isInfoEnabled()) {
			LOGGER.info(getAfterString(point, object) + " done in " + duration + " millis.");
		}
		return object;
	}

	@AfterThrowing(pointcut = ALL, throwing = "exception")
	public void logAfterThrowing(final Exception exception) throws Throwable {
		LOGGER.error("Exception occures! " + exception.getMessage(), exception);
		throw exception;
	}

	@AfterThrowing(pointcut = ALL, throwing = "exception")
	public void logAfterThrowing(final NoSuchElementException exception) throws Throwable {
		LOGGER.warn(exception.getMessage());
		throw exception;
	}
}
