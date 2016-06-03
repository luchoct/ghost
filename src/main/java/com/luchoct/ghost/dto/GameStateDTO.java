/**
 * 
 */
package com.luchoct.ghost.dto;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 * 
 */
public class GameStateDTO {
  
	/**
	 * Rating of the previous movement.
	 */
	private GameRating rating;

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
	public String getNewPrefix() {
		return newPrefix;
	}

	/**
	 * It sets the new prefix made.
	 * @param newPrefix the prefix to set
	 */
	public void setNewPrefix(final String newPrefix) {
		this.newPrefix = newPrefix;
	}

	/**
	 * It returns the rating of the previous movement.
	 * @return the rating
	 */
	public GameRating getRating() {
		return rating;
	}

	/**
	 * It sets the rating of the previous movement.
	 * @param rating the rating to set
	 */
	public void setRating(final GameRating rating) {
		this.rating = rating;
	}

	/**
	 * It returns the possible suffixes analyzed for the second player.
	 * @return The possible suffixes.
	 */
	public SortedSet<String> getSuffixes() {
		return suffixes;
	}

	/**
	 * It returns the metrics of the last movement.
	 * @return the metrics
	 */
	public MetricsDTO getMetrics() {
		return metrics;
	}

	/**
	 * It sets the metrics of the last movement.
	 * @param metrics The metrics to set
	 */
	public void setMetrics(final MetricsDTO metrics) {
		this.metrics = metrics;
	}
}
