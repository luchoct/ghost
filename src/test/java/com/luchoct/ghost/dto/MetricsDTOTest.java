/**
 * 
 */
package com.luchoct.ghost.dto;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Luis
 *
 */
public class MetricsDTOTest {

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testMetrics() {
		// I don't use Spring because is a class with package scope. 
		MetricsDTO metrics = new MetricsDTO();
		
		assertTrue("Unexpected default value", (metrics.getWinnerSuffixes() != null) && metrics.getWinnerSuffixes().isEmpty());
		assertTrue("Unexpected default value", (metrics.getLoserInputs() != null) && metrics.getLoserInputs().isEmpty());
		assertTrue("Unexpected default value", (metrics.getLoserSuffixes() != null) && metrics.getLoserSuffixes().isEmpty());
		assertTrue("Unexpected default value", (metrics.getWinnerInputs() != null) && metrics.getWinnerInputs().isEmpty());
	}
}
