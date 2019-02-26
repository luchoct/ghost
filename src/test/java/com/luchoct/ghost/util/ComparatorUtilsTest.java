/**
 *
 */
package com.luchoct.ghost.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Luis
 */
public class ComparatorUtilsTest {

	/**
	 * Test the "shorter" comparator (the shortest word before).
	 */
	@Test
	public void testShorterComparator() {

		List<String> listWords = Arrays.asList(new String[] {"nes", "ning", "st", "sted", "sting"});
		Collections.sort(listWords, ComparatorUtils.FIRST_SHORTER_COMPARATOR);
		List<String> sortedWords = Arrays.asList(new String[] {"st", "nes", "ning", "sted", "sting"});

		assertThat("Order unexpected",
				listWords,
				equalTo(sortedWords));
	}

	/**
	 * Test the "longer" comparator (the longest word before).
	 */
	@Test
	public void testLongerComparator() {

		List<String> listWords = Arrays.asList(new String[] {"nes", "ning", "st", "sted", "sting"});
		Collections.sort(listWords, ComparatorUtils.FIRST_LONGER_COMPARATOR);
		List<String> sortedWords = Arrays.asList(new String[] {"sting", "ning", "sted", "nes", "st"});

		assertThat("Order unexpected",
				listWords,
				equalTo(sortedWords));
	}

}
