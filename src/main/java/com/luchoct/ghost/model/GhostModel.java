/**
 *
 */
package com.luchoct.ghost.model;

import com.luchoct.ghost.dto.GameStateDTO;
import lombok.Data;

/**
 * @author Luis
 */
@Data
public class GhostModel {

	/**
	 * Current state of the game.
	 */
	private GameStateDTO state;

	/**
	 * Last input of the computer.
	 */
	private String lastInput;

	/**
	 * New input of the user.
	 */
	private String newInput;
}
