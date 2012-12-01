package com.luisgal.ghost.service;

import java.util.SortedSet;
import java.util.TreeMap;

import com.luisgal.ghost.dto.GameMovementDTO;
import com.luisgal.ghost.dto.GameStateDTO;

/**
 * The computer AI for the player 2.
 * @author Luis
 *
 */
public interface IComputerAI {
	
	/**
	 * It sets the dictionary.
	 * @param dictionary The dictionary to set.
	 */
	void setDictionary(TreeMap<String, SortedSet<String>> dictionary); 

	/**
	 * Taking the dictionary and the current movement, it returns the next state 
	 * of the game.
	 * @param currentMovement The information about the current movement. It
	 * contains the last well-made prefix  and the current input of the player.
	 * The current movement must an odd movement (the first, the third, etc.).
	 * @return The new state of the game, with the rating of the current movement,
	 * and the new prefix resulting of a new movement of the computer AI, if
	 * the game is not ended.
	 */
	GameStateDTO getNextState(final GameMovementDTO currentMovement);
}
