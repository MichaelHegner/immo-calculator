package ch.hemisoft.immo.aspect.logging;

public final class ExecutionKey {
	private static final String APP_PACKAGE 		= "ch.hemisoft.immo";
	private static final String BUSINESS_SERVICE 	= APP_PACKAGE + "..service..*Service+.*(..)";
	private static final String BACKEND_SERVICE 	= APP_PACKAGE + "..backend..*Service+.*(..)";
	private static final String REPOSITORY 			= APP_PACKAGE + "..backend..*Repository+.*(..)";
	private static final String CONTROLLER 			= APP_PACKAGE + "..controller..*Controller.*(..)";
	
	public static final String BUSINESS_SERVICE_EXECUTION = "execution(* " + BUSINESS_SERVICE + ")";
	public static final String BACKEND_SERVICE_EXECUTION = "execution(* " + BACKEND_SERVICE + ")";
	public static final String REPOSITORY_EXECUTION = "execution(* " + REPOSITORY + ")";
	public static final String CONTROLLER_EXECUTION = "execution(* " + CONTROLLER + ")";

	private ExecutionKey() {
	}
}
