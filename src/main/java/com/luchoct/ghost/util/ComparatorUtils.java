/**
 *
 */
package com.luchoct.ghost.util;

import java.util.Comparator;

/**
 * @author Luis
 */
public final class ComparatorUtils {

	/**
	 * It's an utility class. You can't create an instance of the class.
	 */
	private ComparatorUtils() {
	}

	/**
	 * It's a comparator that puts before the shortest strings.
	 */
	public static final Comparator<String> FIRST_SHORTER_COMPARATOR = new Comparator<String>() {

		@Override
		public int compare(final String o1, final String o2) {
			if (o1 == null) {
				throw new IllegalArgumentException(
						"First argument of comparator can't be null");
			} else if (o2 == null) {
				throw new IllegalArgumentException(
						"Second argument of comparator can't be null");
			} else {
				return (o1.length() - o2.length());
			}
		}
	};

	/**
	 * It's a comparator that puts before the longest strings.
	 */
	public static final Comparator<String> FIRST_LONGER_COMPARATOR = new Comparator<String>() {

		@Override
		public int compare(final String o1, final String o2) {
			if (o1 == null) {
				throw new IllegalArgumentException(
						"First argument of comparator can't be null");
			} else if (o2 == null) {
				throw new IllegalArgumentException(
						"Second argument of comparator can't be null");
			} else {
				return (o2.length() - o1.length());
			}
		}
	};

}
