/** Class representing the friend of a user in a social network.
  * @author Billy Barbaro
  */
public class Friend implements SocialNetworkObject {
	
	private User user;

	/** The number of links it takes to get to the friend. Directly linked friends have a distance of 0 */
	private int distance;

	/** A constructor that creates a new invalid friend */
	public Friend() {
		super();
	}

	/** Determine that the fields of a Friend have been initialized.
    * @return boolean  True if the fields have both been set
    */
	public boolean isValid() {
		return user != null && distance >= 0;
	}

	/** Sets the User corresponding the the Friend and their distance
	  * @param user 	the user to be set as the friend
	  * @param distance 	the number of links between the two users
	  * @return boolean	true if the user and distance were set.
	  */
	public boolean set(User user, int distance) {

		SocialNetworkUtility.checkNull(user, "User");
		SocialNetworkUtility.checkNull(distance, "Distance");

		// Check that everything required is valid
		if (!this.isValid() && user.isValid() && distance >=0) {
			this.user = user;
			this.distance = distance;
			return true;
		}
		return false;
	}

	/** Gives the user associated with the Friend
	  * @throws UninitializedObjectException 	if the Friend is not valid
	  * @return User 	the user associated with this Friend
	  */
	public User getUser() throws UninitializedObjectException {
		SocialNetworkUtility.checkValid(this, "Friend", "User");
		return user;
	}

	/** Gives the distance associated with the Friend
	  * @throws UninitializedObjectException 	if the Friend is not valid
	  * @return int 	the distance associated with this Friend
	  */
	public int getDistance() throws UninitializedObjectException {
		SocialNetworkUtility.checkValid(this, "Friend","Distance");
		return distance;
	}

	/** Creates a readable string corresponding to the user
	  * @return String 	Tells the User and distance of this friend. If not set, then return "Invalid Friend"
	  */
	@Override
	public String toString() {
		if (this.isValid())
			return user.getID() + " is " + distance + " users away from you.";
		else
			return "Invalid Friend";
	}

	/** Creates a hashCode for the Friend so it can be stored in a hashSet. Friends with the same user get the same hashcode because the same User should not be two different distances from a given User
	  * @return int 	the Friend's hashCode. 0 if the friend is invalid
	  */
	@Override
	public int hashCode() {
		if (!this.isValid())
			return 0;
		return user.getID().hashCode(); // Using this because friends should be unique by users.
	}

	/** A helper method to abstract away the try/catch of Friend.getUser()
	  * @param otherFriend 	the friend to have their User extracted.  We know it is valid
	  * @return User 	the User that is associated with otherFriend
	  */
	private User getOtherUser(Friend otherFriend) {
		try {
			return otherFriend.getUser();
      }
      catch (UninitializedObjectException uoe) { // This case will not be hit because before calling this method we determine otherFriend is valid.
      	assert false;
      }
      return null;
	}

	/** Compares two Friends by their Users.  Distance is not taken into account, because if different the friend is still with the same User.
    * @param o  an object to have its equality tested with this Friend.
    * @return boolean tells whether or not object is equal to the Friend.
    */
	@Override
	public boolean equals(Object o) {
		// The object must be a Friend to be equal to a Friend
      if (!(o instanceof Friend))
         return false;

      // Cast the object into a friend so we can get its User
      Friend otherFriend = (Friend)o;
      User otherUser = null;

      // In order for Friends to be equal, they both must be equal.
      if (otherFriend.isValid()) {
      	otherUser = getOtherUser(otherFriend);

      	// We first check that this Friend is valid. The overloading of the && operator will prevent us from calling .equals on a null pointer.
      	try {
      		return this.isValid() && this.getUser().equals(otherUser);
      	}
      	catch (UninitializedObjectException uoe) { // We are assured this case will not occur because we've check both user's are valid
      		assert false;
      	}
      }
      return false;
	}
}