package com.luisgal.ghost.controller;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.luisgal.ghost.dto.GameMovementDTO;
import com.luisgal.ghost.dto.GameRating;
import com.luisgal.ghost.dto.GameStateDTO;
import com.luisgal.ghost.dto.MetricsDTO;
import com.luisgal.ghost.facade.GhostFacade;
import com.luisgal.ghost.model.GhostModel;
import com.luisgal.ghost.service.ai.ComputerAIService;

/**
 * Managed bean which controls the interactions of the front tier.
 * @author Luis
 */
public class GhostBean {
	
	/**
	 * The logger of the class.
	 */
	static final Logger LOGGER = Logger.getLogger(ComputerAIService.class);

	/**
	 * The facade of the business tier.
	 */
	private GhostFacade facade;

	/**
	 * The model with the information from the game.
	 */
	private GhostModel model;

	/**
	 * Visibility of the panel of metrics.
	 */
	private boolean showMetrics = false;
	
	/**
	 * It returns the facade of the business tier.
	 * @return The facade of the business tier.
	 */
	public final GhostFacade getFacade() {
		return facade;
	}

	/**
	 * It sets the facade of the business tier.
	 * @param newFacade The facade of the business tier to set.
	 */
	public final void setFacade(final GhostFacade newFacade) {
		this.facade = newFacade;
	}

	/**
	 * It returns the model.
	 * @return The model.
	 */
	public final GhostModel getModel() {
		return model;
	}

	/**
	 * It sets a new model.
	 * @param newModel The model to set
	 */
	public final void setModel(final GhostModel newModel) {
		this.model = newModel;
	}

  /**
   * It returns whether the panel of metrics is shown.
   * @return true if the panel of metrics is shown.
   */
  public final boolean isShowMetrics() {
    return showMetrics;
  }

  /**
   * It sets whether the panel of metrics is shown.
   * @param showMetrics The new visibility of the panel of metrics.
   */
  public final void setShowMetrics(final boolean showMetrics) {
    this.showMetrics = showMetrics;
  }
		
	
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
	 * @return Navigation rule to the page of the game.
	 */
	public final String startGame() {
		initializeGame();
		return "start";
	}

	/**
	 * The first player inputs a letter.
	 * @param event Event throw from the interface.
	 */
	public final void playerInput(final ActionEvent event) {
		
		final String newInput = (String) model.getNewInput();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Player 1 input " + newInput);
		}
		
		assert ((newInput != null) && (newInput.length() == 1));
		final GameMovementDTO newMovement = new GameMovementDTO();
		newMovement.setNewInput(Character.valueOf(newInput.charAt(0)));
		newMovement.setOldPrefix(model.getState().getNewPrefix());

		final GameStateDTO newState = facade.getNextState(newMovement);
		model.setState(newState);
		if (!GameRating.PLAYER_LOST_WORD_COMPLETED.equals(newState.getRating())
				&& !GameRating.PLAYER_LOST_WRONG_PREFIX.equals(newState.getRating())) {
			// The last input is the last character of the new prefix.
			model.setLastInput(model.getState().getNewPrefix()
					.substring(model.getState().getNewPrefix().length() - 1));
		} else {
			// The second player doesn't move when the first player lost.
			model.setLastInput(null);
		}
		model.setNewInput(null);
	}
	
	/**
	 * Returns true if the game has finished.
	 * @return True if the game has finished.
	 */
	public boolean isFinished() {
	  final GameRating rating = model.getState().getRating();
	  return (GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT.equals(rating) 
	      || GameRating.PLAYER_LOST_WRONG_PREFIX.equals(rating) 
	      || GameRating.PLAYER_LOST_WORD_COMPLETED.equals(rating));
	}
	
	/**
	 * It returns the probability of player 1 of winning the game, if the game hasn´t finished yet.
	 * @return The probability
	 */
	public float getProbability() {
	  final MetricsDTO metrics = model.getState().getMetrics();
	  
	  if (metrics == null) {
	    //The first movement has not been done yet.
	    return 50f;
	  } else {
	    final GameRating rating = model.getState().getRating();
	    if (GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT.equals(rating)) {
	      return 100f;
	    } else if (GameRating.PLAYER_LOST_WRONG_PREFIX.equals(rating) 
        || GameRating.PLAYER_LOST_WORD_COMPLETED.equals(rating)) {
	      return 0f;
	    } else {
	      return facade.getProbability(model.getState().getMetrics());
	    }
	  }
	}
}
