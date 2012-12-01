/**
 * 
 */
package com.luisgal.ghost.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 *
 */
public class MetricsDTO {

	/**
	 * Loser reachable suffixes (they don't start with other suffixes).
	 */
	private SortedSet<String> loserSuffixes = new TreeSet<String>();

	/**
	 * Winner reachable suffixes (they don't start with other suffixes).
	 */
	private SortedSet<String> winnerSuffixes = new TreeSet<String>();

	/**
	 * The inputs that make the next player win the game. First the inputs that
	 * make the shortest words (the more suitable for the next player).
	 */
	private List<Character> winnerInputs = new ArrayList<Character>();

	/**
	 * The inputs that make the player lose the game. First the inputs that make
	 * the longest words (the more suitable for the next player).
	 */
	private List<Character> loserInputs = new ArrayList<Character>();

	/**
	 * It returns the loser reachable suffixes (they don't start with other
	 * suffixes).
	 * 
	 * @return The loser reachable suffixes.
	 */
	public final SortedSet<String> getLoserSuffixes() {
		return loserSuffixes;
	}

	/**
	 * It returns the winner reachable suffixes (they don't start with other
	 * suffixes).
	 * 
	 * @return The winner reachable suffixes.
	 */
	public final SortedSet<String> getWinnerSuffixes() {
		return winnerSuffixes;
	}

	/**
	 * It returns the inputs that make the next player win the game. First the
	 * inputs that make the shortest words (the more suitable for the next
	 * player).
	 * @return The winner inputs.
	 */
	public final List<Character> getWinnerInputs() {
		return winnerInputs;
	}

	/**
	 * It returns the inputs that make the player lose the game. First the
	 * inputs that make the longest words (the more suitable for the next
	 * player).
	 * @return The loser inputs.
	 */
	public final List<Character> getLoserInputs() {
		return loserInputs;
	}


}
