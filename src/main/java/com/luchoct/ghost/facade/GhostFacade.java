/**
 *
 */
package com.luchoct.ghost.facade;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.dto.MetricsDTO;

/**
 * @author Luis
 */
public interface GhostFacade {

	/**
	 * Taking the dictionary and the current movement, it returns the next state of the game.
	 *
	 * @param currentMovement The information about the current movement. It contains the last well-made prefix  and the
	 *                        current input of the player. The current movement must be an odd movement (the first, the
	 *                        third, etc.).
	 * @return The new state of the game, with the rating of the current movement, and the new prefix resulting of a new
	 * movement of the computer AI, if the game is not ended.
	 */
	GameStateDTO getNextState(GameMovementDTO currentMovement);

	/**
	 * Returns the probability of winning of player 1.
	 *
	 * @param metrics The metrics of the last movement.
	 * @return The probability of winning of player 1.
	 */
	float getProbability(MetricsDTO metrics);
}
