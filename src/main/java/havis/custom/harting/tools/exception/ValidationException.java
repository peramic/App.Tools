package havis.custom.harting.tools.exception;

/**
 * Should be used on validation exceptions.
 * 
 */
public class ValidationException extends Exception {

	/**
	 * Auto generated Serial version ID
	 */
	private static final long serialVersionUID = -3501820874210899862L;

	/**
	 * Constructor, initializes the instance properties.
	 * 
	 * @param message
	 *            to set
	 */
	public ValidationException(final String message) {
		super(message);
	}
}

