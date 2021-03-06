package com.luchoct.ghost.facade;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.dto.MetricsDTO;
import com.luchoct.ghost.service.ComputerAI;
import com.luchoct.ghost.service.DictionaryLoader;
import com.luchoct.ghost.service.ProbabilityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
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
