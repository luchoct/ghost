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
import com.luchoct.ghost.test.BaseServiceTest;

/**
 * @author Luis
 * 
 */
public class ComputerAITest extends BaseServiceTest {

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
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());
		GameMovementDTO movement = new GameMovementDTO();

		// <untagge> suffixes <[d]>
		movement.setOldPrefix("untagg");
		movement.setNewInput('e');
		String[] suffixes = {"d"};
		Character[] winnerInputs = {};
		String[] winnerSuffixes = {};
		Character[] loserInputs = {'d'};
		String[] loserSuffixes = {"d"};
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"untagged".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected",
				GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT, newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes, newState
				.getMetrics().getLoserSuffixes().toArray());
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
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());

		GameMovementDTO movement = new GameMovementDTO();

		// <unt> suffixes <[idied, idier, idies, idiest, idily, idiness, idinesses,
		// idy, idying]>
		movement.setOldPrefix("unti");
		movement.setNewInput('d');
		String[] suffixes = {"ied","ier","ies","iest","ily","iness","inesses","y",
				"ying"};
		Character[] winnerInputs = {};
		String[] winnerSuffixes = {};
		Character[] loserInputs = {'i','y'};
		String[] loserSuffixes = {"ied","ier","ies","ily","iness","y"};
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"untidi".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_PROBABLY_WIN,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes, newState
				.getMetrics().getLoserSuffixes().toArray());
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
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());

		GameMovementDTO movement = new GameMovementDTO();

		// <unt> suffixes <[imelier, imeliest, imeliness, imelinesses, imely,
		// imeous]>
		movement.setOldPrefix("unti");
		movement.setNewInput('m');
		String[] suffixes = {"elier","eliest","eliness","elinesses","ely","eous"};
		Character[] winnerInputs = {};
		String[] winnerSuffixes = {"eliest","eous"};
		Character[] loserInputs = {};
		String[] loserSuffixes = {"elier","eliness","ely"};

		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"untime".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_MAY_WIN,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes, newState
				.getMetrics().getLoserSuffixes().toArray());

		/*
		 * <alleg> suffixes [ation, ations, e, ed, edly, er, ers, es, iance, iances,
		 * iant, ing, orical, orically, oricalness, oricalnesses, ories, orise,
		 * orised, orises, orising, orist, orists, orization, orizations, orize,
		 * orized, orizer, orizers, orizes, orizing, ory, retto, rettos, ro, ros]
		 */
		movement.setOldPrefix("alle");
		movement.setNewInput('g');
		String[] suffixes2 = {"ation","ations","e","ed","edly","er","ers","es",
				"iance","iances","iant","ing","orical","orically","oricalness",
				"oricalnesses","ories","orise","orised","orises","orising","orist",
				"orists","orization","orizations","orize","orized","orizer","orizers",
				"orizes","orizing","ory","retto","rettos","ro","ros"};
		Character[] winnerInputs2 = {};
		String[] winnerSuffixes2 = {"iant","orical","ro"};
		Character[] loserInputs2 = {'a','e'};
		String[] loserSuffixes2 = {"ation","e","iance","ing","ories","orise",
				"orising","orist","orization","orize","orizing","ory","retto"};
		newState = computerAIService.getNextState(movement);
		assertTrue(
				"New prefix unexpected",
				"allegi".equals(newState.getNewPrefix())
						|| "allego".equals(newState.getNewPrefix())
						|| "allegr".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_MAY_WIN,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes2, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs2, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes2, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs2, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes2, newState
				.getMetrics().getLoserSuffixes().toArray());
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

		GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untw");
		movement.setNewInput('i');
		String[] suffixes = {"ne","ned","nes","ning","st","sted","sting","sts"};
		Character[] winnerInputs = {'n','s'};
		String[] winnerSuffixes = {"ne","ning","st"};
		Character[] loserInputs = {};
		String[] loserSuffixes = {};
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue(
				"New prefix unexpected",
				"untwin".equals(newState.getNewPrefix())
						|| "untwis".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_LOST,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes, newState
				.getMetrics().getLoserSuffixes().toArray());

		// <unt> suffixes <[hread, hreaded, hreading, hreads, hreatening, hrifty,
		// hrone, hroned, hrones, hroning]>
		movement.setOldPrefix("unth");
		movement.setNewInput('r');
		String[] suffixes2 = {"ead","eaded","eading","eads","eatening","ifty",
				"one","oned","ones","oning"};
		Character[] winnerInputs2 = {'i'};
		String[] winnerSuffixes2 = {"eatening","ifty"};
		Character[] loserInputs2 = {'o'};
		String[] loserSuffixes2 = {"ead","one","oning"};
		newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"unthri".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_WILL_LOST,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes2, newState.getSuffixes()
				.toArray());
		assertNotNull("Metrics expected", newState.getMetrics());
		assertArrayEquals("Winner inputs unexpected", winnerInputs2, newState
				.getMetrics().getWinnerInputs().toArray());
		assertArrayEquals("Winner suffixes unexpected", winnerSuffixes2, newState
				.getMetrics().getWinnerSuffixes().toArray());
		assertArrayEquals("Loser inputs unexpected", loserInputs2, newState
				.getMetrics().getLoserInputs().toArray());
		assertArrayEquals("Loser suffixes unexpected", loserSuffixes2, newState
				.getMetrics().getLoserSuffixes().toArray());
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
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());

		GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwis");
		movement.setNewInput('t');
		String[] suffixes = {"","ed","ing","s"};
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"untwist".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_LOST_WORD_COMPLETED,
				newState.getRating());
		assertArrayEquals("Suffixes unexpected", suffixes, newState.getSuffixes()
				.toArray());
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
		computerAIService.setDictionary(dictionaryFileLoaderService
				.loadDictionnary());

		GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwin");
		movement.setNewInput('o');
		GameStateDTO newState = computerAIService.getNextState(movement);
		assertTrue("New prefix unexpected",
				"untwino".equals(newState.getNewPrefix()));
		assertEquals("Rating unexpected", GameRating.PLAYER_LOST_WRONG_PREFIX,
				newState.getRating());
		assertTrue("Suffixes unexpected", newState.getSuffixes().isEmpty());
		assertNull("Metrics unexpected", newState.getMetrics());
	}
}
