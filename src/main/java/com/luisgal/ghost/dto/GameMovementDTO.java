/**
 * 
 */
package com.luisgal.ghost.dto;

/**
 * This class contains the information of a state of the game: the last
 * well-formed prefix and the input of the current movement.
 * @author Luis
 * 
 */
public class GameMovementDTO {

	/**
	 * Prefix of word played until new input.
	 */
	private String oldPrefix;

	/**
	 * New input of the current movement.
	 */
	private Character newInput;

	/**
	 * It returns the prefix made until current movement.
	 * @return the prefix
	 */
	public final String getOldPrefix() {
		return oldPrefix;
	}

	/**
	 * It sets the prefix made until current movement.
	 * @param oldPrefix the prefix to set
	 */
	public final void setOldPrefix(final String oldPrefix) {
		this.oldPrefix = oldPrefix;
	}

	/**
	 * It returns the input of the player, corresponding this movement.
	 * @return the newInput
	 */
	public final Character getNewInput() {
		return newInput;
	}

	/**
	 * It sets the new input of the player, corresponding this movement.
	 * @param newInput the newInput to set
	 */
	public final void setNewInput(final Character newInput) {
		this.newInput = newInput;
	}

}
