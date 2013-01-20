/**
 * 
 */
package com.luisgal.ghost.service;

import com.luisgal.ghost.dto.MetricsDTO;

/**
 * @author Luis
 * Interface of all probability calculators
 */
public interface ProbabilityCalculator {
  /**
   * It returns the probability of player 1 of winning the game.
   * @param metrics The metrics of the last movement, calculated by player 2.
   * @return The probability.
   */
  float getNextPlayerProbability(MetricsDTO metrics);
}
