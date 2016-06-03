/**
 *
 */
package com.luchoct.ghost.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Luis
 */
@Data
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
	@Setter(AccessLevel.NONE)
	private SortedSet<String> suffixes = new TreeSet<String>();

	/**
	 * MetricsDTO of the last movement.
	 */
	private MetricsDTO metrics;
}
