package com.luchoct.ghost.service.ai;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameRating;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.dto.MetricsDTO;
import com.luchoct.ghost.service.ComputerAI;
import com.luchoct.ghost.util.ComparatorUtils;
import com.luchoct.ghost.util.NumberUtils;
import com.luchoct.ghost.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Luis
 */
@Service
@Qualifier("computerAI")
public class ComputerAIService implements ComputerAI {

	/**
	 * The logger of the class.
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(ComputerAIService.class);

	/**
	 * Structure with the contents of the dictionary. The keys are prefixes (with odd length) and the values are the
	 * suffixes that produce a word with the corresponding prefix, in alphabetical order.
	 */
	@Getter
	@Setter
	private TreeMap<String, SortedSet<String>> dictionary;

	/**
	 * {@inheritDoc}
	 */
	public final GameStateDTO getNextState(final GameMovementDTO currentMovement) {

		validateMovement(currentMovement);

		final GameStateDTO newState = new GameStateDTO();
		// It contains an empty set.
		assert newState.getSuffixes() != null;

		LOGGER.debug("Calculing next state from prefix <{}> with input <{}>",
				currentMovement.getOldPrefix(), currentMovement.getNewInput());

		// The new prefix will be the joint of the ancient prefix and the input.
		final String newPrefix = new StringBuilder(currentMovement.getOldPrefix())
				.append(currentMovement.getNewInput()).toString();

		SortedSet<String> suffixes = dictionary.get(newPrefix);
		if (suffixes == null) {
			// The newPrefix is wrong. There are no words in the dictionary with that prefix.
			newState.setRating(GameRating.PLAYER_LOST_WRONG_PREFIX);
			// The computer doesn't add a new character and the new prefix contain the wrong prefix.
			newState.setNewPrefix(newPrefix);
			// There are not possible suffixes.
		} else if (suffixes.contains("")) {
			// The newPrefix is a whole word.
			newState.setRating(GameRating.PLAYER_LOST_WORD_COMPLETED);
			// The computer doesn't add a new character and the new prefix contain the whole word.
			newState.setNewPrefix(newPrefix);
			// There are not possible suffixes.
		} else {
			/*
			There are only words with one letter more than the new prefix, so the computer is going to lost. If there
			are only words with an odd length, it doesn't assure that the computer is going to lost, because the player
			can make a wrong prefix after next character of the computer.
			 */
			final ComputerMetrics internalMetrics = calculeDictionaryMetrics(suffixes);
			if (internalMetrics.isOnlyOneCharacterSuffixes()) {
				// The computer loses because he can only make words or make a wrong prefix.
				newState.setRating(GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT);
				// The computer doesn't add a new character and the new prefix contain the prefix made by the user.
				assert internalMetrics.getWinnerInputs().isEmpty();
				assert internalMetrics.getWinnerSuffixes().isEmpty();

				newState.setNewPrefix(newPrefix.concat(internalMetrics.getLoserInputs().get(0).toString()));
			} else if (!internalMetrics.getWinnerInputs().isEmpty()) {
				newState.setRating(GameRating.PLAYER_WILL_LOST);
				/*
				If the computer thinks it will win, it should play randomly among all its winning moves. The first
				winner input is the shortest way to win.
				 */
				int ramdonIndex = new Random(new Date().getTime()).nextInt(internalMetrics.getWinnerInputs().size());
				newState.setNewPrefix(newPrefix.concat(internalMetrics.getWinnerInputs().get(ramdonIndex).toString()));
			} else if (!internalMetrics.getIntermediateInputs().isEmpty()) {
				newState.setRating(GameRating.PLAYER_MAY_WIN);
				// The computer use the defensive strategy to choose the first of the intermediate inputs.
				newState.setNewPrefix(newPrefix.concat(internalMetrics.getIntermediateInputs().get(0).toString()));
			} else {
				assert !internalMetrics.getLoserInputs().isEmpty();
				newState.setRating(GameRating.PLAYER_WILL_PROBABLY_WIN);
				/*
				If the computer thinks it will lose, it should play so as to extend the game as long as possible
				(choosing randomly among choices that force the maximal game length). The first loser input is the
				longest way to lose.
				 */
				newState.setNewPrefix(newPrefix.concat(internalMetrics.getLoserInputs().get(0).toString()));
			}
			final MetricsDTO metrics = new MetricsDTO();
			metrics.getWinnerInputs().addAll(internalMetrics.getWinnerInputs());
			metrics.getLoserInputs().addAll(internalMetrics.getLoserInputs());
			metrics.getWinnerSuffixes().addAll(internalMetrics.getWinnerSuffixes());
			metrics.getLoserSuffixes().addAll(internalMetrics.getLoserSuffixes());
			newState.setMetrics(metrics);
		}
		if (suffixes != null) {
			// It saves the available suffixes in the dictionary before the new prefix.
			newState.getSuffixes().addAll(suffixes);
		}
		LOGGER.debug("New state rating <{}>, new prefix <{}>, suffixes <{}>",
				newState.getRating(), newState.getNewPrefix(), newState.getSuffixes());
		assert newState.getNewPrefix() != null;
		return newState;
	}

