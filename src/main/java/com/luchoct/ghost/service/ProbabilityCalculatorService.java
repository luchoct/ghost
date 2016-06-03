/**
 * 
 */
package com.luchoct.ghost.service;

import org.springframework.stereotype.Service;

import com.luchoct.ghost.dto.MetricsDTO;

/**
 * @author Luis
 *
 */
@Service
public class ProbabilityCalculatorService implements ProbabilityCalculator {

  @Override
  /**
   * {@inheritDoc} 
   */
  public float getNextPlayerProbability(final MetricsDTO metrics) {
    //As the metrics were calculated by the previous player, so a lost for previous player is a win for next player.
    
    // The probability has got 2 partes: one part due to the inputs, another part due to the suffixes. 
    // Each part has got a different weight, and both weight add 100.
    final float weightInputs = getWeightInputs(metrics.getWinnerSuffixes().size() + metrics.getLoserSuffixes().size() - metrics.getWinnerInputs().size() - metrics.getLoserInputs().size());
    //We use the size of the loser inputs, because a lost for previous player is a win for next player
    final float probabilityInputs = getProportionalRatio(metrics.getLoserInputs().size(), metrics.getWinnerInputs().size()) * weightInputs;
    
    final float weightSuffixes = 100f - weightInputs; 
  //We use the size of the loser suffixes, because a lost for previous player is a win for next player
    final float probabilitySuffixes = getProportionalRatio(metrics.getLoserSuffixes().size(), metrics.getWinnerSuffixes().size()) * weightSuffixes;
    
    return probabilityInputs + probabilitySuffixes;
  }

  /**
   * Returns the weight of inputs. It´s between 75 and 100, and when it increases when the number of suffixes decreases.
   * The weight of inputs is 100 when there are not suffixes.
   * The weight of inputs is 75 when there are infinitum suffixes.
   * @param suffixes The number of suffixes.
   * @return The weight of inputs.
   */
  float getWeightInputs(final int suffixes) {
    float weightInputs = 75f;
    int numSuffixes = suffixes;
    weightInputs+= (25f/(numSuffixes + 1));
    return weightInputs;
  }
  
  /**
   * It returns the ratio of the positive cases in the total joint given by the positive and negative  cases. 
   * @param positiveCases The positive cases.
   * @param negativeCases The negative cases.
   * @return The ratio.
   */
  float getProportionalRatio(final int positiveCases, final int negativeCases) {
    return ((float)positiveCases / (positiveCases + negativeCases));
  }
}
