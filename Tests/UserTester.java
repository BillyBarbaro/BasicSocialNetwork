import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/** Unit tests the User object in the Social Network.
  * @author Billy Barbaro
  */

public class UserTester {

   /** Tests the class constructor */
   @Test
   public void testConstructor() {
      User test = new User();
      assertTrue("Error in the Constructor", test != null);
   }

   /** Tests the setID method */
   @Rule
   public ExpectedException idNullException = ExpectedException.none();
   @Test
   public void testSetID() {

      // Sets an ID and makes sure we can't set it a second time
      User test1 = new User();
      assertTrue("Set ID not working.", test1.setID("abc123"));
      assertTrue("User should is not valid.", test1.isValid());
      assertFalse("ID must be immutable.", test1.setID("xyz456"));
      assertTrue("User should be valid.", test1.isValid());

      // Makes sure we can't make a user's ID empty and even after refusing an ID, that we can set it
      User test2 = new User();
      assertFalse("Cannot set ID to empty string.", test2.setID(""));
      assertFalse("User should be invalid.", test2.isValid());
      assertTrue("ID wasn't set.", test2.setID("Test"));
      assertTrue("User wasn't valid/", test2.isValid());

      // Should throw an exception
      User test3 = new User();
      idNullException.expect(NullPointerException.class);
      idNullException.expectMessage("ID may not be null.");
      test3.setID(null);
   }

   /** Tests the getID method */
   @Test
   public void testGetID() {

      // Sets the ID for a user and makes sure it matches.
      User test = new User();
      assertNull(test.getID());
      test.setID("abc123");
      assertEquals("ID incorrect", test.getID(), "abc123");

      // Verifies that the ID doesn't change
      test.setID("xyz456");
      assertEquals("ID cannot be changed.", test.getID(), "abc123");
   }

   /** Creates a valid user for testing */
   private User createValidUser() {
      User test = new User();
      test.setID("abc123");
      return test;
   }

   /** Tests the getter/setter methods for firstName */
   @Test
   public void testFirstName() throws UninitializedObjectException{
      User test = createValidUser();
      assertEquals("First name not set", test.setFirstName("John"), test);
      assertEquals("First name unexpected.", test.getFirstName(), "John");
      assertEquals("First name not changed.", test.setFirstName("Max"), test);
      assertEquals("First name not changed.", test.getFirstName(), "Max");
   }

   /** Tests that setFirstName can't take a null argument */
   @Rule
   public ExpectedException firstNameNullException = ExpectedException.none();

   @Test
   public void testNullFirstName() throws UninitializedObjectException{
      User test = createValidUser();
      firstNameNullException.expect(NullPointerException.class);
      firstNameNullException.expectMessage("First Name may not be null.");
      test.setFirstName(null);
   }

   /** Tests the setFirstName throws an excpetion if the user is not valid */
   @Rule
   public ExpectedException firstNameInvalidExcpetion = ExpectedException.none();
   @Test
   public void testInvalidFirstName() throws UninitializedObjectException{
      User test = new User();
      firstNameInvalidExcpetion.expect(UninitializedObjectException.class);
      firstNameInvalidExcpetion.expectMessage("User must be initialized before First Name is set.");
      test.setFirstName("John");
   }

   /** Tests the getter/setter methods for middleName */
   @Test
   public void testMiddleName() throws UninitializedObjectException{
      User test = createValidUser();
      assertEquals("Middle name not set", test.setMiddleName("John"), test);
      assertEquals("Middle name incorrect", test.getMiddleName(), "John");
      assertEquals("Middle name not changed", test.setMiddleName("Max"), test);
      assertEquals("Middle name not changed", test.getMiddleName(), "Max");
   }

   /** Tests that setMiddleName can't take a null argument */
   @Rule
   public ExpectedException middleNameNullException = ExpectedException.none();
   @Test
   public void testNullMiddleName() throws UninitializedObjectException{
      User test = createValidUser();
      middleNameNullException.expect(NullPointerException.class);
      middleNameNullException.expectMessage("Middle Name may not be null.");
      test.setMiddleName(null);
   }

   /** Tests the setMiddleName throws an excpetion if the user is not valid */
   @Rule
   public ExpectedException middleNameInvalidException = ExpectedException.none();
   @Test
   public void testInvalidMiddleName() throws UninitializedObjectException{
      User test = new User();
      middleNameInvalidException.expect(UninitializedObjectException.class);
      middleNameInvalidException.expectMessage("User must be initialized before Middle Name is set.");
      test.setMiddleName("John");
   }

   /** Tests the getter/setter methods for lastName */
   @Test
   public void testLastName() throws UninitializedObjectException{
      User test = createValidUser();
      assertEquals("Last name not set.", test.setLastName("John"), test);
      assertEquals("Last name incorrect.", test.getLastName(), "John");
      assertEquals("Last name not changed.", test.setLastName("Max"), test);
      assertEquals("Last name not changed.", test.getLastName(), "Max");
   }

