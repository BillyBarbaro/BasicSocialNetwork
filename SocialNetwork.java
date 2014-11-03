import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Map;
import java.util.Collections;

/** Class representing social network with users and links between pairs of users
  * @author Billy Barbaro
  */

public class SocialNetwork {

    /** A hash map mapping IDs to user objects */
    private HashMap<String, User> users;

    /** A hash map mapping IDs to a hash map of IDs linked to the first ID and the link between the two IDs */
    private HashMap<String, HashMap<String, Link>> links; 
 
    /** Creates a social network with no users */
    public SocialNetwork() {
        super();
        users = new HashMap<String, User>();
        links = new HashMap<String, HashMap<String, Link>>();
    }

    /** Adds a user to the social network
    * @param user  the user to be added to the network
    * @return boolean  tells if the user was successfully added
    */
    public boolean addUser(User user) {
        if (!user.isValid()) // User can't be added unless its valid
            return false;
        if (users.get(user.getID()) != null) // User cannot already exist in the network
            return false;
        if (users.put(user.getID(), user) != null) // Adds User to list of Users
            return false;

        // Adds the user into the Links HashMap to be tracked.
        return links.put(user.getID(), new HashMap<String, Link>()) == null;
    }

    /** Checks if a User is a member of the social network
    * @param id  the id of the user to be checked on
    * @return boolean  tells if the id matches the of a user in the network
    */
    public boolean isMember(String id) {
        return users.containsKey(id);
    }

    /** Returns the user from the network matching the id
    * @param id  the id of the user to be queried
    * @return User  the user matching the id.  Null if not present
    */
    public User getUser(String id) {
        return users.get(id);
    }

    /** Gets the two users to be linked by their ids. Adds them to the toAdd set.
      * @param ids  A set of 2 ids for the users to be linked
      * @param toAdd A set of users that will be added
      * @return boolean     Tells if the users can be linked
      */
    private boolean usersFromID(Set<String> ids, Set<User> toAdd) {
        // A link is exclusive between 2 users
        if (ids.size() != 2) {
            return false;
        }

        // Makes sure the user's we're adding are in the network already and cretes a set from them
        for (String id : ids) {
            User toBeAdded = users.get(id);
            if (toBeAdded == null) {
                return false;
            }
            toAdd.add(toBeAdded);
        }
        return true;
    }

   /** Helper method to get the link between two users given their IDs
     * @param ids  A set of two User ids we're getting the link for
     * @return Link    the link between the two ids entered. Null if it doesn't exit
     */
   private Link getLink(Set<String> ids) {

      // Lets us iterate through the input ids
      Iterator<String> iter = ids.iterator();
      HashMap<String, Link> selected = links.get(iter.next());

      // This is the case where the link between the two users doesn't exit
      if (selected == null)
         return null;

      // Because of the way the data is structured, we passe the second id into the returned hashmap to get the link.
      return selected.get(iter.next());
    }

    /** Helper method to abstract away the try/catch block when changing a link
      * @param link     the link to be cgabged
      * @param date     the date on which to change the link
      * @param status   tracks the exit status of the method
      * @param call   specifies the caller of the helper method
      */
    private void actionHelper(Link link, Date date, SocialNetworkStatus status, SocialNetworkUtility.Caller call) {
        try {
            if (call.equals(SocialNetworkUtility.Caller.ESTABLISH))
                link.establish(date, status);
            else
                link.tearDown(date, status);
        }
        catch (UninitializedObjectException uoe) { // The link is assure valid whenever this method is called
            assert false;
        }
    }

    /** Helper method to establish/teardown a given link
      * @param ids  a set of the two ids identifying the Link to manipulate
      * @param date     the date of the action
      * @param status   tells the caller the exit status of their operation
      * @param call   specifies the caller of the helper method
      * @return Set<User>    If no errors, but unsuccessful, the set of users corresponding to the ids.  Otherwise, null
      */
    private Set<User> linkHelper(Set<String> ids, Date date, SocialNetworkStatus status, SocialNetworkUtility.Caller call) {

        // Verifies none of the arguments are null
        SocialNetworkUtility.checkNull(ids, "IDs");
        SocialNetworkUtility.checkNull(date, "Date");
        SocialNetworkUtility.checkNull(status, "Status");

        Set<User> toAdd = new HashSet<User>();

        // Tries to create a set of two users to be linked
        if (!usersFromID(ids, toAdd)) {
            status.setStatus(SocialNetworkStatus.Status.INVALID_USERS);
            return null;
        }
        else {
            // Gets the link from the collection of links and manipulate it
            Link fromNetwork = getLink(ids);
            if (fromNetwork != null) { // In the case that the link already exists
               actionHelper(fromNetwork, date, status, call);
               return null;
            }
            else { // If unsuccessful, we return the users created from the ids for the callers to manipulate
                return toAdd;
            }
        }
    }

