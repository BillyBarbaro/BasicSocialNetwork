/** An exception thrown when a method is called on an object that has not been initialized
  * @author Billy Barbaro
  */

public class UninitializedObjectException extends Exception {

	/** Constructor creating an instance of the exception */
	public UninitializedObjectException() {
		super();
	}

	/** Constructor creating an instance of the exception that displays a message
    * @param message  the string to be displayed when the exception is thrown
    */
	public UninitializedObjectException(String message) {
		super(message);
	}

	/** Constructor creating an instance of the exception that displays a message and provides the stack
    * @param message  the string to be displayed when the exception is thrown
    * @param cause	the execution stack at the time of the error
    */
	public UninitializedObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	/** Constructor creating an instance of the exception that provides the stack
    * @param cause	the execution stack at the time of the error
    */
	public UninitializedObjectException(Throwable cause) {
		super(cause);
	}
}