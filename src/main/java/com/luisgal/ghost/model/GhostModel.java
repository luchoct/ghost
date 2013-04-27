/**
 * 
 */
package com.luisgal.ghost.model;

import com.luisgal.ghost.dto.GameStateDTO;

/**
 * @author Luis
 *
 */
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

	/**
	 * It returns the current state of the game.
	 * @return The state
	 */
	public GameStateDTO getState() {
		return state;
	}

	/**
	 * It sets the current state of the game.
	 * @param state The state to set
	 */
	public void setState(final GameStateDTO state) {
		this.state = state;
	}

	/**
	 * It returns the last input of the second player.
	 * @return The last input of the second player.
	 */
	public String getLastInput() {
		return lastInput;
	}

	/**
	 * It sets the last input of the second player.
	 * @param lastInput The last input to set
	 */
	public void setLastInput(final String lastInput) {
		this.lastInput = lastInput;
	}

	/**
	 * It returns the new input of the first player.
	 * @return The new input of the first player.
	 */
	public String getNewInput() {
		return newInput;
	}

	/**
	 * It sets the new input of the first player.
	 * @param newInput The new input to set
	 */
	public void setNewInput(final String newInput) {
		this.newInput = newInput;
	}
	
	
}
