package com.luchoct.ghost.facade;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.luchoct.ghost.service.DictionaryLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.dto.MetricsDTO;
import com.luchoct.ghost.service.ComputerAI;
import com.luchoct.ghost.service.ProbabilityCalculator;
import com.luchoct.ghost.test.SpringTest;

@RunWith(MockitoJUnitRunner.class)
public class GhostFacadeImplTest {

	@Mock
	private DictionaryLoader dictionaryLoader;

	@Mock
	private ComputerAI computerAI;

	@Mock
	private ProbabilityCalculator probabilityService;

	@InjectMocks
	private GhostFacadeImpl facade;

	@Test
	public void testGetNextState() {

		//Given
		GameMovementDTO movement = mock(GameMovementDTO.class);
		GameStateDTO state = mock(GameStateDTO.class);

		given(computerAI.getNextState(any(GameMovementDTO.class))).willReturn(state);

		//When
		final GameStateDTO result = facade.getNextState(movement);

		//Verify
		verify(computerAI).getNextState(eq(movement));
		verifyNoMoreInteractions(computerAI);

		assertThat(result, is(state));
	}

	@Test
	public void testProbability() {

		//Given
		MetricsDTO metric = mock(MetricsDTO.class);
		float probability = 0.77f;

		given(probabilityService.getNextPlayerProbability(any(MetricsDTO.class))).willReturn(probability);

		//When
		final float result = facade.getProbability(metric);

		//Verify
		verify(probabilityService).getNextPlayerProbability(eq(metric));
		verifyNoMoreInteractions(probabilityService);

		assertThat(result, equalTo(probability));
	}


}
