/**
 *
 */
package com.luchoct.ghost.dto;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Luis
 */
public enum GameRating {

	/**
	 * The player wins: there are only word in the dictionary with one letter more than the prefix made. The new prefix
	 * contains the word made by the computer. Metrics are provided.
	 */
	PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT(-2, "game.state.code.p1w"),

	/**
	 * The player has got a lot of probabilities to win (he will win if he don't make a wrong prefix). The new prefix
	 * contains the movement of the computer. Metrics are provided.
	 */
	PLAYER_WILL_PROBABLY_WIN(-1, "game.state.code.p1wpw"),

	/**
	 * The player may win: there are inputs that give the victory to him. The new prefix contains the movement of the
	 * computer. Metrics are provided.
	 */
	PLAYER_MAY_WIN(0, "game.state.code.p1mw"),

	/**
	 * The player will lost if the other player knows the dictionary (there is no chance to win if the other player
	 * knows the dictionary). The new prefix contains the movement of the computer. Metrics are provided.
	 */
	PLAYER_WILL_LOST(1, "game.state.code.p1wl"),
	/**
	 * The player has made an wrong prefix. The new prefix contains the wrong prefix made by the player. No metrics are
	 * provided.
	 */
	PLAYER_LOST_WRONG_PREFIX(2, "game.state.code.p1lwp"),
	/**
	 * The player has completed a word. The new prefix contains the whole word made by the player. No metrics are
	 * provided.
	 */
	PLAYER_LOST_WORD_COMPLETED(3, "game.state.code.p1lwc");

	/**
	 * The code of current game rating.
	 */
	@Getter
	private final int code;

	/**
	 * The key of the message of current game rating.
	 */
	@Getter
	private final String messageKey;

	GameRating(final int code, final String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public static final GameRating valueOfCode(final int code) {
		final Optional<GameRating> optionalValue = Stream.of(values()).filter(value -> value.code == code).findFirst();
		if (optionalValue.isPresent()) {
			return optionalValue.get();
		} else {
			throw new IllegalArgumentException("The given code is not a GameRating code");
		}
	}
}

