package util;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.contact.ContactPoint;

/**
 * Utility functions for a physic listener.
 * @author Michael Thompson
 *
 */
public class ListenerUtil
{
	/**
	 * Checks the userdata of the contact point bodies/fixtures for 2 objects of a
	 * specific type of userdata have collided. Returns null if one or more are not of the specified types.
	 * 
	 * @param p_point Contact point object.
	 * @param p_class1 Class of first object.
	 * @param p_checkFixture1 If true, it will check the fixture for p_class1.
	 * @param p_class2 Class of second object.
	 * @param p_checkFixture2 If true, it will check the fixture for p_class2.
	 * @return Returns a contact pair with the objects in the order as you specified them in p_class1 and p_class2.
	 */
	@SuppressWarnings("unchecked")
	public static <T1, T2> Pair<T1, T2> checkUserDataForClasses(ContactPoint p_point
			, Class<T1> p_class1, boolean p_checkFixture1, Class<T2> p_class2, boolean p_checkFixture2)
	{
		return checkUserDataForClasses(p_point.getBody1(), p_point.getFixture1(), p_point.getBody2(), p_point.getFixture2(),
				p_class1, p_checkFixture1, p_class2, p_checkFixture2);
	}
	
	/**
	 * Checks the userdata of the contact point bodies/fixtures for 2 objects of a
	 * specific type of userdata have collided. Returns null if one or more are not of the specified types.
	 * 
	 * @param p_body1 First body.
	 * @param p_fixture1 First fixture.
	 * @param p_body2 Second body.
	 * @param p_fixture2 Second fixture.
	 * @param p_class1 Class of first object.
	 * @param p_checkFixture1 If true, it will check the fixture for p_class1.
	 * @param p_class2 Class of second object.
	 * @param p_checkFixture2 If true, it will check the fixture for p_class2.
	 * @return Returns a contact pair with the objects in the order as you specified them in p_class1 and p_class2.
	 */
	@SuppressWarnings("unchecked")
	public static <T1, T2> Pair<T1, T2> checkUserDataForClasses(Body p_body1, Fixture p_fixture1, Body p_body2, Fixture p_fixture2
			, Class<T1> p_class1, boolean p_checkFixture1, Class<T2> p_class2, boolean p_checkFixture2)
	{
		Object userData1 = (p_checkFixture1 ? p_fixture1 : p_body1).getUserData();
		Object userData2 = (p_checkFixture2 ? p_fixture2 : p_body2).getUserData();
		
		if (userData1 != null && userData2 != null &&
				p_class1.isAssignableFrom(userData1.getClass()) &&
				p_class2.isAssignableFrom(userData2.getClass()))
			return new Pair<T1, T2>((T1)userData1, (T2)userData2);
		
		// Flip the booleans if necessary
		if (p_checkFixture1 != p_checkFixture2)
		{
			userData1 = (p_checkFixture2 ? p_fixture1 : p_body1).getUserData();
			userData2 = (p_checkFixture1 ? p_fixture2 : p_body2).getUserData();
		}
		
		// Check with the objects flipped
		if (userData1 != null && userData2 != null &&
				p_class1.isAssignableFrom(userData2.getClass()) &&
				p_class2.isAssignableFrom(userData1.getClass()))
			return new Pair<T1, T2>((T1)userData2, (T2)userData1); // Unflip it
		
		// No matches
		return null;
	}
}
