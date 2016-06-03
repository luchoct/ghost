/**
 *
 */
package com.luchoct.ghost.service.ai;

import java.util.ArrayList;
import java.util.List;

import com.luchoct.ghost.dto.MetricsDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


/**
 * It contains the metrics resulting of the movement.
 *
 * @author Luis
 */
@Data
class ComputerMetrics extends MetricsDTO {

	/**
	 * The inputs that don't assure the victory for any player. It uses a defensive strategy. First the inputs that take
	 * to the next player more characters to make a word.
	 */
	@Setter(AccessLevel.NONE)
	private List<Character> intermediateInputs = new ArrayList<Character>();

	/**
	 * If all the suffixes have got only one character.
	 */
	private boolean onlyOneCharacterSuffixes = false;
}

