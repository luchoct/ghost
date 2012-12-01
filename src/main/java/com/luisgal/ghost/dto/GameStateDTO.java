/**
 * 
 */
package com.luisgal.ghost.dto;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 * 
 */
public class GameStateDTO {

	/**
	 * The player wins: there are only word in the dictionary with one letter more
	 * than the prefix made. The new prefix contains the word made by the computer.
	 * Metrics are provided.
	 */
	public static final Integer PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT = Integer
			.valueOf(-2);

	/**
	 * The player has got a lot of probabilities to win (he will win if he don't
	 * make a wrong prefix). The new prefix contains the movement of the computer.
	 * Metrics are provided.
	 */
	public static final Integer PLAYER_WILL_PROBABLY_WIN = Integer
			.valueOf(-1);

	/**
	 * The player may win: there are inputs that give the victory to him. The new
	 * prefix contains the movement of the computer. Metrics are provided.
	 */
	public static final Integer PLAYER_MAY_WIN = Integer.valueOf(0);

	/**
	 * The player will lost if the other player knows the dictionary (there is no
	 * possibility to win if the other player knows the dictionary). The new
	 * prefix contains the movement of the computer. Metrics are provided.
	 */
	public static final Integer PLAYER_WILL_LOST = Integer
			.valueOf(1);
	/**
	 * The player has made an wrong prefix. The new prefix contains the wrong
	 * prefix made by the player. No metrics are provided.
	 */
	public static final Integer PLAYER_LOST_WRONG_PREFIX = Integer
			.valueOf(2);
	/**
	 * The player has completed a word. The new prefix contains the whole word
	 * made by the player. No metrics are provided.
	 */
	public static final Integer PLAYER_LOST_WORD_COMPLETED = Integer
			.valueOf(3);

	/**
	 * Rating of the previous movement.
	 */
	private Integer rating;

	/**
	 * New prefix made.
	 */
	private String newPrefix;

	/**
	 * Possible suffixes analyzed for the second player.
	 */
	private SortedSet<String> suffixes = new TreeSet<String>();

	/**
	 * MetricsDTO of the last movement.
	 */
	private MetricsDTO metrics;

	/**
	 * It returns new prefix made.
	 * @return the prefix
	 */
	public final String getNewPrefix() {
		return newPrefix;
	}

	/**
	 * It sets the new prefix made.
	 * @param newPrefix the prefix to set
	 */
	public final void setNewPrefix(final String newPrefix) {
		this.newPrefix = newPrefix;
	}

	/**
	 * It returns the rating of the previous movement.
	 * @return the rating
	 * @see GameStateDTO.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT
	 * @see GameStateDTO.PLAYER_WILL_LOST
	 * @see GameStateDTO.PLAYER_LOST_WRONG_PREFIX
	 * @see GameStateDTO.PLAYER_LOST_WORD_COMPLETED
	 */
	public final Integer getRating() {
		return rating;
	}

	/**
	 * It sets the rating of the previous movement.
	 * @param rating the rating to set
	 */
	public final void setRating(final Integer rating) {
		this.rating = rating;
	}

	/**
	 * It returns the possible suffixes analyzed for the second player.
	 * @return The possible suffixes.
	 */
	public final SortedSet<String> getSuffixes() {
		return suffixes;
	}

	/**
	 * It returns the metrics of the last movement.
	 * @return the metrics
	 */
	public final MetricsDTO getMetrics() {
		return metrics;
	}

	/**
	 * It sets the metrics of the last movement.
	 * @param metrics The metrics to set
	 */
	public final void setMetrics(final MetricsDTO metrics) {
		this.metrics = metrics;
	}
}
