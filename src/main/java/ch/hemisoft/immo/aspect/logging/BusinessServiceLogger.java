/**
 *
 */
package ch.hemisoft.immo.aspect.logging;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.hemisoft.immo.aspect.ExecutionKey;

@Aspect
@Component
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
	    switch(exception.getClass().getSimpleName()) {
            case "NoSuchElementException": 
            case "UserNameExistsException": 
            case "EmailExistsException": 
                LOGGER.warn(exception.getMessage());
                break;
            default:
                LOGGER.error("Exception occures! " + exception.getMessage(), exception);
	    }
	    throw exception;
	}
}
