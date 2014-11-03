/** Wrappper class for an enum used to check the status of an operation.
  * @author Billy Barbaro
  */
public class SocialNetworkStatus {

	// Variable to hold the state of our enum
	private Status currentStatus;

	// The enum that contains the possible status of our operations.
	public enum Status {
		ALREADY_VALID,
		INVALID_USERS,
		INVALID_DATE,
		INVALID_DISTANCE,
		ALREADY_ACTIVE,
		ALREADY_INACTIVE,
		SUCCESS
	};

	/** Creates a new status and makes the default status success */
	public SocialNetworkStatus() {
		currentStatus = null;
	}


	/** Gets the current status
	  * @return Status 	the current status of an operation
	  */
	public Status getStatus() {
		return currentStatus;
	}

	/** Sets a new status for an operation
	  * @param newStatus 	changes the status of an operation
	  */
	public void setStatus(Status newStatus) {
		currentStatus = newStatus;
	}
}