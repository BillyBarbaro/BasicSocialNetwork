import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.HashMap;

/** Unit tests the Social Network.
  * @author Billy Barbaro
  */

public class SocialNetworkTester {

   /** Creates a simple Social Network for Testing
   * @return SocialNetwork  a SocialNetwork with 3 users, abc123, xyz456, lmn789
   */
   private SocialNetwork createNetwork() {

      SocialNetwork net = new SocialNetwork();
      User test1 = new User();
      User test2 = new User();
      User test3 = new User();

      test1.setID("abc123");
      test2.setID("xyz456");
      test3.setID("lmn789");

      net.addUser(test1);
      net.addUser(test2);
      net.addUser(test3);

      return net;
   }

   /** Tests the class constructor */
   public void testConstructor() {

      SocialNetwork net = new SocialNetwork();
      assertNotNull("Constructor does not create an object.", net);
   }

   /** Tests the addUser method */
   @Test
   public void testAddUser() {

      SocialNetwork net = new SocialNetwork();
      User test1 = new User();
      User test2 = new User();
      User test3 = new User();

      // We cannot add an invalid user to the network
      assertFalse("Invalid user cannot be added.", net.addUser(test1));

      test1.setID("abc123");
      assertTrue("User was not added.", net.addUser(test1));

      test2.setID("abc123");
      assertFalse("User cannot be added twice.", net.addUser(test2));

      test3.setID("xyz456");
      assertTrue("A second user was not able to be added.", net.addUser(test3));
   }

   /** Tests the isMember method */
   @Test
   public void testIsMember() {

      SocialNetwork net = createNetwork();
      assertTrue("Member not found in network.", net.isMember("abc123"));
      assertFalse("Nonexistent member found in network.", net.isMember("xxx777"));
   }

   /** Tests the getUser method */
   @Test
   public void testGetUser() {

      SocialNetwork net = createNetwork();

      assertEquals("User not gotten.", net.getUser("abc123").getID(), "abc123");
      assertNull("Broke when nonexistent user passed in.", net.getUser("xxx777"));
   }

