package com.luchoct.ghost.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.SortedSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.luchoct.ghost.dto.MetricsDTO;

@RunWith(MockitoJUnitRunner.class)
public class ProbabilityCalculatorImplTest {

	@Mock
	/**
	 * Service to test
	 */
	private ProbabilityCalculatorService probabilityService;

	@Test
	/**
	 * It tests the initialization of the service
	 */
	public void testService() {
		assertNotNull("service not initialised", probabilityService);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetNextPlayerProbability() {
		// Given
		reset(probabilityService);
		final MetricsDTO metrics = mock(MetricsDTO.class);

		final List<Character> winnerInputs = (List<Character>) mock(List.class);
		given(winnerInputs.size()).willReturn(6);
		given(metrics.getWinnerInputs()).willReturn(winnerInputs);

		final List<Character> loserInputs = (List<Character>) mock(List.class);
		given(loserInputs.size()).willReturn(2);
		given(metrics.getLoserInputs()).willReturn(loserInputs);

		final SortedSet<String> winnerSuffixes = (SortedSet<String>) mock(SortedSet.class);
		given(winnerSuffixes.size()).willReturn(90);
		given(metrics.getWinnerSuffixes()).willReturn(winnerSuffixes);

		final SortedSet<String> loserSuffixes = (SortedSet<String>) mock(SortedSet.class);
		given(loserSuffixes.size()).willReturn(10);
		given(metrics.getLoserSuffixes()).willReturn(loserSuffixes);

		final float weightInputs = 75f;
		given(probabilityService.getWeightInputs(anyInt())).willReturn(weightInputs);
		final float ratioInputs = 0.25f;
		given(probabilityService.getProportionalRatio(eq(2), eq(6))).willReturn(ratioInputs);

		final float ratioSuffixes = 0.1f;
		given(probabilityService.getProportionalRatio(eq(10), eq(90))).willReturn(ratioSuffixes);

		given(probabilityService.getNextPlayerProbability((MetricsDTO) any())).willCallRealMethod();

		try {
			// When
			float probability = probabilityService.getNextPlayerProbability(metrics);

			// Then
			verify(probabilityService).getNextPlayerProbability(eq(metrics));
			verify(probabilityService).getWeightInputs(eq(10 + 90 - 2 - 6));
			verify(probabilityService).getProportionalRatio(eq(2), eq(6));
			verify(probabilityService).getProportionalRatio(eq(10), eq(90));
			verifyNoMoreInteractions(probabilityService);
			assertThat(probability, equalTo(ratioInputs * weightInputs + ratioSuffixes * (100 - weightInputs)));
		} finally {
			given(probabilityService.getWeightInputs(anyInt())).willCallRealMethod();
			given(probabilityService.getProportionalRatio(anyInt(), anyInt())).willCallRealMethod();
		}
	}

	@Test
	public void testGetWeightInputs() {
		//Given
		reset(probabilityService);

		given(probabilityService.getWeightInputs(anyInt())).willCallRealMethod();

		final double delta = 0.1;
		final float weight0 = probabilityService.getWeightInputs(0);
		assertThat(Double.valueOf(weight0), closeTo(100f, delta));
		final float weight1 = probabilityService.getWeightInputs(1);
		final float weight5 = probabilityService.getWeightInputs(5);
		final float weight10 = probabilityService.getWeightInputs(10);
		final float weight50 = probabilityService.getWeightInputs(50);
		final float weight100 = probabilityService.getWeightInputs(100);
		assertThat(weight0, greaterThan(weight1));
		assertThat(weight1, greaterThan(weight5));
		assertThat(weight5, greaterThan(weight10));
		assertThat(weight10, greaterThan(weight50));
		assertThat(weight50, greaterThan(weight100));
		assertThat(weight100, greaterThan(75f));
	}

	@Test
	public void testGetProportionalRatio() {

		//Given
		reset(probabilityService);

		given(probabilityService.getProportionalRatio(anyInt(), anyInt())).willCallRealMethod();

		final double delta = 0.1;
		try {
			//When and Test
			assertEquals(0f, probabilityService.getProportionalRatio(0, 1), delta);
			assertEquals(0f, probabilityService.getProportionalRatio(0, 5), delta);
			assertEquals(1f, probabilityService.getProportionalRatio(1, 0), delta);
			assertEquals(1f, probabilityService.getProportionalRatio(5, 0), delta);
			assertEquals(0.5f, probabilityService.getProportionalRatio(1, 1), delta);
			assertEquals(0.5f, probabilityService.getProportionalRatio(5, 5), delta);
			assertEquals(0.25f, probabilityService.getProportionalRatio(1, 4), delta);
		} finally {
			given(probabilityService.getProportionalRatio(anyInt(), anyInt())).willCallRealMethod();
		}

	}
}
