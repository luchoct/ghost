/**
 *
 */
package com.luchoct.ghost.service;

import com.luchoct.ghost.test.SpringTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.SortedSet;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.in;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Luis
 */
public class DictionaryFileLoaderTest extends SpringTest {

	/**
	 * Service to test
	 */
	@Autowired
	private DictionaryFileLoaderService dictionaryFileLoaderService;

	/**
	 * It tests the initialization of the service
	 */
	@Test
	public void testService() {
		assertThat("service not initialised", dictionaryFileLoaderService, notNullValue());
	}

	@AfterEach
	public void freeDictionary() {
		// I force the garbage collector because the dictionary can need quite a lot of memory.
		Runtime.getRuntime().gc();
	}

	/**
	 * It tests that the structure is loaded correctly, indexed only with odd length prefixes.
	 */
	@Test
	public void testOptimizedDictionary() {

    	// The dictionary test file is placed in /dictionaries/word.lst
		TreeMap<String, SortedSet<String>> dictionary = dictionaryFileLoaderService.loadDictionary();
		assertThat("dictionary empty", dictionary, not(anEmptyMap()));
	    // It tests that the word dynamias is correctly split.
		assertThat("prefix expected", dictionary, hasKey("a"));
		assertThat("suffix expected", "dynamias", in(dictionary.get("a")));
		assertThat("prefix unexpected", dictionary, not(hasKey("ad")));
		assertThat("prefix expected", dictionary, hasKey("ady"));
		assertThat("suffix expected", "namias", in(dictionary.get("ady")));
		assertThat("prefix unexpected", dictionary, not(hasKey("adyn")));
		assertThat("prefix expected", dictionary, hasKey("adyna"));
		assertThat("suffix expected", "mias", in(dictionary.get("adyna")));
		assertThat("prefix unexpected", dictionary, not(hasKey("adynam")));
		assertThat("prefix expected", dictionary, hasKey("adynami"));
		assertThat("suffix expected", "as", in(dictionary.get("adynami")));
		assertThat("prefix unexpected", dictionary, not(hasKey("adynamia")));
		assertThat("prefix expected", dictionary, hasKey("adynamias"));
		assertThat("suffix expected", "", in(dictionary.get("adynamias")));
	}
}
