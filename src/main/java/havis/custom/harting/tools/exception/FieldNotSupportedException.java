package havis.custom.harting.tools.exception;

public class FieldNotSupportedException extends Exception {
	private static final long serialVersionUID = 8220849241130459984L;

	/**
	 * Constructor, initializes the instance properties.
	 * 
	 * @param message
	 *            to set
	 */
	public FieldNotSupportedException(final String message) {
		super(message);
	}
}

