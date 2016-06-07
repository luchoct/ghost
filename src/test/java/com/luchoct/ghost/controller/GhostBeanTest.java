package com.luchoct.ghost.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import javax.faces.event.ActionEvent;

import com.luchoct.ghost.model.GhostModel;
import lombok.Data;
import lombok.NonNull;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameRating;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.facade.GhostFacade;

public class GhostBeanTest {
	@Data
	private class MovementMatcher extends ArgumentMatcher<GameMovementDTO> {

		@NonNull
		private final String prefix;
		@NonNull
		private final Character input;

		@Override
		public boolean matches(final Object argument) {
			if (argument instanceof GameMovementDTO) {
				final GameMovementDTO movement = (GameMovementDTO) argument;
				return prefix.equals(movement.getOldPrefix()) && input.equals(movement.getNewInput());
			} else {
				return false;
			}
		}
	}

	@Test
	public void testStartGame() {

		final GhostBean aBean = new GhostBean();
		final GhostModel model = mock(GhostModel.class);

		// Given
		aBean.setModel(model);

		willCallRealMethod().given(model).setState(any(GameStateDTO.class));
		willCallRealMethod().given(model).getState();

		// Then
		final String navigationRule = aBean.startGame();

		// Verify
		verify(model).setState(any(GameStateDTO.class));
		// The navigation rule is the start page.
		assertThat("wrong navigation rule", navigationRule, equalTo("start"));
		// The method creates a new state with an empty prefix and empty suffixes.
		assertThat(aBean.getModel().getState(), notNullValue());
		assertThat(aBean.getModel().getState().getMetrics(), nullValue());
		assertThat(aBean.getModel().getState().getNewPrefix(), isEmptyString());
		assertThat(aBean.getModel().getState().getRating(), nullValue());
		assertThat(aBean.getModel().getState().getSuffixes(), empty());

		verify(model).setLastInput(isNull(String.class));
		verify(model).setNewInput(isNull(String.class));
		verify(model, atLeast(1)).getState();
		verifyNoMoreInteractions(model);
	}

	private void testPlayerInput(final GameRating rating) {
		final GhostBean aBean = new GhostBean();
		final GhostModel model = mock(GhostModel.class);

		final GhostFacade facade = mock(GhostFacade.class);

		// Given
		aBean.setFacade(facade);
		aBean.setModel(model);

		final String oldPrefix = "k";

		final GameStateDTO state = new GameStateDTO();
		state.setNewPrefix(oldPrefix);

		given(model.getState()).willReturn(state);

		final Character newInput = Character.valueOf('k');
		given(model.getNewInput()).willReturn(newInput.toString());

		final GameStateDTO newState = new GameStateDTO();
		newState.setNewPrefix(oldPrefix.concat(newInput.toString()));
		newState.setRating(rating);

		given(facade.getNextState(any(GameMovementDTO.class))).willReturn(newState);

		final ActionEvent event = mock(ActionEvent.class);

		// Then
		aBean.playerInput(event);

		// verify
		verify(facade).getNextState(argThat(new MovementMatcher(oldPrefix, newInput)));

		verify(model).setState(eq(newState));
		if (GameRating.PLAYER_LOST_WORD_COMPLETED.equals(rating)
				|| GameRating.PLAYER_LOST_WRONG_PREFIX.equals(rating)) {
			verify(model).setLastInput(isNull(String.class));
		} else {
			verify(model).setLastInput(eq(newInput.toString()));
		}
		verify(model).setNewInput(isNull(String.class));

		verifyNoMoreInteractions(event);
	}

	@Test
	public void testPlayerInputThatCompletesAWord() {
		testPlayerInput(GameRating.PLAYER_LOST_WORD_COMPLETED);
	}

	@Test
	public void testPlayerInputThatCreatesWrongPrefix() {
		testPlayerInput(GameRating.PLAYER_LOST_WRONG_PREFIX);
	}

	@Test
	public void testPlayerInputThatGivesPossibilitiesToWin() {
		testPlayerInput(GameRating.PLAYER_MAY_WIN);
	}

	@Test
	public void testPlayerInputThatWillMakeHimLost() {
		testPlayerInput(GameRating.PLAYER_WILL_LOST);

	}

	@Test
	public void testPlayerInputThatWillGiveAGoodChanceToWin() {
		testPlayerInput(GameRating.PLAYER_WILL_PROBABLY_WIN);
	}

	@Test
	public void testPlayerInputThatMakeHimWin() {
		testPlayerInput(GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT);
	}
}
