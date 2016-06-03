/**
 * 
 */
package com.luchoct.ghost.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luchoct.ghost.test.BaseServiceTest;

/**
 * @author Luis
 * 
 */
public class DictionaryFileLoaderTest extends BaseServiceTest {

  @Autowired
  /**
   * Service to test
   */
  private DictionaryFileLoaderService dictionaryFileLoaderService;

  @Test
  /**
   * It tests the initialization of the service
   */
  public void testService() {
    assertNotNull("service not initialised", dictionaryFileLoaderService);
  }

  @After
  public void freeDictionary() {
    /*
     *  I force the garbage collector because the dictionary can need quite a lot
     *  of memory.
     */
    Runtime.getRuntime().gc();
  }

  /**
   * It tests that the structure is loaded correctly, indexed only with odd
   * length prefixes.
   */
  @Test
  public void testOptimizedDictionary() {

    /*
     * The dictionary test file is placed in /dictionaries/word.lst
     */
    dictionaryFileLoaderService.setDictionaryFilePath(File.separator
        + "dictionaries" + File.separator + "word.lst");
    TreeMap<String, SortedSet<String>> dictionary = dictionaryFileLoaderService
        .loadDictionnary();
    assertFalse("dictionary empty", dictionary.isEmpty());
    /*
     * It tests that the word dynamias is correctly split.
     */
    assertTrue("prefix expected", dictionary.containsKey("a"));
    assertTrue("suffix expected", dictionary.get("a").contains("dynamias"));
    assertFalse("prefix unexpected", dictionary.containsKey("ad"));
    assertTrue("prefix expected", dictionary.containsKey("ady"));
    assertTrue("suffix expected", dictionary.get("ady").contains("namias"));
    assertFalse("prefix unexpected", dictionary.containsKey("adyn"));
    assertTrue("prefix expected", dictionary.containsKey("adyna"));
    assertTrue("suffix expected", dictionary.get("adyna").contains("mias"));
    assertFalse("prefix unexpected", dictionary.containsKey("adynam"));
    assertTrue("prefix expected", dictionary.containsKey("adynami"));
    assertTrue("suffix expected", dictionary.get("adynami").contains("as"));
    assertFalse("prefix unexpected", dictionary.containsKey("adynamia"));
    assertTrue("prefix expected", dictionary.containsKey("adynamias"));
    assertTrue("suffix expected", dictionary.get("adynamias").contains(""));
  }

}
