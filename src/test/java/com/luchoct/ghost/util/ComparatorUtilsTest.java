/**
 * 
 */
package com.luchoct.ghost.util;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Luis
 *
 */
public class ComparatorUtilsTest {

	/**
	 * Test the "shorter" comparator (the shortest word before). 
	 */
	@Test
	public void testShorterComparator() {
		
		String [] words = {"nes", "ning", "st", "sted", "sting"};
		List<String> listWords = Arrays.asList(words);
		Collections.sort(listWords,ComparatorUtils.FIRST_SHORTER_COMPARATOR);
		String [] sortedWords = {"st", "nes", "ning", "sted", "sting"};
		
		assertArrayEquals("Order unexpected", sortedWords, listWords.toArray());	
	}
		
	/**
	 * Test the "longer" comparator (the longest word before). 
	 */
	@Test
	public void testLongerComparator() {
		
		String [] words = {"nes", "ning", "st", "sted", "sting"};
		List<String> listWords = Arrays.asList(words);
		Collections.sort(listWords,ComparatorUtils.FIRST_LONGER_COMPARATOR);
		String [] sortedWords = {"sting", "ning", "sted", "nes", "st"};
		
		assertArrayEquals("Order unexpected", sortedWords, listWords.toArray());	
	}
		
}
