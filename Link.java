import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/** Class representing a link between two users in a social network with the ability to establish, tear down, and re-establish the link by date.
  * @author Billy Barbaro
  */

public class Link implements SocialNetworkObject {

   /** A set of 2 users whom the link is between */
   private Set<User> users;
   
   /** A time ordered list of events between the users.
    *  We can assume each event at an even index is an establish and each event at an odd index is a tear down, 
    *  because a link must first be established to be torn down.
    */
   private ArrayList<Date> events;

   /** Creates a new invalid link */
   public Link() {
      super();
      events = new ArrayList<Date>();
   }

   /** Takes a set of Users to be added to the link and checks for errors
    * @param toCheck   a set of users to be checked
    * @param status    a status variable to tell the error if the method return false
    * @return boolean   true if users can be added. False otherwise
    */
   private boolean checkUsers(Set<User> toCheck, SocialNetworkStatus status) {

      // Link must not already have users
      if (this.isValid()) {
         status.setStatus(SocialNetworkStatus.Status.ALREADY_VALID);
         return false;
      }
      // A link must be exclusive between two users
      if (toCheck.size() != 2) {
         status.setStatus(SocialNetworkStatus.Status.INVALID_USERS);
         return false;
      }
      // All users passed to the link must be valid
      for (User u : toCheck) {
         if (!u.isValid()) {
            status.setStatus(SocialNetworkStatus.Status.INVALID_USERS);
            return false;
         }
      }

      return true;
   }

   /** Specifies the two users which the link is between
    * @param users  a Set<User> of 2 valid users the link connects
    * @param status a status variable to notify the method calling this of the state after completion
    * @throws NullPointerException  occurs when either variable is null
    */
   public void setUsers(Set<User> users, SocialNetworkStatus status) {

      // Verifies no varibale are null. If they are, throws an exception
      SocialNetworkUtility.checkNull(users, "Users");
      SocialNetworkUtility.checkNull(status, "Status");

      // If the users don't check, we simply return with the failure information contained in the status object
      if (!checkUsers(users, status))
         return;

      this.users = users;
      status.setStatus(SocialNetworkStatus.Status.SUCCESS);
   }

   /** Returns if the link is valid
    * @return boolean  shows if the link is valid
    */
   public boolean isValid() {
      return users != null;
   }

   /** Gives the users associated with the link
    * @throws UninitializedObjectException  occurs when the method is called on an invalid link
    * @return Set<User>  a set of the two users associated with the link
    */
   public Set<User> getUsers() throws UninitializedObjectException{
      SocialNetworkUtility.checkValid(this, "Link", "Users");
      return users;
   }

   /** Handles the different actions for establish/tearDown if the events list is empty
     * @param date   the date to be set
     * @param status    records the exit statue of the method
     * @param call   specifies the caller of the helper method
     */
   private void manipulateEmptyLink(Date date, SocialNetworkStatus status, SocialNetworkUtility.Caller call) {
      if (call.equals(SocialNetworkUtility.Caller.ESTABLISH)) { // In the case establish called it, we go ahead and add the date
         events.add(date);
         status.setStatus(SocialNetworkStatus.Status.SUCCESS);
      }
      else { // We can't tear down a link before it's active
         status.setStatus(SocialNetworkStatus.Status.ALREADY_INACTIVE);
      }
   }

   /** Sets the status variable to the correct state if the link is in the incorrect state
     * @param status    the status variable to be set
     * @param call   specifies the caller of the helper method
     */
   private void alreadySet(SocialNetworkStatus status, SocialNetworkUtility.Caller call) {
      if (call.equals(SocialNetworkUtility.Caller.ESTABLISH))
         status.setStatus(SocialNetworkStatus.Status.ALREADY_ACTIVE);
      else
         status.setStatus(SocialNetworkStatus.Status.ALREADY_INACTIVE);
   }

