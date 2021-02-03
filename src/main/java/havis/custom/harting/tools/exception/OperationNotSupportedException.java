package havis.custom.harting.tools.exception;

public class OperationNotSupportedException extends Exception {

	private static final long serialVersionUID = -2216165030742692703L;

	/**
	 * Constructor, initializes the instance properties.
	 * 
	 * @param message
	 *            to set
	 */
	public OperationNotSupportedException(final String message) {
		super(message);
	}
}

