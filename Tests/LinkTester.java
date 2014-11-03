import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/** Unit tests the Link object in the Social Network.
  * @author Billy Barbaro
  */

public class LinkTester {

   /** Creates a basic Link between two users
   * @return Link  a Link between two users abc123 and xyz456
   */
   private Link createTestLink() {

      Link test = new Link();

      SocialNetworkStatus status = new SocialNetworkStatus();

      User test1 = new User();
      User test2 = new User();

      Set<User> testGrp1 = new HashSet<User>();
      test1.setID("abc123");
      test2.setID("xyz456");
      testGrp1.add(test1);
      testGrp1.add(test2);
      test.setUsers(testGrp1, status);

      return test;
   }

   /** Creates a Link between two users with the date it was established
   * @return Link  a Link between two users abc123 and xyz456 established on 1/1/2000
   */
   private Link createEstablishedLink() throws UninitializedObjectException {

      Link test = createTestLink();
      SocialNetworkStatus status = new SocialNetworkStatus();

      test.establish(new Date(2000, 1, 1), status);
      return test;
   }

   /** Creates a Link between two users which has been established and torn down several times
   * @return Link  a Link between two users abc123 and xyz456 with a history of being established and torn down
   */
   private Link createHistoriedLink() throws UninitializedObjectException {

      Link test = createEstablishedLink();
      SocialNetworkStatus status = new SocialNetworkStatus();

      test.tearDown(new Date(2000, 2, 2), status);// Here we establish and tear down a link on the same day to account for the edge case
      test.establish(new Date(2000, 2, 2), status);
      test.tearDown(new Date(2000, 2, 14), status);
      test.establish(new Date(2000, 3, 3), status);
      test.tearDown(new Date(2000, 3, 4), status);
      return test;
   }

   /** Tests the class constructor */
   @Test
   public void testConstructor() {
      Link test = new Link();
      assertTrue(test != null);
   }

