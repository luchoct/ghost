/**
 * 
 */
package com.luisgal.ghost.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 * These are the metrics that player 2 calculates.
 */
public class MetricsDTO implements Serializable {

	private static final long serialVersionUID = -2412025865153006L;

  /**
	 * Loser reachable suffixes for next player (they don't start with other suffixes).
	 */
	private SortedSet<String> loserSuffixes = new TreeSet<String>();

	/**
	 * Winner reachable suffixes for next player (they don't start with other suffixes).
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
	public SortedSet<String> getLoserSuffixes() {
		return loserSuffixes;
	}

	/**
	 * It returns the winner reachable suffixes (they don't start with other
	 * suffixes).
	 * 
	 * @return The winner reachable suffixes.
	 */
	public SortedSet<String> getWinnerSuffixes() {
		return winnerSuffixes;
	}

	/**
	 * It returns the inputs that make the next player win the game. First the
	 * inputs that make the shortest words (the more suitable for the next
	 * player).
	 * @return The winner inputs.
	 */
	public List<Character> getWinnerInputs() {
		return winnerInputs;
	}

	/**
	 * It returns the inputs that make the player lose the game. First the
	 * inputs that make the longest words (the more suitable for the next
	 * player).
	 * @return The loser inputs.
	 */
	public List<Character> getLoserInputs() {
		return loserInputs;
	}
}