	private void validateMovement(final GameMovementDTO currentMovement) {
		if (currentMovement.getOldPrefix() != null
				&& !NumberUtils.isEven(currentMovement.getOldPrefix().length())) {
			throw new IllegalArgumentException(
					"The current movement must be an odd movement so the prefix must have an odd length.");
		} else if (currentMovement.getNewInput() == null) {
			throw new IllegalArgumentException(
					"The current movement must contain an input.");
		}
	}

	/**
	 * It returns different metrics about the suffixes, to been used in the algorithm of next movement.
	 *
	 * @param suffixes The possible suffix from the dictionary.
	 * @return The metrics.
	 */
	private ComputerMetrics calculeDictionaryMetrics(final SortedSet<String> suffixes) {
		// This method doesn't need to be called if suffixes contain ""
		assert !suffixes.contains("");

		final ComputerMetrics metrics = new ComputerMetrics();

		// The number of even and odd suffixes is well initialised.
		assert metrics.getWinnerSuffixes() != null && metrics.getWinnerSuffixes().isEmpty();
		assert metrics.getLoserSuffixes() != null && metrics.getLoserSuffixes().isEmpty();

		/*
		Not all the suffixes are reachable, because a word can be prefixed by another word, for instance: 'abduce',
		'abduced'. Only the reachable suffixes are useful to decide the next movement (we'll never build the word
		'abduced').
		 */
		final SortedSet<String> reachableSuffixes = getReachableSuffixes(suffixes);
		LOGGER.trace("Reachable suffixes {}", reachableSuffixes);

		/*
		The player 2 lost if there are only one character reachable suffixes. He's going to build a word, or he's going
		to build a wrong prefix. In addition, depending on the length of each reachable suffixe, it can be a winner
		suffix or a loser suffix for the player 2.
		 */
		metrics.setOnlyOneCharacterSuffixes(true);
		reachableSuffixes.forEach(currentSuffix -> {
			final int lengthSuffix = currentSuffix.length();
			if (lengthSuffix > 1) {
				metrics.setOnlyOneCharacterSuffixes(false);
			}
			if (NumberUtils.isEven(lengthSuffix)) {
				metrics.getWinnerSuffixes().add(currentSuffix);
			} else {
				metrics.getLoserSuffixes().add(currentSuffix);
			}
		});

		assert metrics.getWinnerInputs() != null && metrics.getWinnerInputs().isEmpty();
		assert metrics.getIntermediateInputs() != null && metrics.getIntermediateInputs().isEmpty();
		assert metrics.getLoserInputs() != null && metrics.getLoserInputs().isEmpty();

		/*
		Winner inputs are characters than can only build winner suffixes. If player 2 uses one of that character, he's
		going to win (if he doesn't build a wrong prefix).
		 */
		metrics.getWinnerInputs().addAll(getWinnerInputs(metrics.getWinnerSuffixes(), metrics.getLoserSuffixes()));
		/*
		Winner inputs are characters than can only build loser suffixes. If player 2 uses one of that character, he's
		going to loose (if player 1 doesn't build a wrong prefix).
		 */
		metrics.getLoserInputs().addAll(getLoserInputs(metrics.getWinnerSuffixes(), metrics.getLoserSuffixes()));
		metrics.getIntermediateInputs().addAll(
				getIntermediateInputs(metrics.getWinnerSuffixes(), metrics.getLoserSuffixes()));

		LOGGER.debug("metrics of suffixes obtained: only 1 character <{}>, even reachable <{}>, odd reachable <{}>, "
						+ "winner inputs <{}>, loser inputs <{}>, intermediate inputs <{}>",
				metrics.isOnlyOneCharacterSuffixes(), metrics.getWinnerSuffixes(), metrics.getLoserSuffixes(),
				metrics.getWinnerInputs(), metrics.getLoserInputs(), metrics.getIntermediateInputs());
		return metrics;
	}

