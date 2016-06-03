/**
 * 
 */
package com.luchoct.ghost.service.ai;

import java.util.ArrayList;
import java.util.List;

import com.luchoct.ghost.dto.MetricsDTO;


/**
 * It contains the metrics resulting of the movement.
 * 
 * @author Luis
 * 
 */
class ComputerMetrics extends MetricsDTO {

	/**
	 * The inputs that don't assure the victory for any player. It uses a
	 * defensive strategy. First the inputs that take to the next player more
	 * characters to make a word.
	 */
	private List<Character> intermediateInputs = new ArrayList<Character>();

	/**
	 * If all the suffixes have got only one character.
	 */
	private boolean onlyOneCharacterSuffixes = false;

	/**
	 * It returns if the suffixes have got only one character.
	 * 
	 * @return true if the suffixes have got only one character.
	 */
	public final boolean isOnlyOneCharacterSuffixes() {
		return onlyOneCharacterSuffixes;
	}

	/**
	 * It sets if the suffixes have got only one character.
	 * 
	 * @param onlyOneCharacterSuffixes Value to set.
	 */
	public final void setOnlyOneCharacterSuffixes(final boolean onlyOneCharacterSuffixes) {
		this.onlyOneCharacterSuffixes = onlyOneCharacterSuffixes;
	}

	/**
	 * It returns the inputs that don't assure the victory for any player. It
	 * uses a defensive strategy. First the inputs that take to the next player
	 * more characters to make a word.
	 * @return The intermediate inputs.
	 */
	public final List<Character> getIntermediateInputs() {
		return intermediateInputs;
	}
}