   /** Tests the establishLink method */
   @Test
   public void testLinks() {

      SocialNetwork net = createNetwork();

      SocialNetworkStatus status = new SocialNetworkStatus();

      Set<String> users = new HashSet<String>();
      users.add("abc123");
      users.add("xyz456");

      // Creates a link between two users and makes sure we can't establish it twice without tearing it down
      net.establishLink(users, new Date(2000, 1, 1), status);
      assertEquals("Link was not established.", status.getStatus(), SocialNetworkStatus.Status.SUCCESS);

      net.establishLink(users, new Date(2000, 5, 1), status);
      assertEquals("Link cannot be established twice.", status.getStatus(), SocialNetworkStatus.Status.ALREADY_ACTIVE);

      // Makes sure tearing down a link happens appropriately
      net.tearDownLink(users, new Date(1999, 1, 2), status);
      assertEquals("Link cannot be set when date is invalid.", status.getStatus(), SocialNetworkStatus.Status.INVALID_DATE);

      net.tearDownLink(users, new Date(2000, 1, 2), status);
      assertEquals("Link was not successfully torn down.", status.getStatus(), SocialNetworkStatus.Status.SUCCESS);

      net.tearDownLink(users, new Date(2000, 1, 5), status);
      assertEquals("Inactive link cannot be torn down.", status.getStatus(), SocialNetworkStatus.Status.ALREADY_INACTIVE);

      net.establishLink(users, new Date(2000, 3, 3), status);
      assertEquals("Link was not re-established.", status.getStatus(), SocialNetworkStatus.Status.SUCCESS);

      assertFalse("isActive before date failed.", net.isActive(users, new Date(1999, 1, 1)));
      assertFalse("isActive falied on an invalid user.", net.isActive(users, new Date(2000, 1, 6)));
      assertTrue("isActive failed on an active user.", net.isActive(users, new Date(2000, 4, 8)));

      // Does not allow us to establish a link between three users
      users.add("lmn789");

      net.establishLink(users, new Date(), status);
      assertEquals("Link with invalid users can't be established.",status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // Cannot establish a link with only a single user
      Set<String> single = new HashSet<String>();
      single.add("abc123");

      net.establishLink(users, new Date(), status);
      assertEquals("Link with invalid users can't be established.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      net.tearDownLink(users, new Date(), status);
      assertEquals("Link with invalid users can't be torn down.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // A link including a user that does not exist cannot be active
      Set<String> noExist = new HashSet<String>();
      noExist.add("xyz456");
      noExist.add("lmn789");
      assertFalse("A link the does not exist shouldn't be active.", net.isActive(noExist, new Date()));
   }

   /** Tests null pointer exception in establishLink */
   @Rule
   public ExpectedException establishNull = ExpectedException.none();
   @Test
   public void testEstablishNull() throws UninitializedObjectException {
      SocialNetwork net = createNetwork();
      SocialNetworkStatus status = new SocialNetworkStatus();

      establishNull.expect(NullPointerException.class);
      establishNull.expectMessage("IDs may not be null.");
      net.establishLink(null, null, null);
   }

   /** Tests null pointer exception in tearDown */
   @Rule
   public ExpectedException tearDownNull = ExpectedException.none();
   @Test
   public void testTearDownNull() throws UninitializedObjectException {
      SocialNetwork net = createNetwork();
      SocialNetworkStatus status = new SocialNetworkStatus();

      tearDownNull.expect(NullPointerException.class);
      tearDownNull.expectMessage("IDs may not be null.");
      net.tearDownLink(null, null, null);
   }

   /** Tests the neighborhood methods.  
     * Code is pretty linear and not reused
     */
   @Rule
   public ExpectedException neighborhoodNull = ExpectedException.none();
   @Test public void testNeighborhood() throws UninitializedObjectException{
      SocialNetwork net = new SocialNetwork();
      SocialNetworkStatus status = new SocialNetworkStatus();

      // Create the Users for testing
      User tom = new User();
      tom.setID("Tom");
      net.addUser(tom);
      User jim = new User();
      jim.setID("Jim");
      net.addUser(jim);
      User tim = new User();
      tim.setID("Tim");
      net.addUser(tim);
      User jon = new User();
      jon.setID("Jon");
      net.addUser(jon);
      User bob = new User();
      bob.setID("Bob");
      net.addUser(bob);
      User edd = new User();
      edd.setID("Edd");
      net.addUser(edd);
      User joe = new User();
      joe.setID("Joe");
      net.addUser(joe);

      // Link together Users as friends
      HashSet<String> friend1 = new HashSet<String>();
      friend1.add("Tim");
      friend1.add("Jim");
      net.establishLink(friend1, new Date(), status);

      HashSet<String> friend2 = new HashSet<String>();
      friend2.add("Tom");
      friend2.add("Jon");
      net.establishLink(friend2, new Date(1990, 1, 1), status);
      net.tearDownLink(friend2, new Date(1990, 1, 3), status);
      net.establishLink(friend2, new Date(1990, 1, 4), status);

      HashSet<String> friend3 = new HashSet<String>();
      friend3.add("Jon");
      friend3.add("Bob");
      net.establishLink(friend3, new Date(1990, 1, 1), status);
      net.tearDownLink(friend3, new Date(1990, 1, 4), status);
      net.establishLink(friend3, new Date(1990, 1, 5), status);

      HashSet<String> friend4 = new HashSet<String>();
      friend4.add("Bob");
      friend4.add("Edd");
      net.establishLink(friend4, new Date(1990, 1, 2), status);
      net.tearDownLink(friend4, new Date(1990, 1, 8), status);
      net.establishLink(friend4, new Date(1990, 1, 9), status);

      HashSet<String> friend5 = new HashSet<String>();
      friend5.add("Bob");
      friend5.add("Joe");
      net.establishLink(friend5, new Date(1990, 1, 2), status);
      net.tearDownLink(friend5, new Date(1990, 1, 6), status);

      HashSet<String> friend6 = new HashSet<String>();
      friend6.add("Joe");
      friend6.add("Tom");
      net.establishLink(friend6, new Date(1990, 1, 3), status);
      net.tearDownLink(friend6, new Date(1990, 1, 7), status);
      net.establishLink(friend6, new Date(1990, 1, 9), status);

      HashSet<String> tear = new HashSet<String>();
      tear.add("Joe");
      tear.add("Bob");
      net.tearDownLink(tear, new Date(2000, 5, 1), status);

      HashSet<String> isolate = new HashSet<String>();
      isolate.add("Jim");
      isolate.add("Tim");
      net.tearDownLink(isolate, new Date(2000, 5, 5), status);

      // Create friend objects of the appropriate distances for each user
      Friend tim0 = new Friend();
      tim0.set(tim, 0);
      Friend tim1 = new Friend();
      tim1.set(tim, 1);

      Friend jim0 = new Friend();
      jim0.set(jim, 0);
      Friend jim1 = new Friend();
      jim1.set(jim, 1);

      Friend tom0 = new Friend();
      tom0.set(tom, 0);
      Friend tom1 = new Friend();
      tom1.set(tom, 1);
      Friend tom2 = new Friend();
      tom2.set(tom, 2);
      Friend tom3 = new Friend();
      tom3.set(tom, 3);

      Friend jon0 = new Friend();
      jon0.set(jon, 0);
      Friend jon1 = new Friend();
      jon1.set(jon, 1);
      Friend jon2 = new Friend();
      jon2.set(jon, 2);

      Friend bob0 = new Friend();
      bob0.set(bob, 0);
      Friend bob1 = new Friend();
      bob1.set(bob, 1);
      Friend bob2 = new Friend();
      bob2.set(bob, 2);
      Friend bob3 = new Friend();
      bob3.set(bob, 3);

      Friend edd0 = new Friend();
      edd0.set(edd, 0);
      Friend edd1 = new Friend();
      edd1.set(edd, 1);
      Friend edd2 = new Friend();
      edd2.set(edd, 2);
      Friend edd3 = new Friend();
      edd3.set(edd, 3);
      Friend edd4 = new Friend();
      edd4.set(edd, 4);

      Friend joe0 = new Friend();
      joe0.set(joe, 0);
      Friend joe1 = new Friend();
      joe1.set(joe, 1);
      Friend joe2 = new Friend();
      joe2.set(joe, 2);
      Friend joe3 = new Friend();
      joe3.set(joe, 3);
      Friend joe4 = new Friend();
      joe4.set(joe, 4);

      // Create the set of friends each user should return for the first given date
      HashSet<Friend> tomFriends = new HashSet<Friend>();
      tomFriends.add(tom0);
      tomFriends.add(jon1);
      tomFriends.add(bob2);
      tomFriends.add(edd3);
      tomFriends.add(joe1);

      HashSet<Friend> jimFriends = new HashSet<Friend>();
      jimFriends.add(jim0);
      jimFriends.add(tim1);

      HashSet<Friend> timFriends = new HashSet<Friend>();
      timFriends.add(tim0);
      timFriends.add(jim1);

      HashSet<Friend> jonFriends = new HashSet<Friend>();
      jonFriends.add(jon0);
      jonFriends.add(tom1);
      jonFriends.add(bob1);
      jonFriends.add(edd2);
      jonFriends.add(joe2);

      HashSet<Friend> bobFriends = new HashSet<Friend>();
      bobFriends.add(bob0);
      bobFriends.add(tom2);
      bobFriends.add(jon1);
      bobFriends.add(edd1);
      bobFriends.add(joe1);

      HashSet<Friend> eddFriends = new HashSet<Friend>();
      eddFriends.add(edd0);
      eddFriends.add(bob1);
      eddFriends.add(jon2);
      eddFriends.add(tom3);
      eddFriends.add(joe2);

      HashSet<Friend> joeFriends = new HashSet<Friend>();
      joeFriends.add(joe0);
      joeFriends.add(bob1);
      joeFriends.add(jon2);
      joeFriends.add(tom1);
      joeFriends.add(edd2);

      HashSet<Friend> tomFriends1 = new HashSet<Friend>();
      tomFriends1.add(tom0);
      tomFriends1.add(jon1);
      tomFriends1.add(joe1);

      HashSet<Friend> tomFriends2 = new HashSet<Friend>();
      tomFriends2.add(tom0);
      tomFriends2.add(jon1);
      tomFriends2.add(joe1);
      tomFriends2.add(bob2);

      // Testing at the date when all links are active
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Tom", new Date(2000, 1, 2), status), tomFriends);
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Jim", new Date(2000, 1, 2), status), jimFriends);
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Tim", new Date(2000, 1, 2), status), timFriends);
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Jon", new Date(2000, 1, 2), status), jonFriends);
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Bob", new Date(2000, 1, 2), status), bobFriends);
      assertEquals("Error in neighborhood with no max distance", net.neighborhood("Edd", new Date(2000, 1, 2), status), eddFriends);

      assertEquals("Error in neighborhood with max distance of 1", net.neighborhood("Tom", new Date(2000, 1, 2), 1, status), tomFriends1);
      assertEquals("Error in neighborhood with max distance or 2", net.neighborhood("Tom", new Date(2000, 1, 2), 2, status), tomFriends2);
      assertEquals("Error in neighborhood with max distance of 7", net.neighborhood("Tom", new Date(2000, 1, 2), 7, status), tomFriends);

      // New groups of friends for after the link has been torn down
      jimFriends = new HashSet<Friend>();
      jimFriends.add(jim0);

      timFriends = new HashSet<Friend>();
      timFriends.add(tim0);

      jonFriends = new HashSet<Friend>();
      jonFriends.add(jon0);
      jonFriends.add(tom1);
      jonFriends.add(bob1);
      jonFriends.add(edd2);
      jonFriends.add(joe2);

      bobFriends = new HashSet<Friend>();
      bobFriends.add(bob0);
      bobFriends.add(tom2);
      bobFriends.add(jon1);
      bobFriends.add(edd1);
      bobFriends.add(joe3);

      eddFriends = new HashSet<Friend>();
      eddFriends.add(edd0);
      eddFriends.add(bob1);
      eddFriends.add(jon2);
      eddFriends.add(tom3);
      eddFriends.add(joe4);

      joeFriends = new HashSet<Friend>();
      joeFriends.add(joe0);
      joeFriends.add(bob3);
      joeFriends.add(jon2);
      joeFriends.add(tom1);
      joeFriends.add(edd4);

      // Testing to see if tearing down the link affects our results
      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Tom", new Date(2000, 6, 2), status), tomFriends);

      // Makes sure the method works even when a user has no friends :(
      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Jim", new Date(2000, 6, 2), status), jimFriends);
      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Tim", new Date(2000, 6, 2), status), timFriends);

      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Jon", new Date(2000, 6, 2), status), jonFriends);
      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Bob", new Date(2000, 6, 2), status), bobFriends);
      assertEquals("Error in neighborhood with inactive links", net.neighborhood("Edd", new Date(2000, 6, 2), status), eddFriends);

      // Testing for an invalid user
      assertNull("Invalid user should return null", net.neighborhood("Lenny", new Date(2000, 6, 2), status));
      assertEquals("Invalid users should change status", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // Testing for invalid distance
      assertNull("Invalid distance should return null", net.neighborhood("Tom", new Date(2000, 6, 2), -14, status));
      assertEquals("Invalid distance should change status", status.getStatus(), SocialNetworkStatus.Status.INVALID_DISTANCE);

      HashMap<Date, Integer> jonTrends = new HashMap<Date, Integer>();
      jonTrends.put(new Date(1990, 1, 1), 3);
      jonTrends.put(new Date(1990, 1, 2), 5);
      jonTrends.put(new Date(1990, 1, 7), 4);
      jonTrends.put(new Date(1990, 1, 8), 3);
      jonTrends.put(new Date(1990, 1, 9), 5);

      HashMap<Date, Integer> tomTrends = new HashMap<Date, Integer>();
      tomTrends.put(new Date(1990, 1, 1), 3);
      tomTrends.put(new Date(1990, 1, 2), 5);
      tomTrends.put(new Date(1990, 1, 7), 4);
      tomTrends.put(new Date(1990, 1, 8), 3);
      tomTrends.put(new Date(1990, 1, 9), 5);

      HashMap<Date, Integer> joeTrends = new HashMap<Date, Integer>();
      joeTrends.put(new Date(1990, 1, 2), 5);
      joeTrends.put(new Date(1990, 1, 7), 1);
      joeTrends.put(new Date(1990, 1, 9), 5);

      HashMap<Date, Integer> bobTrends = new HashMap<Date, Integer>();
      bobTrends.put(new Date(1990, 1, 1), 3);
      bobTrends.put(new Date(1990, 1, 2), 5);
      bobTrends.put(new Date(1990, 1, 7), 4);
      bobTrends.put(new Date(1990, 1, 8), 3);
      bobTrends.put(new Date(1990, 1, 9), 5);

      HashMap<Date, Integer> eddTrends = new HashMap<Date, Integer>();
      eddTrends.put(new Date(1990, 1, 2), 5);
      eddTrends.put(new Date(1990, 1, 7), 4);
      eddTrends.put(new Date(1990, 1, 8), 1);
      eddTrends.put(new Date(1990, 1, 9), 5);

      
      assertEquals("Trends incorrect for Jon.", jonTrends, net.neighborhoodTrend("Jon", status));
      assertEquals("Trends incorrect for Tom.", tomTrends, net.neighborhoodTrend("Tom", status));
      assertEquals("Trends incorrect for Bob.", bobTrends, net.neighborhoodTrend("Bob", status));
      assertEquals("Trends incorrect for Joe.", joeTrends, net.neighborhoodTrend("Joe", status));
      assertEquals("Trends incorrect for Edd.", eddTrends, net.neighborhoodTrend("Edd", status));
      
      neighborhoodNull.expect(NullPointerException.class);
      neighborhoodNull.expectMessage("Date may not be null.");
      net.neighborhood("Tom", null, status);

   }
}