	/**
	 * It returns the reachable suffixes (they don't start with other suffixes) among all the suffixes.
	 *
	 * @param suffixes Suffixes to analyze.
	 * @return The reachable suffixes.
	 */
	private SortedSet<String> getReachableSuffixes(final SortedSet<String> suffixes) {
		return suffixes.stream()
				.filter(currentSuffix -> {
					final SortedSet<String> otherSuffixes = new TreeSet<String>(suffixes);
					otherSuffixes.remove(currentSuffix);
					// otherSuffixes is a copy of suffixes without the current suffix.
					return !StringUtils.containsSomePrefix(currentSuffix, otherSuffixes);
				}).collect(Collectors.toCollection(() -> new TreeSet<>()));
	}

	/**
	 * It returns the inputs that make the next player win the game. First the inputs that make the shortest words (the
	 * more suitable for the next player).
	 *
	 * @param winnerSuffixes The winner reachable suffixes.
	 * @param loserSuffixes  The loser reachable suffixes.
	 * @return The inputs that make the player win the game.
	 */
	private List<Character> getWinnerInputs(final Collection<String> winnerSuffixes,
											final Collection<String> loserSuffixes) {

		// If a letter makes several suffixes, it only takes into account the shortest one.
		return winnerSuffixes.stream()
				.sorted(ComparatorUtils.FIRST_SHORTER_COMPARATOR)
				.map(winnerSuffix -> winnerSuffix.charAt(0))
				.distinct()
				.filter(input -> !StringUtils.containsPrefix(loserSuffixes, input.toString()))
				.collect(Collectors.toList());
	}

	/**
	 * It returns the inputs that make the player lose the game. First the input that make the longest words (the most
	 * suitable for the next player).
	 *
	 * @param winnerSuffixes The winner reachable suffixes.
	 * @param loserSuffixes  The loser reachable suffixes.
	 * @return The inputs that make the player lose the game.
	 */
	private List<Character> getLoserInputs(final Collection<String> winnerSuffixes,
										   final Collection<String> loserSuffixes) {
		final List<Character> losers = loserSuffixes.stream()
				.sorted(ComparatorUtils.FIRST_SHORTER_COMPARATOR)
				// If a letter makes several suffixes, it only takes into account the shortest one.
				.map(loserSuffix -> loserSuffix.charAt(0))
				.distinct()
				.filter(input -> !StringUtils.containsPrefix(winnerSuffixes, input.toString()))
				.collect(Collectors.toList());
		// We reverse the inputs to get the right order.
		Collections.reverse(losers);
		return losers;
	}

	/**
	 * It returns the inputs that don't assure the victory for any player. It uses a defensive strategy. First the
	 * inputs that take to the next player more characters to make a word.
	 *
	 * @param winnerSuffixes The winner reachable suffixes.
	 * @param loserSuffixes  The loser reachable suffixes.
	 * @return The non-definitive inputs in order.
	 */
	private List<Character> getIntermediateInputs(final Collection<String> winnerSuffixes,
												  final Collection<String> loserSuffixes) {

		final List<Character> intermediate = loserSuffixes.stream()
				.sorted(ComparatorUtils.FIRST_SHORTER_COMPARATOR)
				// If a letter makes several suffixes, it only takes into account the shortest one.
				.map(loserSuffix -> loserSuffix.charAt(0))
				.distinct()
				// It's an intermediate character as it's a prefix of both lists of suffix, even list and odd list.
				.filter(input -> StringUtils.containsPrefix(winnerSuffixes, input.toString()))
				.collect(Collectors.toList());
		// We reverse the inputs to get the right order.
		Collections.reverse(intermediate);
		return intermediate;
	}
}
