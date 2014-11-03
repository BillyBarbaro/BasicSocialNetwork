/** Class representing a user in a social network.  Users are meant to be connected in pairs with links.
  * @author Billy Barbaro
  */

public class User implements SocialNetworkObject {

   private String id;
   private String firstName;
   private String middleName;
   private String lastName;
   private String email;
   private String phoneNumber;
   
   /** Constructor that creates a new invalid user */
   public User() {
      super();
   }

   /** Verifies that a string can be used to set a field
    * @param check  the string to be added to a field
    * @param type  a string specifying the method that called this. Customizes the exception message
    * @throws NullPointerException  called if the string is null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    */
   private void verify(String check, String type) throws UninitializedObjectException{
      SocialNetworkUtility.checkNull(check, type);
      SocialNetworkUtility.checkValid(this, "User", type);
   }

   /** Sets a user's ID.  Can only be done once and the ID may not be an empty string
    * @param id  a String representing the user's id
    * @throws NullPointerException  called if the id is being set to null
    * @return boolean tells whether or not the user's ID was set
    */
   public boolean setID(String id) throws NullPointerException {

      SocialNetworkUtility.checkNull(id, "ID");
      // Here we handle the cases in which the user is not permitted to set an ID.
      if (this.isValid() || id.equals("")) {
         return false;
      }
      this.id = id;
      return true;
   }

   /** Gets the ID of the user.
    * @return String  The user's ID.  If it has not yet been set, it is null.
    */
   public String getID() {
      return id;
   }

   /** Sets a user's first name. May not be an empty string. User must have an ID
    * @param name  a String representing the user's first name
    * @throws NullPointerException  called if first name is being set to null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    * @return User the user who's first name was set.
    */
   public User setFirstName(String name) throws UninitializedObjectException {
      this.verify(name, "First Name");
      this.firstName = name;
      return this;
   }

   /** Gets the first name of the user.
    * @return String  The user's first name.  If it has not yet been set, it is null.
    */
   public String getFirstName() {
      return firstName;
   }

   /** Sets a user's middle name. May not be an empty string. User must have an ID
    * @param name  a String representing the user's middle name
    * @throws NullPointerException  called if middle name is being set to null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    * @return User the user who's middle name was set.
    */
   public User setMiddleName(String name) throws UninitializedObjectException {
      this.verify(name, "Middle Name");
      this.middleName = name;
      return this;
   }

   /** Gets the middle name of the user.
    * @return String  The user's middle name.  If it has not yet been set, it is null.
    */
   public String getMiddleName() {
      return middleName;
   }

   /** Sets a user's last name. May not be an empty string. User must have an ID
    * @param name  a String representing the user's last name
    * @throws NullPointerException  called if last name is being set to null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    * @return User the user who's last name was set.
    */
   public User setLastName(String name) throws UninitializedObjectException {
      this.verify(name, "Last Name");
      this.lastName = name;
      return this;
   }

   /** Gets the last name of the user.
    * @return String  The user's last name.  If it has not yet been set, it is null.
    */
   public String getLastName() {
      return lastName;
   }

   /** Sets a user's email. May not be an empty string. User must have an ID
    * @param email  a String representing the user's email
    * @throws NullPointerException  called if email is being set to null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    * @return User the user who's email was set.
    */
   public User setEmail(String email) throws UninitializedObjectException{
      this.verify(email, "Email");
      this.email = email;
      return this;
   }

   /** Gets the email of the user.
    * @return String  The user's emails.  If it has not yet been set, it is null.
    */
   public String getEmail() {
      return email;
   }

   /** Sets a user's phone number. May not be an empty string. User must have an ID
    * @param number  a String representing the user's phone number
    * @throws NullPointerException  called if phoneNumber is being set to null
    * @throws UninitializedObjectExcpetion  called if the user is not valid
    * @return User the user who's phone number was set.
    */
   public User setPhoneNumber(String number) throws UninitializedObjectException{
      this.verify(number, "Phone Number");
      this.phoneNumber = number;
      return this;
   }

   /** Gets the phone number of the user.
    * @return String  The user's phone number.  If it has not yet been set, it is null.
    */
   public String getPhoneNumber() {
      return phoneNumber;
   }

   /** Tells if a user is valid
    * @return boolean  True if the user is valid.  False if it is invalid.
    */
   public boolean isValid() {
    return id != null;
   }

   /** Gives a readable representation of the user
    * @return String  Tells user's information.  If invalid, string says "Invlaid User"
    */
   @Override
   public String toString() {

      String userString = new String();

      // If the user is valid, we construct a string displaying their information
      if (this.isValid())
         userString = String.format("User: %s First Name: %s Middle Name: %s Last Name: %s Email: %s Phone Number: %s", this.getID(), this.getFirstName(), this.getMiddleName(), this.getLastName(), this.getEmail(), this.getPhoneNumber());
      else
         userString = "Invalid User: Uninitialized ID";

      return userString;
   }

   /** Compares two users by their IDs
    * @param o  an object to have its equality tested with this User.
    * @return boolean tells whether or not object is equal to the user.
    */
   @Override
   public boolean equals(Object o) {
      // The object must be a User to be equal to a User
      if (!(o instanceof User))
         return false;

      // Cast the object into a user so we can get its ID
      User otherUser = (User)o;
      String otherID = otherUser.getID();

      // We first check that this user's ID is not null. The overloading of the && operator will prevent us from calling .equals on a null pointer.
      return this.isValid() && this.getID().equals(otherID);
   }

   /** Generates a hash for the user based off ID so they can be stored in a HashSet
    * @return int the hash for the user
    */
   @Override
   public int hashCode() {
      if(!this.isValid())
         return 0;
      return this.getID().hashCode();
   }
}