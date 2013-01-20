package com.luisgal.ghost.service;

import java.util.SortedSet;
import java.util.TreeMap;

/**
 * It loads a dictionary into a structure useful for the second player. Indexing
 * all prefixes consumes quite a lot memory.
 * @author Luis
 * 
 */
public interface DictionaryLoader {

	/**
   * The dictionary is loaded from a file, where there is a word in each line.
   * Only odd length prefixes are used as keys because the dictionary is used
   * for the player 2.
	 * @return A map where the keys are prefixes and the values are the suffixes
	 *         that produce a word with the corresponding prefix, in alphabetical
	 *         order. The map is sorted in alphabetical order too.
	 */
	TreeMap<String, SortedSet<String>> loadDictionnary();

}