   /** Helper method for establish/tearDown.
     * @param date   the Date for which the action is to take place
     * @param status    a status variable to notify the caller of the exit status
     * @param call   specifies the caller of the helper method
     * @throws UninitializedObjectException  if the current link is not valid
     */
   private void manipulateLink(Date date, SocialNetworkStatus status, SocialNetworkUtility.Caller call) throws UninitializedObjectException {
      SocialNetworkUtility.checkValid(this, "Link", "a new event");

      // If either argument is null, throws exception
      SocialNetworkUtility.checkNull(date, "Date");
      SocialNetworkUtility.checkNull(status, "Status");

      int size = events.size();

      // Here the call flag is used to determine the Link's current status using the even/odd trick.
      int targetIndicies;
      if (call.equals(SocialNetworkUtility.Caller.ESTABLISH))
         targetIndicies = 0;
      else
         targetIndicies = 1;

      // If the list is empty, we take appropriate action depending on the caller
      if (size == 0) {
         manipulateEmptyLink(date, status, call);
      }
      else { // Otherwise, we check the last element of the list to make sure the date come after that so it can be added
         Date lastEvent = events.get(size - 1);
         if (date.before(lastEvent))
            status.setStatus(SocialNetworkStatus.Status.INVALID_DATE);
         else if (size % 2 != targetIndicies)
            alreadySet(status, call);
         else { // If the link is in the wrong state, we take appropriate action
            events.add(date);
            status.setStatus(SocialNetworkStatus.Status.SUCCESS);
         }
      }
   }

   /** Establishes the link between two users on the given date
    * @param date  the date on which the link is to be established
    * @param status a status variable to notify the method calling this of the state after completion
    * @throws UninitializedObjectException  occurs when the method is called on an invalid link
    * @throws NullPointerException occurs if either of the arguments are null
    */
   public void establish(Date date, SocialNetworkStatus status) throws UninitializedObjectException{
      manipulateLink(date, status, SocialNetworkUtility.Caller.ESTABLISH);
   }

   /** Tears down the link between two users on the given date
    * @param date  the date on which the link is to be torn down
    * @param status a status variable to notify the method calling this of the state after completion
    * @throws UninitializedObjectException  occurs when the method is called on an invalid link
    * @throws NullPointerException occurs if either of the arguments are null
    */
   public void tearDown(Date date, SocialNetworkStatus status) throws UninitializedObjectException{
      manipulateLink(date, status, SocialNetworkUtility.Caller.TEARDOWN);
   }

   /** A method to determine if a date is between the current and next date
     * @param date   The date to be checked
     * @param current The current date we're looking at
     * @param next   The next event in the list
     * @return boolean  Tells if date lies between current and next
     */
   private boolean isCorrectIndex(Date date, Date current, Date next) {
      return (!date.before(current) && next != null && next.after(date));
   }

   /** A helper method that loops through the events and finds the date before the given one
     * @param date   the date we're looking for
     * @throws UninitializedObjectException  thrown if the link is invalid
     * @return Integer  the index of the event right before our date. -1 the date is after all events. -2 if the date preceeds all events
     */
   private Integer loopEvents(Date date) throws UninitializedObjectException {
      SocialNetworkUtility.checkValid(this, "Link", "checking events");

      // If the date comes before everything, there's no sense in checking it
      if (this.firstEvent() != null && date.before(this.firstEvent())) {
         return -2;
      }

      // Set up for iterator
      Iterator<Date> iter = events.iterator();
      Date current = null;
      Date next = iter.next();
      int index = 0;

       // Moves through pairs of dates in events, looking for 2 that surround the given date
      while (iter.hasNext()) {
         current = next;
         next = iter.next();

         // If we are in the proper spot for the date, check the index to see if active or inactive
         if (isCorrectIndex(date, current, next))
            return index;

         index++;
      }
      // The date must be after all events in the list
      return -1; 
   }

 /** Tells if the link was active on the given date
    * @param date  the date on which the link is to be inspected
    * @throws UninitializedObjectException  occurs when the method is called on an invalid link
    * @return boolean  tells if the link was active
    */
 public boolean isActive(Date date) throws UninitializedObjectException {

      Integer index = loopEvents(date);

      // Here we've found the proper spot for the date so we check its activity
      if (index >= 0)
         return index % 2 == 0;
      else if (index == -1) //In this case the event must be after all events
         return events.size() % 2 == 1;
      else
         return false;
   }

   /** Tells the first event recorded with the link
   * @throws UninitializedObjectException  occurs when the method is called on an invalid link
   * @return Date  the Date of the first interaction between the two users
   */
   public Date firstEvent() throws UninitializedObjectException{
      SocialNetworkUtility.checkValid(this, "Link", "First Event");

      if (events.size() == 0)
         return null;

      return events.get(0);
   }

   /** Tells the last event recorded with the link
   * @throws UninitializedObjectException  occurs when the method is called on an invalid link
   * @return Date  the Date of the last interaction between the two users
   */
   public Date lastEvent() throws UninitializedObjectException{
      SocialNetworkUtility.checkValid(this, "Link", "Last Event");

      if (events.size() == 0)
         return null;

      return events.get(events.size() - 1);
   }

