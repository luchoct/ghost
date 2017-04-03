/**
 *
 */
package com.luchoct.ghost.service.ai;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

		assertTrue("Unexpected default value",
				dictionaryMetrics.getWinnerSuffixes() != null && dictionaryMetrics.getWinnerSuffixes().isEmpty());
		assertTrue("Unexpected default value",
				dictionaryMetrics.getLoserSuffixes() != null && dictionaryMetrics.getLoserSuffixes().isEmpty());
		assertTrue("Unexpected default value",
				dictionaryMetrics.getLoserInputs() != null && dictionaryMetrics.getLoserInputs().isEmpty());
		assertTrue("Unexpected default value",
				dictionaryMetrics.getWinnerInputs() != null && dictionaryMetrics.getWinnerInputs().isEmpty());
	}
}
