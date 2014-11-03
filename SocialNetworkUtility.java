/** A collection of methods used by classes making up the Social Network.
  * @author Billy Barbaro
  */
public class SocialNetworkUtility {
	
  // An enum to let helper methods know their caller
   public enum Caller {
      ESTABLISH,
      TEARDOWN
   };

	/** Checks the validity of this link. If it is uninitialized, throws an exception
    * @param o    the object to be tested if it is null
    * @param type   a string to help customize the message thrown with the excpetion
    * @throws NullPointerException  if the object passed in is null
    */
   public static void checkNull(Object o, String type) {
      if (o == null) 
         throw new NullPointerException(type + " may not be null.");
   }

   public static void checkValid(SocialNetworkObject sno, String objectType, String field) throws UninitializedObjectException {
      if (!sno.isValid())
         throw new UninitializedObjectException(objectType + " must be initialized before " + field + " is set.");
   }
}