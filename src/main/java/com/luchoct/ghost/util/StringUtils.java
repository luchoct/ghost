/**
 * 
 */
package com.luchoct.ghost.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Luis
 * 
 */
public final class StringUtils {

	/**
	 * It's an utility class. You can't create an instance of the class.
	 */
	private StringUtils() {
	}

	/**
	 * It returns whether the word contains at least one of the possible prefixes.
	 * @param word Word to test.
	 * @param possiblePrefixes Possible prefixes that can appear in the word.
	 * @return true if the word contains at least one prefix.
	 */
	public static boolean containsSomePrefix(final String word,
			final Collection<String> possiblePrefixes) {
		boolean contains = false;
		Iterator<String> itCandidates = possiblePrefixes.iterator();
		while (itCandidates.hasNext() && (!contains)) {
			if (word.startsWith(itCandidates.next())) {
				contains = true;
			}
		}
		return contains;
	}

	/**
	 * It returns whether at least a word contains the prefix.
	 * @param words Words to test.
	 * @param prefix Prefix that can appear in the words.
	 * @return true if some word contains the prefix.
	 */
	public static boolean containsPrefix(final Collection<String> words,
			final String prefix) {
		boolean contains = false;
		Iterator<String> itCandidates = words.iterator();
		while (itCandidates.hasNext() && (!contains)) {
			if (itCandidates.next().startsWith(prefix)) {
				contains = true;
			}
		}
		return contains;
	}
}
