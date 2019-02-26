/**
 *
 */
package com.luchoct.ghost.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Luis
 */
public class StringUtilsTest {

	/**
	 * Test the function that returns if some word starts with the prefix given.
	 */
	@Test
	public void testIsContainedInSomeWord() {

		String[] words = {"ne", "ned", "nes", "ning", "st", "sted", "sting", "sts"};
		List<String> listWords = Arrays.asList(words);

		assertThat("Prefix contained", StringUtils.containsPrefix(listWords, "sti"), equalTo(true));
		assertThat("Prefix contained", StringUtils.containsPrefix(listWords, "ne"), equalTo(true));
		assertThat("Prefix contained", StringUtils.containsPrefix(listWords, "ni"), equalTo(true));
		assertThat("Prefix contained", StringUtils.containsPrefix(listWords, "s"), equalTo(true));
		assertThat("Prefix not contained", StringUtils.containsPrefix(listWords, "sta"), equalTo(false));
		assertThat("Prefix not contained", StringUtils.containsPrefix(listWords, "a"), equalTo(false));
		assertThat("Prefix not contained", StringUtils.containsPrefix(listWords, "no"), equalTo(false));
	}

	/**
	 * Test the function that returns if a word starts with some of the prefixes given.
	 */
	@Test
	public void testContainsSomePrefix() {

		String[] prefixes = {"ne", "ned", "nes", "ning", "st", "sted", "sting", "sts"};
		List<String> listPrefixes = Arrays.asList(prefixes);

		assertThat("Prefix contained", StringUtils.containsSomePrefix("stedadsdf", listPrefixes), equalTo(true));
		assertThat("Prefix contained", StringUtils.containsSomePrefix("neasdsadfa", listPrefixes), equalTo(true));
		assertThat("Prefix contained", StringUtils.containsSomePrefix("ningsdfsf", listPrefixes), equalTo(true));
		assertThat("Prefix not contained", StringUtils.containsSomePrefix("nasfds", listPrefixes), equalTo(false));
		assertThat("Prefix not contained", StringUtils.containsSomePrefix("abdsfsd", listPrefixes), equalTo(false));
		assertThat("Prefix not contained", StringUtils.containsSomePrefix("ninsdfdsf", listPrefixes), equalTo(false));
		assertThat("Prefix not contained", StringUtils.containsSomePrefix("swerwrw", listPrefixes), equalTo(false));
	}
}