   /** Tests that setLastName can't take a null argument */
   @Rule
   public ExpectedException lastNameNullException = ExpectedException.none();
   @Test
   public void testNullLastName() throws UninitializedObjectException{
      User test = createValidUser();
      lastNameNullException.expect(NullPointerException.class);
      lastNameNullException.expectMessage("Last Name may not be null.");
      test.setLastName(null);
   }

   /** Tests the setLastName throws an excpetion if the user is not valid */
   @Rule
   public ExpectedException lastNameInvalidException = ExpectedException.none();
   @Test
   public void testInvalidLastName() throws UninitializedObjectException{
      User test = new User();
      lastNameInvalidException.expect(UninitializedObjectException.class);
      lastNameInvalidException.expectMessage("User must be initialized before Last Name is set.");
      test.setLastName("John");
   }

   /** Tests the getter/setter methods for email */
   @Test
   public void testEmail() throws UninitializedObjectException{
      User test = createValidUser();
      assertEquals("Email not set.", test.setEmail("john@foo.com"), test);
      assertEquals("Email incorrect.", test.getEmail(), "john@foo.com");
      assertEquals("Email not changed.", test.setEmail("max@bar.com"), test);
      assertEquals("Email not changed.", test.getEmail(), "max@bar.com");
   }

   /** Tests that setEmail can't take a null argument */
   @Rule
   public ExpectedException emailNullException = ExpectedException.none();
   @Test
   public void testNullEmail() throws UninitializedObjectException{
      User test = createValidUser();
      emailNullException.expect(NullPointerException.class);
      emailNullException.expectMessage("Email may not be null.");
      test.setEmail(null);
   }

   /** Tests the setEmail throws an excpetion if the user is not valid */
   @Rule
   public ExpectedException emailInvalidException = ExpectedException.none();
   @Test
   public void testInvalidEmail() throws UninitializedObjectException{
      User test = new User();
      emailInvalidException.expect(UninitializedObjectException.class);
      emailInvalidException.expectMessage("User must be initialized before Email is set.");
      test.setEmail("john@foo.com");
   }

   /** Tests the getter/setter methods for phoneNumber */
   @Test
   public void testPhoneNumber() throws UninitializedObjectException{
      User test = createValidUser();
      assertEquals("Phone Number not set.", test.setPhoneNumber("(123)555-6789"), test);
      assertEquals("Phone Number incorrect.", test.getPhoneNumber(), "(123)555-6789");
      assertEquals("Phone Number not changed.", test.setEmail("(321)555-9876"), test);
      assertEquals("Phone Number not changed.", test.getEmail(), "(321)555-9876");
   }

   /** Tests that setPhoneNumber can't take a null argument */
   @Rule
   public ExpectedException phoneNumberNullException = ExpectedException.none();
   @Test
   public void testNullPhoneNumber() throws UninitializedObjectException{
      User test = createValidUser();
      phoneNumberNullException.expect(NullPointerException.class);
      phoneNumberNullException.expectMessage("Phone Number may not be null.");
      test.setPhoneNumber(null);
   }

   /** Tests the setPhoneNumber throws an excpetion if the user is not valid */
   @Rule
   public ExpectedException phoneNumberInvalidException = ExpectedException.none();
   @Test
   public void testInvalidPhoneNumber() throws UninitializedObjectException{
      User test = new User();
      phoneNumberInvalidException.expect(UninitializedObjectException.class);
      phoneNumberInvalidException.expectMessage("User must be initialized before Phone Number is set.");
      test.setPhoneNumber("(123)555-5678");
   }

   /** Tests the isValid method */
   @Test
   public void testIsValid() {

      User test = new User();
      assertFalse("User initialized as valid", test.isValid());

      // Empty string is not a valid ID
      test.setID("");
      assertFalse("User should not be invalid.", test.isValid());

      test.setID("abc123");
      assertTrue("Should be valid", test.isValid());

      test.setID("xwy456");
      assertTrue("Should be valid", test.isValid());
   }

   /** Tests the toString method */
   @Test
   public void testToString() {

      User test = new User();
      assertEquals("Error in invalid toString.", test.toString(), "Invalid User: Uninitialized ID");

      // Empty string
      test.setID("");
      assertEquals("Error in invalid toString.", test.toString(), "Invalid User: Uninitialized ID");

      test.setID("abc123");
      assertEquals("Error in toString.", test.toString(), "User: abc123 First Name: null Middle Name: null Last Name: null Email: null Phone Number: null");

      // The ID should not be mutable
      test.setID("xyz456");
      assertEquals("Error in toString.", test.toString(), "User: abc123 First Name: null Middle Name: null Last Name: null Email: null Phone Number: null");
   }

   /** Tests the equals method */
   @Test
   public void testEquals() {
      User test1 = new User();
      User test2 = new User();
      User test3 = new User();

      test1.setID("abc123");
      test2.setID("abc123");
      test3.setID("xyz456");

      assertTrue("Objects should be equal", test1.equals(test2));
      assertFalse("Objects should be equal",test1.equals(test3));
      assertFalse("Objects should not be equal",test1.equals("abc123"));
   }
}