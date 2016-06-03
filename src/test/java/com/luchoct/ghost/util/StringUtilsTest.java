/**
 * 
 */
package com.luchoct.ghost.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author Luis
 *
 */
public class StringUtilsTest {
	
	/**
	 * Test the function that returns if some word starts with the prefix given. 
	 */
	@Test
	public void testIsContainedInSomeWord() {
		
		String [] words = {"ne", "ned", "nes", "ning", "st", "sted", "sting", "sts"};
		List<String> listWords = Arrays.asList(words);
		
		assertTrue("Prefix contained",StringUtils.containsPrefix(listWords, "sti"));
		assertTrue("Prefix contained",StringUtils.containsPrefix(listWords, "ne"));
		assertTrue("Prefix contained",StringUtils.containsPrefix(listWords, "ni"));
		assertTrue("Prefix contained",StringUtils.containsPrefix(listWords, "s"));
		assertFalse("Prefix not contained",StringUtils.containsPrefix(listWords, "sta"));
		assertFalse("Prefix not contained",StringUtils.containsPrefix(listWords, "a"));
		assertFalse("Prefix not contained",StringUtils.containsPrefix(listWords, "no"));
	}

	/**
	 * Test the function that returns if a word starts with some of the prefixes given. 
	 */
	@Test
	public void testContainsSomePrefix() {
		
		String [] prefixes = {"ne", "ned", "nes", "ning", "st", "sted", "sting", "sts"};
		List<String> listPrefixes = Arrays.asList(prefixes);
		
		assertTrue("Prefix contained",StringUtils.containsSomePrefix("stedadsdf", listPrefixes));
		assertTrue("Prefix contained",StringUtils.containsSomePrefix("neasdsadfa", listPrefixes));
		assertTrue("Prefix contained",StringUtils.containsSomePrefix("ningsdfsf", listPrefixes));
		assertFalse("Prefix not contained",StringUtils.containsSomePrefix("nasfds", listPrefixes));
		assertFalse("Prefix not contained",StringUtils.containsSomePrefix("abdsfsd", listPrefixes));
		assertFalse("Prefix not contained",StringUtils.containsSomePrefix("ninsdfdsf", listPrefixes));
		assertFalse("Prefix not contained",StringUtils.containsSomePrefix("swerwrw", listPrefixes));
	}
}