    /** Helper method taking the ids of the users linked together and adding the link to the network
      * @param ids     the ids of the users linked
      * @param toAdd   the link to be added to the network
      */
    private void addLinkToNetwork(Set<String> ids, Link toAdd) {

        // Grab both users
        Iterator<String> iter = ids.iterator();
        String id1 = iter.next();
        String id2 = iter.next();

        // Saves the link under the id of the second user in the first user's hashmap
        HashMap<String, Link> entry1 = links.get(id1);
        entry1.put(id2, toAdd);

        // Saves the link under the id of the first user in the second user's hashmap
        HashMap<String, Link> entry2 = links.get(id2);
        entry2.put(id1, toAdd);
    }

    /** Establishes a link between two users in the social network
    * @param ids  a Set<Strings> of two Users' Ids to have a link established between
    * @param date  the date to establish the link
    * @param status    a status variable to notify the method calling this of the state after completion
    * @throws NullPointerExcpetion  occurs if any of the parameters are null
    */
    public void establishLink(Set<String> ids, Date date, SocialNetworkStatus status) {

        Set<User> createNew = linkHelper(ids, date, status, SocialNetworkUtility.Caller.ESTABLISH);

        // If the link didn't exist, we create, establish it, and add it to the network
        if (createNew != null) {
            Link newLink = new Link();
            newLink.setUsers(createNew, status);
            actionHelper(newLink, date, status, SocialNetworkUtility.Caller.ESTABLISH);
            addLinkToNetwork(ids, newLink);
        }
    }

    /** Tears down a link between two users in the social network
    * @param ids  a Set<Strings> of two Users' Ids to have a link tron down
    * @param date  the date to tear down the link
    * @param status    a status variable to notify the method calling this of the state after completion
    * @throws NullPointerExcpetion  occurs if any of the parameters are null
    */
    public void tearDownLink(Set<String> ids, Date date, SocialNetworkStatus status) {
        linkHelper(ids, date, status, SocialNetworkUtility.Caller.TEARDOWN);
    }

    /** Checks if a link between two users in the social network is active on a given date
    * @param ids  a Set<Strings> of two Users' Ids to have a link checked
    * @param date  the date to check the link
    * @return boolean  tells is the link was active at the given date
    */
    public boolean isActive(Set<String> ids, Date date) {
        Set<User> toTest = new HashSet<User>();

        // Tries to create a set of two users to be linked
        if (!usersFromID(ids, toTest))
            return false;

        // Gets the link from the collection of links and checks if it is active
        Link active = getLink(ids);
        if (active != null) {
            try {
                return active.isActive(date);
            }
            catch(UninitializedObjectException uoe) { // At this point in code the link is assured to be valid
                assert false;
            }
        }
        return false;
    }

    /** Takes the User associated with friendID and tries to add them to the set of friends
      * @param friends the set of friends for the current search
      * @param friendID    the ID of the User we're looking to add to the set of friends
      * @param nextLevel   the queue to place the user's linked users in
      * @param link    the link between the friend and another user
      * @param date    the date to search the links at
      * @param depth   the current level of depth we're searching
      * @throws UninitializedObjectException   the link is invalid
      */
    private void addFriendToSetHelper(Set<Friend> friends, String friendID, Queue<String> nextLevel, Link link, Date date, int depth) throws UninitializedObjectException {
        
        //  Makes sure the user we're adding isn't the one we started with and the link between the two users is active at the given date
        if (link.isActive(date)) {
            Friend newFriend = new Friend();            
            newFriend.set(this.getUser(friendID), depth);
            
            // Try and add the friend to the set. If an equal one is already in it, the if statement is false
            if (friends.add(newFriend))
                nextLevel.add(friendID);
        }
    }

    /** Helper method to abstract away the try/catch block. This will never be called becasue we're assured the helper method will never have an invalid object.
      * @param friends the set of friends for the current search
      * @param friendID    the ID of the User we're looking to add to the set of friends
      * @param nextLevel   the queue to place the user's linked users in
      * @param link    the link between the friend and another user
      * @param date    the date to search the links at
      * @param depth   the current level of depth we're searching
      */
    private void addFriendToSet(Set<Friend> friends, String friendID, Queue<String> nextLevel, Link link, Date date, int depth) {
        try {
            addFriendToSetHelper(friends, friendID, nextLevel, link, date, depth);
        }
        catch (UninitializedObjectException uoe) {
            assert false;
        }
    }

