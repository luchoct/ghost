/**
 * 
 */
package com.luchoct.ghost.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Luis
 *
 */
public class NumbersUtilsTest {
	
	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testEven() {
		assertTrue("Espected even", NumberUtils.isEven(2));
		assertFalse("Espected odd", NumberUtils.isEven(5));
	}

	

}
