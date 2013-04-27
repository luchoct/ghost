/**
 * 
 */
package com.luisgal.ghost.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luisgal.ghost.util.NumberUtils;

/**
 * It loads a dictionary from a file into a structure useful for the second
 * player.
 * @author Luis
 * 
 */
public class DictionaryFileLoaderService implements DictionaryLoader {

  /**
   * The logger of the class.
   */
  private static final Logger LOGGER = LoggerFactory
      .getLogger(DictionaryFileLoaderService.class);

  /**
   * The file path of the dictionary to load.
   */
  private String dictionaryFilePath;

  /**
   * It sets the file path of the dictionary to load.
   * 
   * @param path The file path of the dictionary to load.
   */
  public final void setDictionaryFilePath(final String path) {
    dictionaryFilePath = path;
  }

  /**
   * It returns the file path of the dictionary to load.
   * 
   * @return The file path of the dictionary to load.
   */
  public final String getDictionaryFilePath() {
    return dictionaryFilePath;
  }

  /**
   * {@inheritDoc}
   */
  public final TreeMap<String, SortedSet<String>> loadDictionnary() {
    LOGGER.debug("Opening dictionary file {}", dictionaryFilePath);
    TreeMap<String, SortedSet<String>> dictionary = new TreeMap<String, SortedSet<String>>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(this.getClass()
          .getResource(dictionaryFilePath).getPath()));
      try {
        String line = reader.readLine();
        while (line != null) {
          // We ignore spaces at the beginning and at the end of the
          // word.
          if (!"".equals(line.trim())) {
            // One word per line.
            appendWord(dictionary, line);
          }
          line = reader.readLine();
        }
      } finally {
        reader.close();
      }
    } catch (IOException ex) {
      LOGGER.error("Exception opening file {}", dictionaryFilePath, ex);
    }
    if (LOGGER.isDebugEnabled()) {
      for (String prefix : dictionary.keySet()) {
        LOGGER.debug("Dictionary prefix <{}> suffixes <{}>", prefix, dictionary.get(prefix));
      }
    }
    return dictionary;
  }

  /**
   * It appends a word to the dictionary structure. The keys are prefixes and
   * the values are the suffixes that produce a word with the corresponding
   * prefix, in alphabetical order. Only odd length prefixes are used as keys.
   * @param dictionary Dictionary structure to complete.
   * @param word Word to add.
   */
  private void appendWord(final Map<String, SortedSet<String>> dictionary,
      final String word) {
    LOGGER.trace("Appending word {} to dictionary", word);
    char[] letters = word.toCharArray();
    /*
     * It creates the keys and the values of the map. If the word has got an odd
     * length the suffix can be ""
     */

    for (int i = 0; i < letters.length; i++) {
      /*
       * It's possible optimize the memory adding only the prefix with an odd
       * length. I will add all the prefixes to be able to give more information
       * to the user.
       */
      if (NumberUtils.isEven(i)) {
        /*
         * If i = (letters.length - 1) the prefix is the whole word and the
         * suffix is "".
         */
        String prefix = word.substring(0, i + 1);
        String suffix = word.substring(i + 1);
        LOGGER.trace("Appending (prefix,suffix) = ({},{})", prefix, suffix);
        SortedSet<String> suffixes = dictionary.get(prefix);
        // Maybe, it's the first word with that prefix.
        if (suffixes == null) {
          suffixes = new TreeSet<String>();
          dictionary.put(prefix, suffixes);
        }
        suffixes.add(suffix);
      }
    }
  }
}