    /** Adds a user to the set of friends and adds the users they're linked to to the queue
      * @param id  the id of the user to perform the method on
      * @param nextLevel   the queue to place the user's linked users in
      * @param friends the set of friends for the current search
      * @param date    the date to search the links at
      * @param depth   the current level of depth we're searching
      */
    private void checkFriends(String id, Queue<String> nextLevel, Set<Friend> friends, Date date, int depth) {

        // Get the User's linked the the user with id
        HashMap<String, Link> directFriends = links.get(id);

        // Loop through each friend in the and tries to add them to the set
        for (Entry<String, Link> entry : directFriends.entrySet()) {
            String friendID = entry.getKey();
            Link link = entry.getValue();
            addFriendToSet(friends, friendID, nextLevel, link, date, depth);
        }
    }

    /** Loop over everything currently in the queue for this depth and them to freinds if its not already there.
      * @param friends the set of friends for the current search
      * @param date    the date to search the links at
      * @param currentLevel the current queue of users at this depth
      * @param depth   the current level of depth we're searching
      * @return linkedList<String> the next level of user's to be serached.
      */
    private Queue<String> addFriends(Set<Friend> friends, Date date, Queue<String> currentLevel, int depth) {
        Queue<String> nextLevel = new LinkedList<String>();

        // Loop through each user in the level and create a friend from them and add their friends to the queue
        for (String id : currentLevel)
            checkFriends(id, nextLevel, friends, date, depth);

        return nextLevel;
    }

    /** Adds a user to their own friend network with distance of 0
      * @param id  the ID of the user to be added as a friend
      * @param friends the set of friends to add the user to
      */
    private void addSelfAsFriend(String id, Set<Friend> friends) {
        Friend own = new Friend();
        User self = this.getUser(id);
        own.set(self, 0);
        friends.add(own);
    }

    /** Helper method for checking a User's neighborhood
      * @param id   the user to find friends of
      * @param date    the date for which to check the links
      * @param distance    the farthest number of links a friend included in this set is away from the user
      * @param status   the exit status of the method
      * @return Set<Friend>    a set of Friends of the user within distance_max
      */
    private Set<Friend> neighborhoodHelper(String id, Date date, int distance, SocialNetworkStatus status) {

      if (!checkNeighborhoodParams(id, date, distance, status))
         return null;

        HashSet<Friend> friends = new HashSet<Friend>();

        // Adds the starting user as a friend at distance 0
        addSelfAsFriend(id, friends);

        // A linked List treated as a queue to hold users to be serached in our breadth first graph search
        Queue<String> toFindFriends = new LinkedList<String>();
        toFindFriends.add(id);

        int currentDepth = 1; // The current number of links from the original user

        // This loop checks that we have nodes left to search and that we have not exceeded the distance. (Note that if there is no max distance, distance is set to a negative number)
        while (toFindFriends.size() > 0 && currentDepth <= distance) {
            toFindFriends = addFriends(friends, date, toFindFriends, currentDepth);
            currentDepth++;
        }

        return friends;
    }

    /** Verifies that the parameters passed to the neighborhood method are valid
      * @param id   the user's ID. Must be a member of the network
      * @param date    must not be null
      * @param distance     must be positive or 0
      * @param status   saves the exit status of the operation
      * @return boolean    true if the params are all valid. False if not.
      */
    private boolean checkNeighborhoodParams(String id, Date date, int distance, SocialNetworkStatus status) {
        SocialNetworkUtility.checkNull(id, "ID");
        SocialNetworkUtility.checkNull(date, "Date");
        SocialNetworkUtility.checkNull(status, "Status");

        if (!this.isMember(id)) { // Make sure the user is in the network
            status.setStatus(SocialNetworkStatus.Status.INVALID_USERS);
            return false;
        }
        if (distance < 0) { // Make sure the distance is a valid number
            status.setStatus(SocialNetworkStatus.Status.INVALID_DISTANCE);
            return false;
        }
        return true;
    }

    /** Finds all the users to which the user with the given ID is directly and indirectly linked
      * @param id   the user to find friends of
      * @param date    the date for which to check the links
      * @param status   the exit status of the method
      * @return Set<Friend>    a set of Friends of the user
      */
    public Set<Friend> neighborhood(String id, Date date, SocialNetworkStatus status) {
      return neighborhoodHelper(id, date, Integer.MAX_VALUE, status);
    }

