/**
 * 
 */
package com.luchoct.ghost.util;

/**
 * @author Luis
 * 
 */
public final class NumberUtils {
	
	/**
	 * It's an utility class. You can't create an instance of the class.
	 */
	private NumberUtils() {
	}

	/**
	 * It returns whether the number is an even number.
	 * @param number The number to test.
	 * @return true if the number is an even number; false if the number is an odd
	 *         number.
	 */
	public static boolean isEven(final long number) {
		return ((number % 2) == 0);
	}
}
