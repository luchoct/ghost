package com.luchoct.ghost.controller;

import javax.faces.event.ActionEvent;

import com.luchoct.ghost.model.GhostModel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.luchoct.ghost.dto.GameMovementDTO;
import com.luchoct.ghost.dto.GameRating;
import com.luchoct.ghost.dto.GameStateDTO;
import com.luchoct.ghost.facade.GhostFacade;
import com.luchoct.ghost.service.ai.ComputerAIService;

/**
 * Managed bean which controls the interactions of the front tier.
 *
 * @author Luis
 */
public class GhostBean {

	/**
	 * The logger of the class.
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(ComputerAIService.class);

	/**
	 * The facade of the business tier.
	 */
	@Getter
	@Setter
	private GhostFacade facade;

	/**
	 * The model with the information from the game.
	 */
	@Getter
	@Setter
	private GhostModel model;

	/**
	 * Visibility of the panel of metrics.
	 */
	@Getter
	@Setter
	private boolean showMetrics = false;

	/**
	 * Initialize the information of the game.
	 */
	private void initializeGame() {
		model.setState(new GameStateDTO());
		model.getState().setRating(null);
		model.getState().setNewPrefix("");
		model.setNewInput(null);
		model.setLastInput(null);
	}

	/**
	 * The game starts.
	 *
	 * @return Navigation rule to the page of the game.
	 */
	public final String startGame() {
		initializeGame();
		return "start";
	}

	/**
	 * The first player inputs a letter.
	 *
	 * @param event Event throw from the interface.
	 */
	public final void playerInput(final ActionEvent event) {

		final String newInput = model.getNewInput();
		LOGGER.debug("Player 1 input {}" + newInput);

		if ((newInput != null) && (newInput.length() == 1)) {
			final GameMovementDTO newMovement = new GameMovementDTO();
			newMovement.setNewInput(Character.valueOf(newInput.charAt(0)));
			newMovement.setOldPrefix(model.getState().getNewPrefix());

			final GameStateDTO newState = facade.getNextState(newMovement);
			model.setState(newState);
			if (GameRating.PLAYER_LOST_WORD_COMPLETED.equals(newState.getRating())
					|| GameRating.PLAYER_LOST_WRONG_PREFIX.equals(newState.getRating())) {
				// The second player doesn't move when the first player lost.
				model.setLastInput(null);
			} else {
				// The last input is the last character of the new prefix.
				model.setLastInput(model.getState().getNewPrefix()
						.substring(model.getState().getNewPrefix().length() - 1));
			}
			model.setNewInput(null);
		}
	}

	/**
	 * Returns true if the game has finished.
	 *
	 * @return True if the game has finished.
	 */
	public final boolean isFinished() {
		final GameRating rating = model.getState().getRating();
		return GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT.equals(rating)
				|| GameRating.PLAYER_LOST_WRONG_PREFIX.equals(rating)
				|| GameRating.PLAYER_LOST_WORD_COMPLETED.equals(rating);
	}

	/**
	 * It returns the probability of player 1 of winning the game, if the game hasn´t finished yet.
	 *
	 * @return The probability
	 */
	public final float getProbability() {
		final GameRating rating = model.getState().getRating();
		if (rating == null) {
			//The first movement has not been done yet.
			return 50f;
		} else  if (GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT.equals(rating)) {
			return 100f;
		} else if (GameRating.PLAYER_LOST_WRONG_PREFIX.equals(rating)
				|| GameRating.PLAYER_LOST_WORD_COMPLETED.equals(rating)) {
			return 0f;
		} else {
			return facade.getProbability(model.getState().getMetrics());
		}
	}
}