   /** Tests the setUser method */
   @Rule
   public ExpectedException setUserNull = ExpectedException.none();
   @Test
   public void testSetUser() {

      Link link1 = new Link();
      SocialNetworkStatus status = new SocialNetworkStatus();

      User test1 = new User();
      User test2 = new User();
      User test3 = new User();
      User test4 = new User();

      // Two invalid users
      Set<User> testGrp1 = new HashSet<User>();
      testGrp1.add(test1);
      testGrp1.add(test2);
      link1.setUsers(testGrp1, status);
      assertEquals("Users should invalid.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // One invalid user
      Set<User> testGrp2 = new HashSet<User>();
      test1.setID("abc123");
      testGrp2.add(test1);
      testGrp2.add(test2);
      link1.setUsers(testGrp2, status);
      assertEquals("Users should invalid.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // One invalid user
      Set<User> testGrp3 = new HashSet<User>();
      test1.setID("abc123");
      test2.setID("abc123");
      testGrp3.add(test1);
      testGrp3.add(test2);
      link1.setUsers(testGrp3, status);
      assertEquals("Users should invalid.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      // Too many users
      Set<User> testGrp4 = new HashSet<User>();
      testGrp4.add(test1);
      testGrp4.add(test3);
      testGrp4.add(test4);
      test3.setID("xyz456");
      test4.setID("lmn987");
      link1.setUsers(testGrp4, status);
      assertEquals("Users should invalid.", status.getStatus(), SocialNetworkStatus.Status.INVALID_USERS);

      Set<User> testGrp5 = new HashSet<User>();
      testGrp5.add(test1);
      testGrp5.add(test3);
      SocialNetworkStatus me = new SocialNetworkStatus();
      link1.setUsers(testGrp5, me);
      assertEquals("Set users failed.", me.getStatus(), SocialNetworkStatus.Status.SUCCESS);

      // Can't add a second set of users to the same link
      Set<User> testGrp6 = new HashSet<User>();
      testGrp6.add(test1);
      testGrp6.add(test4);
      link1.setUsers(testGrp6, status);
      assertEquals("Can't set new users to a link.", status.getStatus(), SocialNetworkStatus.Status.ALREADY_VALID);

      // This should throw an exception
      setUserNull.expect(NullPointerException.class);
      setUserNull.expectMessage("Users may not be null.");
      link1.setUsers(null, null);
   }

   /** Tests the isValid method */
   @Test
   public void testIsValid() {

      Link link1 = new Link();

      SocialNetworkStatus status = new SocialNetworkStatus();

      User test1 = new User();
      User test2 = new User();

      assertFalse("Initialized link should start invalid.", link1.isValid());

      // If the users aren't actually added, the link should still be invalid
      Set<User> testGrp1 = new HashSet<User>();
      testGrp1.add(test1);
      testGrp1.add(test2);
      link1.setUsers(testGrp1, status);
      assertFalse("Link should be invalid", link1.isValid());

      test1.setID("abc123");
      test2.setID("xyz456");
      link1.setUsers(testGrp1, status);
      assertTrue("Link should be valid.", link1.isValid());
   }

   /** Tests the getUsers method */
   @Rule
   public ExpectedException getUsersUninitialized = ExpectedException.none();
   @Test
   public void testGetUsers() throws UninitializedObjectException {

      Link link1 = createTestLink();
      Link link2 = new Link();

      User test1 = new User();
      User test2 = new User();

      Set<User> testGrp1 = new HashSet<User>();
      test1.setID("abc123");
      test2.setID("xyz456");
      testGrp1.add(test2);
      testGrp1.add(test1);

      assertEquals("Users gotten incorrectly.", link1.getUsers(), testGrp1);

      // This should throw an exception
      getUsersUninitialized.expect(UninitializedObjectException.class);
      getUsersUninitialized.expectMessage("Link must be initialized before Users is set.");
      link2.getUsers();
   }

   /** Tests the establish method */
   @Rule
   public ExpectedException establishUninitialized = ExpectedException.none();
   @Test
   public void testEstablish() throws UninitializedObjectException {

      Link link1 = createTestLink();
      Link link2 = new Link();

      SocialNetworkStatus status = new SocialNetworkStatus();

      link1.establish(new Date(2000, 1, 1), status);
      assertEquals("Link was not established.", status.getStatus(), SocialNetworkStatus.Status.SUCCESS);

      // Can't establish a link if it is already established
      link1.establish(new Date(2000, 2, 1), status);
      assertEquals("Link cannot be established without first being torn down.", status.getStatus(), SocialNetworkStatus.Status.ALREADY_ACTIVE);

      // Can't establish a link before it was established
      link1.establish(new Date(1999, 1, 1), status);
      assertEquals("Can't establish a link before the current date.", status.getStatus(), SocialNetworkStatus.Status.INVALID_DATE);

      // Should throw an exception because it is uninitialized
      establishUninitialized.expect(UninitializedObjectException.class);
      establishUninitialized.expectMessage("Link must be initialized before a new event is set.");
      link2.establish(new Date(), status);
   }

   /** Tests null pointer exception in establish */
   @Rule
   public ExpectedException establishNull = ExpectedException.none();
   @Test
   public void testEstablishNull() throws UninitializedObjectException {
      Link link1 = createTestLink();
      SocialNetworkStatus status = new SocialNetworkStatus();

      establishNull.expect(NullPointerException.class);
      establishNull.expectMessage("Date may not be null.");
      link1.establish(null, null);
   }

   /** Tests the tearDown method */
   @Rule
   public ExpectedException tearDownUninitialized = ExpectedException.none();
   @Test
   public void testTearDown() throws UninitializedObjectException {

      Link link1 = createEstablishedLink();
      Link link2 = new Link();

      SocialNetworkStatus status = new SocialNetworkStatus();

      // Cannot tear down a link in the past
      link1.tearDown(new Date(1999, 1, 1), status);
      assertEquals("Link must be torn down after it was created.", status.getStatus(), SocialNetworkStatus.Status.INVALID_DATE);
      link1.tearDown(new Date(2000, 2, 2), status);
      assertEquals("Link was not torn down.", status.getStatus(), SocialNetworkStatus.Status.SUCCESS);
      link1.tearDown(new Date(2000, 2, 5), status);
      assertEquals("Link must be established in order to be torn down.", status.getStatus(), SocialNetworkStatus.Status.ALREADY_INACTIVE);

      // Should throw an exception because it is uninitialized
      tearDownUninitialized.expect(UninitializedObjectException.class);
      tearDownUninitialized.expectMessage("Link must be initialized before a new event is set.");
      link2.tearDown(new Date(),status);
   }

   /** Tests null pointer exception in tearDown */
   @Rule
   public ExpectedException tearDownNull = ExpectedException.none();
   @Test
   public void testTearDownNull() throws UninitializedObjectException {
      Link link1 = createTestLink();
      SocialNetworkStatus status = new SocialNetworkStatus();

      tearDownNull.expect(NullPointerException.class);
      tearDownNull.expectMessage("Date may not be null.");
      link1.tearDown(null, null);
   }

   /** Tests the isActive method */
   @Rule
   public ExpectedException isActiveUninitialized = ExpectedException.none();
   @Test
   public void testIsActive() throws UninitializedObjectException {

      Link link1 = createHistoriedLink();
      Link link2 = new Link();

      // Should be active on the day it was created
      assertTrue("Link should be active at the time it was created.", link1.isActive(new Date(2000, 1, 1)));
      assertFalse("Link should be invalid.", link1.isActive(new Date(2000, 6, 6)));

      assertTrue("Link should be active.", link1.isActive(new Date(2000, 1, 15)));
      assertTrue("Link should be active.", link1.isActive(new Date(2000, 2, 2)));
      assertTrue("Link should be active.", link1.isActive(new Date(2000, 2, 5)));
      assertFalse("Link should be inactive.", link1.isActive(new Date(2000, 2, 17)));

      // Should be true because the link was established on this day
      assertTrue("Link should be active on the day it is established.", link1.isActive(new Date(2000, 3, 3)));

      // Throws exception because date is invalid
      isActiveUninitialized.expect(UninitializedObjectException.class);
      isActiveUninitialized.expectMessage("Link must be initialized before checking events is set.");
      link2.isActive(new Date());
   }

   /** Tests the firstEvent method */
   @Rule
   public ExpectedException firstEventUninitialized = ExpectedException.none();
   @Test
   public void testFirstEvent() throws UninitializedObjectException {

      Link link1 = createTestLink();
      Link link2 = createEstablishedLink();
      Link link3 = createHistoriedLink();
      Link link4 = new Link();

      assertNull("A new link should have no events.", link1.firstEvent());

      link2 = createEstablishedLink();
      assertEquals("First Event is incorrect.", link2.firstEvent(), new Date(2000, 1, 1));

      link3 = createHistoriedLink();
      assertEquals("First Event is incorrect.", link3.firstEvent(), new Date(2000, 1, 1));

      // If it's not yet valid, throws an excpetion
      firstEventUninitialized.expect(UninitializedObjectException.class);
      firstEventUninitialized.expectMessage("Link must be initialized before First Event is set.");
      link4.firstEvent();
   }

   /** Tests the lastEvent method */
   @Rule
   public ExpectedException lastEventUninitialized = ExpectedException.none();
   @Test
   public void testLastEvent() throws UninitializedObjectException {

      Link link1 = createTestLink();
      Link link2 = createEstablishedLink();
      Link link3 = createHistoriedLink();
      Link link4 = new Link();

      assertNull("A new link should have no events.", link1.lastEvent());

      link2 = createEstablishedLink();
      assertEquals("Last Event is incorrect.", link2.lastEvent(), new Date(2000, 1, 1));

      link3 = createHistoriedLink();
      assertEquals("Last Event is incorrect.", link3.lastEvent(), new Date(2000, 3, 4));

      // If it's not yet valid, throws an excpetion
      lastEventUninitialized.expect(UninitializedObjectException.class);
      lastEventUninitialized.expectMessage("Link must be initialized before Last Event is set.");
      link4.lastEvent();
   }

   /** Tests the nextEvent method */
   @Rule
   public ExpectedException nextEventUninitialized = ExpectedException.none();
   @Test
   public void testNextEvent() throws UninitializedObjectException {

      Link link1 = createHistoriedLink();
      Link link2 = new Link();

      assertEquals("Next event incorrect when input before original date.", link1.nextEvent(new Date(1999, 12, 12)), new Date(2000, 1, 1));
      assertEquals("Next event incorrect when on the same date as an event.", link1.nextEvent(new Date(2000, 1, 1)), new Date(2000, 2, 2));
      assertEquals("Next event incorrect.", link1.nextEvent(new Date(2000, 1, 11)), new Date(2000, 2, 2));
      assertEquals("Next event incorrect.", link1.nextEvent(new Date(2000, 2, 15)), new Date(2000, 3, 3));
      assertNull("Next event incorrect when after all events.", link1.nextEvent(new Date(2000, 4, 4)));

      // Throws Excpetion
      nextEventUninitialized.expect(UninitializedObjectException.class);
      nextEventUninitialized.expectMessage("Link must be initialized before checking events is set.");
      link2.nextEvent(new Date()); 
   }

   /** Tests the previousEvent method */
   @Test
   public void testPreviousEvent() throws UninitializedObjectException {

      Link link1 = createTestLink();
      SocialNetworkStatus status = new SocialNetworkStatus();
      link1.establish(new Date(1990, 1, 2), status);
      link1.tearDown(new Date(1990, 1, 6), status);

      assertEquals(link1.previousEvent(new Date(1990, 1, 6)), new Date(1990, 1, 2));

      Link link2 = createHistoriedLink();

      assertNull(link2.previousEvent(new Date(1999, 1, 6)));
      assertEquals(link2.previousEvent(new Date(2000, 3, 10)), new Date(2000, 3, 4));
      assertEquals(link2.previousEvent(new Date(2000, 3, 2)), new Date(2000, 2, 14));
      assertEquals(link2.previousEvent(new Date(2000, 2, 2)), new Date(2000, 1, 1));
   }

   /** Tests the toString method */
   @Test
   public void testToString() {

      Link link1 = new Link();
      Link link2;
      try {
         link2 = createHistoriedLink();
      }
      catch (UninitializedObjectException uoe) {
         link2 = new Link();
      }

      assertEquals("toString incorrect for invalid link.", link1.toString(), "Invalid Link: Unitialized IDs");
      assertEquals("toString incorrect.", link2.toString(), "The link between abc123 and xyz456\nLink established on 2000.01.01\nLink torn down on 2000.02.02\nLink established on 2000.02.02\nLink torn down on 2000.02.14\nLink established on 2000.03.03\nLink torn down on 2000.03.04");
   }
}