   /** A little helper method to get the next date in the list of events
     * @param index  specifies the index before the date wanted
     * @return Date  the date of the event after index
     */
   private Date getNextEvent(int index) {
      if (index + 1 < events.size())
         return events.get(index + 1);
      else
         return null;
   }

   /** Gives the next event after the given date.
   * @param date  the date on which the inspection begins
   * @throws UninitializedObjectException  occurs when the method is called on an invalid link
   * @return Date  the date of the next action.  Null if there is none
   */
   public Date nextEvent(Date date) throws UninitializedObjectException {

      // Gets the index of the in the event list right before the date given
      Integer index = loopEvents(date);

      // If we found the proper spot for the date, return it
      if (index >= 0)
         return getNextEvent(index);
      else if (index == -2) // The date is before any given date.
         return this.firstEvent();
      else
         return null;
   }

   /** Helper method to check that when we ask for the previous event, we aren't given the current event
     * @param date  the user's date. We assure the event we return is not on this date
     * @param index the index at which we are check.
     * @throws UninitializedObjectException  occurs when the method is called on an invalid link
     * @return Date  the previousDate, adjusted so it's not the same as the date requested.
     */
   private Date getPreviousEvent(Date date, int index) throws UninitializedObjectException {
      int currentPlace = index;
      while (true) {
         if (currentPlace - 1 < 0)
            return this.firstEvent();
         if (events.get(currentPlace).equals(date))
            currentPlace -= 1;
         else
            return events.get(currentPlace);
      }
   }

   /** Gives the event right before the given date.
   * @param date  the date on which the inspection begins
   * @throws UninitializedObjectException  occurs when the method is called on an invalid link
   * @return Date  the last date of action.  Null if there is none
   */
   public Date previousEvent(Date date) throws UninitializedObjectException {

      Integer index = loopEvents(date);

      // If we've found the proper spot, confirm there's nothing before it
      if (index >= 0)
         return getPreviousEvent(date, index);
      else if (index == -2) // The date is before the link is established, so there's nothing before it
         return null;
      else // The given date is after all listed events, so we get the last event and verify we don't land on it
         return getPreviousEvent(date, events.size() - 1);
   }

   /** Gvies the link in a readable form
   * @return String  describes the link to a user. Contains "Invalid Link" if link is invalid.
   */
   public String toString() {
      if (this.isValid()) {
         StringBuilder linkDescription = new StringBuilder("The link between ");

         // Uses an iterator to get the first two users and put 'and' between them
         Iterator<User> iter = users.iterator();
         linkDescription.append(iter.next().getID());
         linkDescription.append(" and ");
         linkDescription.append(iter.next().getID());

         DateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");

         for (int i = 0; i < events.size(); i++) {
            Date event = (Date)(events.get(i).clone());

            // Corrections for the deprecated Date class
            event.setYear(event.getYear() - 1900);
            event.setMonth(event.getMonth() - 1);
            if (i % 2 == 0) { // Establish Action
               linkDescription.append("\nLink established on ");
               linkDescription.append(formatDate.format(event));
            }
            else { // Tear down action
               linkDescription.append("\nLink torn down on ");
               linkDescription.append(formatDate.format(event));
            }
         }
         return linkDescription.toString();
      }
      else {
         return "Invalid Link: Unitialized IDs";
      }
   }

   /** Compares two links by their users
   * @param o  an object to have its equality tested with this object.
   * @return boolean tells whether or not object is equal to the user.
   */
   @Override
   public boolean equals(Object o) {
      // Must be a link to be equal
      if (!(o instanceof Link))
         return false;
      // Cast the object to a link so we can user the appropriate methods
      Link testEqual = (Link)o;
      Set<User> testUsers;

      // Only valid links can be equal
      try {
         testUsers = testEqual.getUsers();
      }
      catch (UninitializedObjectException uoe) {
         return false;
      }

      // Only valid links can be equal
      try {
         return testUsers.equals(this.getUsers());
      }
      catch (UninitializedObjectException uoe) {
         return false;
      }
   }

  /** Generates a hash for the link based off the users
  * @return int the hash for the link
  */
  @Override
  public int hashCode() {
    try {
      return this.getUsers().hashCode();
    }
    catch (UninitializedObjectException uoe) {
      return 0;
    }
  }
}