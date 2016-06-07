/**
 *
 */
package com.luchoct.ghost.service.ai;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameRating;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.service.DictionaryFileLoaderService;
import com.luchoct.ghost.test.SpringTest;

/**
 * @author Luis
 */
public class ComputerAITest extends SpringTest {

	@Autowired
	/**
	 * Service to test
	 */
	private ComputerAIService computerAIService;

	@Autowired
	/**
	 * Service required to load the dictionary.
	 */
	private DictionaryFileLoaderService dictionaryFileLoaderService;

	@After
	public void freeDictionary() {
		computerAIService.setDictionary(null);
		Runtime.getRuntime().gc();
	}

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testService() {
		assertNotNull("service not initialised", computerAIService);
		assertNotNull("service not initialised", dictionaryFileLoaderService);
	}

	private void assertMetrics(final String[] expectedWinnerSuffixes, final String[] expectedLoserSuffixes,
							   final Character[] expectedWinnerInputs, final Character[] expectedLoserInputs,
							   final GameStateDTO newState) {
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", expectedWinnerInputs, newState.getMetrics().getWinnerInputs()
				.toArray());
		assertArrayEquals("Winner suffixes unexpected",
				expectedWinnerSuffixes, newState.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected",
				expectedLoserInputs, newState.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected",
				expectedLoserSuffixes, newState.getMetrics().getLoserSuffixes().toArray());
	}

	@Test
	/**
	 * It tests the rating of the input of the first player
	 */
	public void testPlayerWin() {

		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService.loadDictionnary());
		final GameMovementDTO movement = new GameMovementDTO();

		// <untagge> suffixes <[d]>
		movement.setOldPrefix("untagg");
		movement.setNewInput('e');
		final String[] suffixes = {"d"};
		final Character[] winnerInputs = {};
		final String[] winnerSuffixes = {};
		final Character[] loserInputs = {'d'};
		final String[] loserSuffixes = {"d"};
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected", "untagged".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);
	}

	@Test
	/**
	 * It tests if the computer discover that the first player is going to lose.
	 */
	public void testPlayerWillProbablyWin() {
		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService.loadDictionnary());

		final GameMovementDTO movement = new GameMovementDTO();

		// <unt> suffixes <[idied, idier, idies, idiest, idily, idiness, idinesses,
		// idy, idying]>
		movement.setOldPrefix("unti");
		movement.setNewInput('d');
		final String[] suffixes = {"ied", "ier", "ies", "iest", "ily", "iness", "inesses", "y",
				"ying"};
		final Character[] winnerInputs = {};
		final String[] winnerSuffixes = {};
		final Character[] loserInputs = {'i', 'y'};
		final String[] loserSuffixes = {"ied", "ier", "ies", "ily", "iness", "y"};
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertEquals("New prefix unexpected", "untidi", newState.getNewPrefix());
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_PROBABLY_WIN, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);
	}

	@Test
	/**
	 * It tests a game state where both players can win.
	 */
	public void testPlayerMayWin() {
		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService.loadDictionnary());

		final GameMovementDTO movement = new GameMovementDTO();

		// <unt> suffixes <[imelier, imeliest, imeliness, imelinesses, imely,
		// imeous]>
		movement.setOldPrefix("unti");
		movement.setNewInput('m');
		final String[] suffixes = {"elier", "eliest", "eliness", "elinesses", "ely", "eous"};
		final Character[] winnerInputs = {};
		final String[] winnerSuffixes = {"eliest", "eous"};
		final Character[] loserInputs = {};
		final String[] loserSuffixes = {"elier", "eliness", "ely"};

		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected", "untime".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_MAY_WIN, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);

		/*
		 * <alleg> suffixes [ation, ations, e, ed, edly, er, ers, es, iance, iances,
		 * iant, ing, orical, orically, oricalness, oricalnesses, ories, orise,
		 * orised, orises, orising, orist, orists, orization, orizations, orize,
		 * orized, orizer, orizers, orizes, orizing, ory, retto, rettos, ro, ros]
		 */
		movement.setOldPrefix("alle");
		movement.setNewInput('g');
		final String[] suffixes2 = {"ation", "ations", "e", "ed", "edly", "er", "ers", "es", "iance", "iances", "iant",
				"ing", "orical", "orically", "oricalness", "oricalnesses", "ories", "orise", "orised", "orises",
				"orising", "orist", "orists", "orization", "orizations", "orize", "orized", "orizer", "orizers",
				"orizes", "orizing", "ory", "retto", "rettos", "ro", "ros"};
		final Character[] winnerInputs2 = {};
		final String[] winnerSuffixes2 = {"iant", "orical", "ro"};
		final Character[] loserInputs2 = {'a', 'e'};
		final String[] loserSuffixes2 = {"ation", "e", "iance", "ing", "ories", "orise", "orising", "orist",
				"orization", "orize", "orizing", "ory", "retto"};
		newState = computerAIService.getNextState(movement);
		assertTrue(
				"New prefix unexpected",
				"allegi".equals(newState.getNewPrefix())
						|| "allego".equals(newState.getNewPrefix())
						|| "allegr".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_MAY_WIN, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes2, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes2, loserSuffixes2, winnerInputs2, loserInputs2, newState);
	}

	@Test
	/**
	 * It tests if the computer discover that the first player is going to lose.
	 */
	public void testPlayerWillLost() {

		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());

		final GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untw");
		movement.setNewInput('i');
		final String[] suffixes = {"ne", "ned", "nes", "ning", "st", "sted", "sting", "sts"};
		final Character[] winnerInputs = {'n', 's'};
		final String[] winnerSuffixes = {"ne", "ning", "st"};
		final Character[] loserInputs = {};
		final String[] loserSuffixes = {};
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue(
				"New prefix unexpected",
				"untwin".equals(newState.getNewPrefix()) || "untwis".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_LOST, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);

		// <unt> suffixes <[hread, hreaded, hreading, hreads, hreatening, hrifty,
		// hrone, hroned, hrones, hroning]>
		movement.setOldPrefix("unth");
		movement.setNewInput('r');
		final String[] suffixes2 = {"ead", "eaded", "eading", "eads", "eatening", "ifty", "one", "oned", "ones",
				"oning"};
		final Character[] winnerInputs2 = {'i'};
		final String[] winnerSuffixes2 = {"eatening", "ifty"};
		final Character[] loserInputs2 = {'o'};
		final String[] loserSuffixes2 = {"ead", "one", "oning"};
		newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected", "unthri".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_LOST, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes2, newState.getSuffixes().toArray());
		assertMetrics(winnerSuffixes2, loserSuffixes2, winnerInputs2, loserInputs2, newState);
	}

	@Test
	/**
	 * It tests a whole word of the first player
	 */
	public void testPlayerLostWholeWords() {

		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService.loadDictionnary());

		final GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwis");
		movement.setNewInput('t');
		final String[] suffixes = {"", "ed", "ing", "s"};
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected", "untwist".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_LOST_WORD_COMPLETED, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes().toArray());
		assertNull("Metrics unexpected", newState.getMetrics());
	}

	@Test
	/**
	 * It tests a wrong prefix of the first player
	 */
	public void testPlayerLostWrongPreffix() {

		/*
		 * The dictionary test file is placed in /dictionaries/word.lst
		 */
		dictionaryFileLoaderService.setDictionaryFilePath(File.separator
				+ "dictionaries" + File.separator + "word.lst");
		computerAIService.setDictionary(dictionaryFileLoaderService.loadDictionnary());

		final GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwin");
		movement.setNewInput('o');
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected", "untwino".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_LOST_WRONG_PREFIX, newState.getRating());
		assertTrue("Suffixes unexpected", newState.getSuffixes().isEmpty());
		assertNull("Metrics unexpected", newState.getMetrics());
	}
}
