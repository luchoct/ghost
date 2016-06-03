/**
 *
 */
package com.luchoct.ghost.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 *         These are the metrics that player 2 calculates.
 */
@Data
public class MetricsDTO implements Serializable {

	private static final long serialVersionUID = -2412025865153006L;

	/**
	 * Winner reachable suffixes for next player (they don't start with other suffixes).
	 */
	@Setter(AccessLevel.NONE)
	private SortedSet<String> winnerSuffixes = new TreeSet<>();

	/**
	 * Loser reachable suffixes for next player (they don't start with other suffixes).
	 */
	@Setter(AccessLevel.NONE)
	private SortedSet<String> loserSuffixes = new TreeSet<>();

	/**
	 * The inputs that make the next player win the game. First the inputs that make the shortest words (the most
	 * suitable for the next player).
	 */
	@Setter(AccessLevel.NONE)
	private List<Character> winnerInputs = new ArrayList<>();

	/**
	 * The inputs that make the player lose the game. First the inputs that make the longest words (the most suitable
	 * for the next player).
	 */
	@Setter(AccessLevel.NONE)
	private List<Character> loserInputs = new ArrayList<>();
}
