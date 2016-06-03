/**
 * 
 */
package com.luchoct.ghost.dto;

/**
 * @author Luis
 *
 */
public enum GameRating {
  
  /**
   * The player wins: there are only word in the dictionary with one letter more
   * than the prefix made. The new prefix contains the word made by the computer.
   * Metrics are provided.
   */
  PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT (-2, "game.state.code.p1w"),

  /**
   * The player has got a lot of probabilities to win (he will win if he don't
   * make a wrong prefix). The new prefix contains the movement of the computer.
   * Metrics are provided.
   */
  PLAYER_WILL_PROBABLY_WIN (-1, "game.state.code.p1wpw"),

  /**
   * The player may win: there are inputs that give the victory to him. The new
   * prefix contains the movement of the computer. Metrics are provided.
   */
  PLAYER_MAY_WIN (0, "game.state.code.p1mw"),

  /**
   * The player will lost if the other player knows the dictionary (there is no
   * possibility to win if the other player knows the dictionary). The new
   * prefix contains the movement of the computer. Metrics are provided.
   */
  PLAYER_WILL_LOST (1, "game.state.code.p1wl"),
  /**
   * The player has made an wrong prefix. The new prefix contains the wrong
   * prefix made by the player. No metrics are provided.
   */
  PLAYER_LOST_WRONG_PREFIX (2, "game.state.code.p1lwp"),
  /**
   * The player has completed a word. The new prefix contains the whole word
   * made by the player. No metrics are provided.
   */
  PLAYER_LOST_WORD_COMPLETED (3 ,"game.state.code.p1lwc");
  
  private final int code;
  
  private final String messageKey;
  
  private GameRating (final int code, final String messageKey) {
    this.code = code;
    this.messageKey = messageKey;
  }
  
  public static final GameRating valueOfCode (final int code) {
    for (GameRating value : values()) {
      if (value.code == code) {
        return value;
      }
    }
    throw new IllegalArgumentException("The given code is not a GameRating code");
  }
  
  /**
   * @return the code
   */
  public int getCode() {
    return code;
  }

  /**
   * @return the messageKey
   */
  public String getMessageKey() {
    return messageKey;
  }
}

