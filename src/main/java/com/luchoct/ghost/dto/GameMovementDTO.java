/**
 *
 */
package com.luchoct.ghost.dto;

import lombok.Data;

/**
 * This class contains the information of a state of the game: the last well-formed prefix and the input of the current
 * movement.
 *
 * @author Luis
 */
@Data
public class GameMovementDTO {

	/**
	 * Prefix of word played until new input.
	 */
	private String oldPrefix;

	/**
	 * New input of the current movement.
	 */
	private Character newInput;
}
