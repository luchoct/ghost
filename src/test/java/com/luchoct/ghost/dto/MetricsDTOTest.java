/**
 *
 */
package com.luchoct.ghost.dto;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Luis
 */
public class MetricsDTOTest {

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testMetrics() {
		// I don't use Spring because is a class with package scope. 
		MetricsDTO metrics = new MetricsDTO();

		assertThat("Unexpected default value",
				metrics.getWinnerSuffixes() != null && metrics.getWinnerSuffixes().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				metrics.getLoserInputs() != null && metrics.getLoserInputs().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				metrics.getLoserSuffixes() != null && metrics.getLoserSuffixes().isEmpty(),
				equalTo(true));
		assertThat("Unexpected default value",
				metrics.getWinnerInputs() != null && metrics.getWinnerInputs().isEmpty(),
				equalTo(true));
	}
}
