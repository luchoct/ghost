/**
 *
 */
package com.luchoct.ghost.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Luis
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/ApplicationContext.xml"})
@Disabled
public abstract class SpringTest {
}
