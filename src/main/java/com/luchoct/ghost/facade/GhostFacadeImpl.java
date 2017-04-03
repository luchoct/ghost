/**
 *
 */
package com.luchoct.ghost.facade;

import java.util.SortedSet;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.dto.MetricsDTO;
import com.luchoct.ghost.service.ComputerAI;
import com.luchoct.ghost.service.DictionaryLoader;
import com.luchoct.ghost.service.ProbabilityCalculator;

/**
 * @author Luis
 */
@Component
public class GhostFacadeImpl implements GhostFacade {

	/**
	 * The class that loads the dictionary.
	 */
	private DictionaryLoader dictionaryLoader;

	/**
	 * The service that emulate the second player.
	 */
	private ComputerAI computerAI;

	private ProbabilityCalculator probabilityService;

	/**
	 * It creates a facade, and loads the dictionary into the computerAI. Only odd length prefixes are used as keys
	 * because the dictionary is used for the player 2.
	 *
	 * @param dictionaryLoader service to load the dictionary.
	 * @param computerAI service to play the computer player.
	 * @param probabilityService service to calculate probability to win a game.
	 */
	@Autowired
	public GhostFacadeImpl( final DictionaryLoader dictionaryLoader, @Qualifier("computerAI") ComputerAI computerAI, ProbabilityCalculator probabilityService) {
		if (dictionaryLoader == null) {
			throw new IllegalArgumentException("The dictionary loader can't be null");
		} else {
			this.dictionaryLoader = dictionaryLoader;
		}
		this.computerAI = computerAI;
		this.probabilityService = probabilityService;
	}

	@PostConstruct
	private void loadDictionary() {
		final TreeMap<String, SortedSet<String>> dictionary = dictionaryLoader.loadDictionnary();
		computerAI.setDictionary(dictionary);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GameStateDTO getNextState(final GameMovementDTO currentMovement) {
		return computerAI.getNextState(currentMovement);
	}

	@Override
	public final float getProbability(final MetricsDTO metrics) {
		return probabilityService.getNextPlayerProbability(metrics);
	}
}
