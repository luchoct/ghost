/**
 * 
 */
package com.luisgal.ghost.facade;

import java.util.SortedSet;
import java.util.TreeMap;

import com.luisgal.ghost.dto.GameMovementDTO;
import com.luisgal.ghost.dto.GameStateDTO;
import com.luisgal.ghost.service.IComputerAI;
import com.luisgal.ghost.service.IDictionaryLoader;

/**
 * @author Luis
 * 
 */
public class GhostFacade implements IGhostFacade {

  /**
   * The service that emulate the second player.
   */
  private IComputerAI computerAI;

  /**
   * It creates a facade, and loads the dictionary into the computerAI. Only odd
   * length prefixes are used as keys because the dictionary is used for the
   * player 2.
   * @param dictionaryLoader service to load the dictionary.
   * @param computerAI service that emulates the second player.
   */
  public GhostFacade(final IDictionaryLoader dictionaryLoader,
      final IComputerAI computerAI) {
    if (dictionaryLoader == null) {
      throw new IllegalArgumentException("The dictionary loader can't be null");
    } else if (computerAI == null) {
      throw new IllegalArgumentException("The computer AI can't be null");
    }
    this.computerAI = computerAI;
    TreeMap<String, SortedSet<String>> dictionary = dictionaryLoader
        .loadDictionnary();
    computerAI.setDictionary(dictionary);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final GameStateDTO getNextState(final GameMovementDTO currentMovement) {
    return computerAI.getNextState(currentMovement);
  }

}