    /** Finds all the users to which the user with the given ID which are less than or equal to the maximum distance links away specified
      * @param id   the user to find friends of
      * @param date    the date for which to check the links
      * @param distance_max    the farthest number of links a friend included in this set is away from the user
      * @param status   the exit status of the method
      * @return Set<Friend>    a set of Friends of the user within distance_max
      */
   public Set<Friend> neighborhood(String id, Date date, int distance_max, SocialNetworkStatus status) {
     return neighborhoodHelper(id, date, distance_max, status);
   }

    /** Helper method. Given a link with the event insde our desired interval, checks its activity, and truncates the interal to its active period
     * @param friendID the id of the friend we're linked to
     * @param link  the Link betwwen the origianl user and friend
     * @param currentEvent   the current date of activity we're observing
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     * @return boolean true if we can find an event
     */
   private boolean eventBetween(Link link, String friendID, Date currentEvent, HashSet<String> alreadyChecked, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      
      // We've hit a potential site of change for the link so we save it
      linkChange.add(currentEvent);
      if (link.isActive(currentEvent)) { // If we're active at the time of the link, configure the algoithm to continue to probe deeper.
         Date finish = new Date();
         Date nextEvent = link.nextEvent(currentEvent);
         if (nextEvent == null || nextEvent.after(intervalEnd)) { // If the end of the activity is out of bounds, shorten it and do not move on
            checkLinksInInterval(friendID, alreadyChecked, currentEvent, intervalEnd, linkChange, status);
            return true;
         }
         else { // Continue probing within the interval
            checkLinksInInterval(friendID, alreadyChecked, currentEvent, nextEvent, linkChange, status);
            return false;
         }
      }
      return false;
   }

