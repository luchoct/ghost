/**
 * 
 */
package com.luisgal.ghost.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Luis
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/ApplicationContext.xml"})
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@Ignore
public abstract class BaseServiceTest {
}
