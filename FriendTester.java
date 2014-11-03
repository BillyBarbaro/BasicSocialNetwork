import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/** Unit tests the Friend object in the Social Network.
  * @author Billy Barbaro
  */
public class FriendTester {
	
	/** Tests the constructor */
	@Test
	public void testConstructor() {
		Friend test = new Friend();
    	assertTrue("Error in the Constructor", test != null);
	}

	/** Tests the set method and isValid */
	@Rule
   public ExpectedException setNullException = ExpectedException.none();
	@Test
	public void testSet() {
		User test = new User();
		test.setID("friend");
		Friend newFriend = new Friend();

		assertFalse("A newly created Friend should be invalid", newFriend.isValid());

		assertFalse("Distance may not be negative.", newFriend.set(test, -5));
		assertFalse("When set fails Friend remains invalid", newFriend.isValid());

		assertTrue("Friend not set", newFriend.set(test, 1));
		assertTrue("After set friend is valid", newFriend.isValid());

		assertFalse("Friend may not be reset", newFriend.set(test, 14));
		assertTrue("After failed reset friend is valid", newFriend.isValid());

		Friend nullFriend = new Friend();
		setNullException.expect(NullPointerException.class);
      setNullException.expectMessage("User may not be null.");
      nullFriend.set(null, 5);
	}

	/** Tests the getUser method */
	@Rule
   public ExpectedException getUserInvalid = ExpectedException.none();
	@Test
	public void testGetUser() throws UninitializedObjectException {
		Friend best = new Friend();
		User mike = new User();
		mike.setID("Mike");
		User james = new User();
		james.setID("James");

		best.set(mike, 0);
		assertFalse("Wrong user returned", best.getUser().equals(james));
		assertTrue("Wrong user returned", best.getUser().equals(mike));

		Friend fail = new Friend();
		getUserInvalid.expect(UninitializedObjectException.class);
      getUserInvalid.expectMessage("Friend must be initialized before User is set.");
      fail.getUser();
	}

	/** Tests the getDistance method */
	@Rule
   public ExpectedException getDistanceInvalid = ExpectedException.none();
	@Test
	public void testGetDistance() throws UninitializedObjectException {
		Friend best = new Friend();
		User mike = new User();
		mike.setID("Mike");

		best.set(mike, 0);
		assertFalse("Wrong distance returned", best.getDistance() == 5);
		assertTrue("Wrong distance returned", best.getDistance() == 0);

		Friend fail = new Friend();
		getDistanceInvalid.expect(UninitializedObjectException.class);
      getDistanceInvalid.expectMessage("Friend must be initialized before Distance is set.");
      fail.getDistance();
	}

	/** Tests the toString method */
	@Test
	public void testToString() {
		Friend best = new Friend();
		User kevin = new User();
		kevin.setID("Kevin");

		assertEquals("Invalid String incorrect", best.toString(), "Invalid Friend");

		best.set(kevin, 2);
		assertEquals("Valid toString incorrect", best.toString(), "Kevin is 2 users away from you.");
	}

	/** Tests the equals method */
	@Test
	public void testEquals() {
		Friend best = new Friend();
		Friend otherBest = new Friend();
		Friend distant = new Friend();

		User harry = new User();
		harry.setID("Harry");
		User tim = new User();
		tim.setID("Tim");

		best.set(harry, 0);
		otherBest.set(tim, 0);
		distant.set(harry, 18);

		assertTrue("Same object should be equal", best.equals(best));
		assertFalse("Friends of different Users should not be equal", best.equals(otherBest));
		assertTrue("Friends of the same user with different distances should be equal", best.equals(distant));
	}
}