   /** Helper method. Given a link with the event outside our desired interval, truncates the interal to its active period
     * @param friendID the id of the friend we're linked to
     * @param link  the Link betwwen the origianl user and friend
     * @param currentEvent   the current date of activity we're observing
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     * @return boolean true if we can find an event
     */
   private void eventOutsideInterval(Link link, String friendID, Date currentEvent, Date intervalStart, Date intervalEnd, HashSet<String> alreadyChecked, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException{
      Date lastEvent = link.previousEvent(currentEvent);
      // Checks that we're not the first event for the link
      if (lastEvent != null) {
         if (!currentEvent.after(intervalEnd) && lastEvent.before(intervalStart)) { // If an event occured to make us active in the interval, probe it
            checkLinksInInterval(friendID, alreadyChecked, intervalStart, currentEvent, linkChange, status);
         }
         else { // If the link became active before the interval began, probe it
            if (!lastEvent.before(intervalStart))
               checkLinksInInterval(friendID, alreadyChecked, lastEvent, intervalEnd, linkChange, status);
         }
      }
   }

   /** Helper method. Given an event a link, we check if it's inside or outside our interval and handle it appropriatly
     * @param friendID the id of the friend we're linked to
     * @param link  the Link betwwen the origianl user and friend
     * @param currentEvent   the current date of activity we're observing
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     * @return boolean true if we can find an event
     */
   private boolean inspectEvent(String friendID, Link link, Date currentEvent, HashSet<String> alreadyChecked, Date intervalStart, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      // The current event falls within our interval
      if (!currentEvent.before(intervalStart) && currentEvent.before(intervalEnd)) {
         if (eventBetween(link, friendID, currentEvent, alreadyChecked, intervalEnd, linkChange, status)) {
             return true;
         }
      }
      else { // The current event falls outside our interval
         eventOutsideInterval(link, friendID, currentEvent, intervalStart, intervalEnd, alreadyChecked, linkChange, status);
         return true;
      }
      return false;
   }

   /** Helper method. Probes the link for activity between the specified intervals
     * @param friendID the id of the friend we're linked to
     * @param link  the Link betwwen the origianl user and friend
     * @param currentEvent   the current date of activity we're observing
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     * @return boolean true if there are no events after the currentEvent
     */
   private boolean noMoreEvents(String friendID, Link link, Date currentEvent, HashSet<String> alreadyChecked, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      // We first check it this is the last event
      if (currentEvent == null) {
         if (link.isActive(link.lastEvent())) { // If the link ended active, we probe from that last event to end of the interval
            checkLinksInInterval(friendID, alreadyChecked, link.lastEvent(), intervalEnd, linkChange, status);
         }
         return true;
      }
      return false;
   }

   /** Helper method. Probes the link for activity between the specified intervals
     * @param originalID the id of the User being explored from
     * @param entry an entry from the hashMap of events which contains the information on the link we are to explore
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     */
   private void getLinkActivity(String friendID, Link link, HashSet<String> alreadyChecked, Date intervalStart, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      Date currentEvent = link.nextEvent(intervalStart);
      EventLoop: while (true) {
         // If there are no events left in the link, we check it's tail and break out
         if(noMoreEvents(friendID, link, currentEvent, alreadyChecked, intervalEnd, linkChange, status)) {
             break EventLoop;
         }
         else { // Here we have found an event for the link and we handle it
            if (inspectEvent(friendID, link, currentEvent, alreadyChecked, intervalStart, intervalEnd, linkChange, status)) {
               break EventLoop;
            }
            // Gets the next event to be tested in the next loop
            currentEvent = link.nextEvent(currentEvent);
         }
      }
   }

   /** Helper method. Checks if a given link has been explored. If it hasn't, explores it
     * @param originalID the id of the User being explored from
     * @param entry an entry from the hashMap of events which contains the information on the link we are to explore
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     */
   private void linkBeenExplored(String originalID, Entry<String, Link> entry, HashSet<String> alreadyChecked, Date intervalStart, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      
      // Extracts data from the entry
      String friendID = entry.getKey();
      Link link = entry.getValue();

      // If this user has not already been explored we go on to probe the link
      if (!alreadyChecked.contains(friendID)) {
         HashSet<String> newCheck = (HashSet<String>)alreadyChecked.clone();
         newCheck.add(originalID);

         getLinkActivity(friendID, link, newCheck, intervalStart, intervalEnd, linkChange, status);       
      }
   }

   /** Helper method. Called recursively to explore critical dates for a user in the network
     * @param id the id of the User to have their links probed
     * @param alreadyChecked a set of User's that have already been visited during a given sequence. Prevents infinite loops
     * @param intervalStart  the opening date to look for activity
     * @param intervalEnd the close of the window in which we look for activity
     * @param linkChange  a set keeping track of dates on which the links change
     * @param status   the exit status of the method
     * @throws UninitializedObjectExcpetion  should never occur due to construction of the method
     */
   private void checkLinksInInterval(String id, HashSet<String> alreadyChecked, Date intervalStart, Date intervalEnd, HashSet<Date> linkChange, SocialNetworkStatus status) throws UninitializedObjectException {
      HashMap<String, Link> currentNode = links.get(id);
      
      // Iterates through each of a user's links and attempts to probe them
      for (Entry<String, Link> entry : currentNode.entrySet()) {
         linkBeenExplored(id, entry, alreadyChecked, intervalStart, intervalEnd, linkChange, status);
      }
   }

   /** Takes a set of dates of potential change and determines the actual ones using the neighborhood method
     * @param id the User whose neighborhood we're looking at
     * @param potentialChanges  the set of Dates in question
     * @retrn Map<Date, Integer>   the map of trends
     */
   private Map<Date, Integer> potentialChangesToTrends(String id, Set<Date> potentialChanges) {
      // Sets up the return value
      Map<Date, Integer> trends = new HashMap<Date, Integer>();

      // Makes our set of dates orderable and sorts them in chronological order
      ArrayList<Date> dateChange = new ArrayList<Date>(potentialChanges);
      Collections.sort(dateChange);

      int currentSize = -1; // Iterates across the sorted list of dates looking for points at which the size changes so the date can be added to the map
      for (Date d : dateChange) {
         int sizeAtd = neighborhood(id, d, new SocialNetworkStatus()).size();
         if (sizeAtd != currentSize) {
            trends.put(d, sizeAtd);
            currentSize = sizeAtd;
         }
      }
      return trends;
   }

   /** Returns a map with the dates at which the size of a user's neighborhood changed and the size at those dates
     * @param id  the ID of the user whose neighborhood we wanna look at
     * @param status   the exit status of the method
     * @return HashMap<Date, Integer> the map of dates to neighborhood sizes
     */
   public Map<Date, Integer> neighborhoodTrend(String id, SocialNetworkStatus status) {

      HashSet<Date> linkChange = new HashSet<Date>();
      try { // Calls a helper method to discover all dates on which potential changes could've occurred in the network
         checkLinksInInterval(id, new HashSet<String>(), new Date(0), new Date(Long.MAX_VALUE), linkChange, status);
      }
      catch (UninitializedObjectException uoe) {
         assert false;
      }

      // Once we have our map of times of potential change, calculate the size at these times to find the trends.
      Map<Date, Integer> trends = potentialChangesToTrends(id, linkChange);
      return trends;
   }

}