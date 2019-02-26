/**
 *
 */
package com.luchoct.ghost.service.ai;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameRating;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.service.DictionaryFileLoaderService;
import com.luchoct.ghost.test.SpringTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Luis
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/ApplicationContext.xml"})
@DirtiesContext
public class ComputerAITest extends SpringTest {

	@Configuration
	static class Config {
		@Autowired
		/**
		 * Service required to load the dictionary.
		 */
		private DictionaryFileLoaderService dictionaryFileLoaderService;

		@Bean
		@Qualifier("initializedComputerAIService")
		public ComputerAIService getInitializedComputerAIService() {
			final ComputerAIService service = new ComputerAIService();
			service.setDictionary(dictionaryFileLoaderService.loadDictionary());
			return service;
		}
	}

	@Autowired
	@Qualifier("initializedComputerAIService")
	/**
	 * Service to test
	 */
	private ComputerAIService computerAIService;

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testService() {
		assertThat("service not initialised", computerAIService, notNullValue());
	}

	private void assertMetrics(final String[] expectedWinnerSuffixes, final String[] expectedLoserSuffixes,
							   final Character[] expectedWinnerInputs, final Character[] expectedLoserInputs,
							   final GameStateDTO newState) {
		assertThat("Metrics expected", newState.getMetrics(), notNullValue());
		assertThat("Winner inputs unexpected", newState.getMetrics().getWinnerInputs(),
				containsInAnyOrder(expectedWinnerInputs));
		assertThat("Winner suffixes unexpected", newState.getMetrics().getWinnerSuffixes(),
				containsInAnyOrder(expectedWinnerSuffixes));
		assertThat("Loser inputs unexpected", newState.getMetrics().getLoserInputs(),
				containsInAnyOrder(expectedLoserInputs));
		assertThat("Loser suffixes unexpected", newState.getMetrics().getLoserSuffixes(),
				containsInAnyOrder(expectedLoserSuffixes));
	}

	@Test
	/**
	 * It tests the rating of the input of the first player
	 */
	public void testPlayerWin() {
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
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("untagged"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes));
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);
	}

	@Test
	/**
	 * It tests if the computer discover that the first player is going to lose.
	 */
	public void testPlayerWillProbablyWin() {

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
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("untidi"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_WILL_PROBABLY_WIN));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes));
		assertMetrics(winnerSuffixes, loserSuffixes, winnerInputs, loserInputs, newState);
	}

	@Test
	/**
	 * It tests a game state where both players can win.
	 */
	public void testPlayerMayWin() {

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
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("untime"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_MAY_WIN));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes));
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
		assertThat(
				"New prefix unexpected",
				newState.getNewPrefix(),
				anyOf(equalTo("allegi"), equalTo("allego"), equalTo("allegr")));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_MAY_WIN));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes2));
		assertMetrics(winnerSuffixes2, loserSuffixes2, winnerInputs2, loserInputs2, newState);
	}

	@Test
	/**
	 * It tests if the computer discover that the first player is going to lose.
	 */
	public void testPlayerWillLost() {

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
		assertThat(
				"New prefix unexpected",
				newState.getNewPrefix(),
				anyOf(equalTo("untwin"), equalTo("untwis")));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_WILL_LOST));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes));
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
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("unthri"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_WILL_LOST));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes2));
		assertMetrics(winnerSuffixes2, loserSuffixes2, winnerInputs2, loserInputs2, newState);
	}

	@Test
	/**
	 * It tests a whole word of the first player
	 */
	public void testPlayerLostWholeWords() {

		final GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwis");
		movement.setNewInput('t');
		final String[] suffixes = {"", "ed", "ing", "s"};
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("untwist"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_LOST_WORD_COMPLETED));
		assertThat("Suffixes unexpected", newState.getSuffixes(), containsInAnyOrder(suffixes));
		assertThat("Metrics unexpected", newState.getMetrics(), nullValue());
	}

	@Test
	/**
	 * It tests a wrong prefix of the first player
	 */
	public void testPlayerLostWrongPreffix() {

		final GameMovementDTO movement = new GameMovementDTO();

		// <untwi> suffixes <[ne, ned, nes, ning, st, sted, sting, sts]>
		movement.setOldPrefix("untwin");
		movement.setNewInput('o');
		final GameStateDTO newState = computerAIService.getNextState(movement);
		assertThat("New prefix unexpected", newState.getNewPrefix(), equalTo("untwino"));
		assertThat("Rating unexpected", newState.getRating(), equalTo(GameRating.PLAYER_LOST_WRONG_PREFIX));
		assertThat("Suffixes unexpected", newState.getSuffixes(), empty());
		assertThat("Metrics unexpected", newState.getMetrics(), nullValue());
	}
}
