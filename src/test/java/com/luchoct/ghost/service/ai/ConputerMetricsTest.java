/**
 *
 */
package com.luchoct.ghost.service.ai;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Luis
 */
public class ConputerMetricsTest {

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testMetrics() {
		// I don't use Spring because is a class with package scope. 
		ComputerMetrics dictionaryMetrics = new ComputerMetrics();

		assertThat("Unexpected default value",
				dictionaryMetrics.getWinnerSuffixes() != null && dictionaryMetrics.getWinnerSuffixes().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				dictionaryMetrics.getLoserSuffixes() != null && dictionaryMetrics.getLoserSuffixes().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				dictionaryMetrics.getLoserInputs() != null && dictionaryMetrics.getLoserInputs().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				dictionaryMetrics.getWinnerInputs() != null && dictionaryMetrics.getWinnerInputs().isEmpty(),
				equalTo(true));
	}
}
