/**
 *
 */
package com.luchoct.ghost.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Luis
 */
public class NumbersUtilsTest {

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testEven() {
		assertThat("Espected even", NumberUtils.isEven(2), equalTo(true));
		assertThat("Espected odd", NumberUtils.isEven(5), equalTo(false));
	}


}
