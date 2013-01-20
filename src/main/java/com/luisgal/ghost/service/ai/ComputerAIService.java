package com.luisgal.ghost.service.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.luisgal.ghost.dto.GameMovementDTO;
import com.luisgal.ghost.dto.GameRating;
import com.luisgal.ghost.dto.GameStateDTO;
import com.luisgal.ghost.dto.MetricsDTO;
import com.luisgal.ghost.service.ComputerAI;
import com.luisgal.ghost.util.ComparatorUtils;
import com.luisgal.ghost.util.NumberUtils;
import com.luisgal.ghost.util.StringUtils;

/**
 * 
 * @author Luis
 * 
 */
@Service
@Qualifier("computerAI")
public class ComputerAIService implements ComputerAI {

  /**
   * The logger of the class.
   */
  static final Logger LOGGER = Logger.getLogger(ComputerAIService.class);

  /**
   * Structure with the contents of the dictionary. The keys are prefixes (with
   * odd length) and the values are the suffixes that produce a word with the
   * corresponding prefix, in alphabetical order.
   */
  private TreeMap<String, SortedSet<String>> dictionary;

  /**
   * It returns the structure with the contents of the dictionary.
   * 
   * @return the structure with the dictionary
   */
  public final TreeMap<String, SortedSet<String>> getDictionary() {
    return dictionary;
  }

  /**
   * It sets the structure with the contents of the dictionary.
   * 
   * @param dictionary the structure with the dictionary
   */
  public final void setDictionary(
      final TreeMap<String, SortedSet<String>> dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * {@inheritDoc}
   */
  public final GameStateDTO getNextState(final GameMovementDTO currentMovement) {

    if ((currentMovement.getOldPrefix() != null)
        && !NumberUtils.isEven(currentMovement.getOldPrefix().length())) {
      throw new IllegalArgumentException(
          "The current movement must be an odd movement so the prefix must have an odd length.");
    } else if (currentMovement.getNewInput() == null) {
      throw new IllegalArgumentException(
          "The current movement must contain an input.");
    }

    final GameStateDTO newState = new GameStateDTO();
    // It contains an empty set.
    assert (newState.getSuffixes() != null);

    if (LOGGER.isDebugEnabled()) {
      final StringBuilder sbMessage = new StringBuilder(100);
      sbMessage.append("Calculing next state from prefix <");
      sbMessage.append(currentMovement.getOldPrefix());
      sbMessage.append("> with input <");
      sbMessage.append(currentMovement.getNewInput());
      LOGGER.debug(sbMessage.toString());
    }

    // The new prefix will be the joint of the ancient prefix and the input.
    final String newPrefix = (new StringBuilder(currentMovement.getOldPrefix()))
        .append(currentMovement.getNewInput()).toString();

    SortedSet<String> suffixes = dictionary.get(newPrefix);
    if (suffixes == null) {
      /*
       * The newPrefix is wrong. There are no words in the dictionary with that
       * prefix.
       */
      newState.setRating(GameRating.PLAYER_LOST_WRONG_PREFIX);
      /*
       * The computer doesn't add a new character and the new prefix contain the
       * wrong prefix.
       */
      newState.setNewPrefix(newPrefix);
      // There are not possible suffixes.
    } else if (suffixes.contains("")) {
      /*
       * The newPrefix is a whole word.
       */
      newState.setRating(GameRating.PLAYER_LOST_WORD_COMPLETED);
      /*
       * The computer doesn't add a new character and the new prefix contain the
       * whole word.
       */
      newState.setNewPrefix(newPrefix);
      // There are not possible suffixes.
    } else {
      /*
       * There are only words with one letter more than the new prefix, so the
       * computer is going to lost. If there are only words with an odd length,
       * it doesn't assure that the computer is going to lost, because the
       * player can make a wrong prefix after next character of the computer.
       */
      final ComputerMetrics internalMetrics = calculeDictionaryMetrics(suffixes);
      if (internalMetrics.isOnlyOneCharacterSuffixes()) {
        /*
         * The computer loses because he can only make words or make a wrong
         * prefix.
         */
        newState.setRating(GameRating.PLAYER_WINS_ONLY_ONE_CHARACTER_LEFT);
        /*
         * The computer doesn't add a new character and the new prefix contain
         * the prefix made by the user.
         */
        assert (internalMetrics.getWinnerInputs().isEmpty());
        assert (internalMetrics.getWinnerSuffixes().isEmpty());

        newState.setNewPrefix(newPrefix.concat(internalMetrics.getLoserInputs()
            .get(0).toString()));
      } else if (!internalMetrics.getWinnerInputs().isEmpty()) {
        // The computer will win.
        newState.setRating(GameRating.PLAYER_WILL_LOST);
        /*
         * If the computer thinks it will win, it should play randomly among all
         * its winning moves. The first winner input is the shortest way to win.
         */
        int ramdonIndex = (new Random(new Date().getTime()))
            .nextInt(internalMetrics.getWinnerInputs().size());
        newState.setNewPrefix(newPrefix.concat(internalMetrics
            .getWinnerInputs().get(ramdonIndex).toString()));
      } else if (!internalMetrics.getIntermediateInputs().isEmpty()) {
        // The computer may win.
        newState.setRating(GameRating.PLAYER_MAY_WIN);
        /*
         * The computer use the defensive strategy to choose the first of the
         * intermediate inputs.
         */
        newState.setNewPrefix(newPrefix.concat(internalMetrics
            .getIntermediateInputs().get(0).toString()));
      } else {
        assert (!internalMetrics.getLoserInputs().isEmpty());
        // The computer will probably win.
        newState.setRating(GameRating.PLAYER_WILL_PROBABLY_WIN);
        /*
         * If the computer thinks it will lose, it should play so as to extend
         * the game as long as possible (choosing randomly among choices that
         * force the maximal game length). The first loser input is the longest
         * way to lose.
         */
        newState.setNewPrefix(newPrefix.concat(internalMetrics.getLoserInputs()
            .get(0).toString()));
      }
      final MetricsDTO metrics = new MetricsDTO();
      metrics.getWinnerInputs().addAll(internalMetrics.getWinnerInputs());
      metrics.getLoserInputs().addAll(internalMetrics.getLoserInputs());
      metrics.getWinnerSuffixes().addAll(internalMetrics.getWinnerSuffixes());
      metrics.getLoserSuffixes().addAll(internalMetrics.getLoserSuffixes());
      newState.setMetrics(metrics);
    }
    if (suffixes != null) {
      /*
       * It saves the available suffixes in the dictionary before the new
       * prefix.
       */
      newState.getSuffixes().addAll(suffixes);
    }
    if (LOGGER.isDebugEnabled()) {
      final StringBuilder sbMessage = new StringBuilder(100);
      sbMessage.append("New state rating <");
      sbMessage.append(newState.getRating());
      sbMessage.append(">, new prefix <");
      sbMessage.append(newState.getNewPrefix());
      sbMessage.append(">, suffixes <");
      sbMessage.append(newState.getSuffixes());
      sbMessage.append(">");
      LOGGER.debug(sbMessage.toString());
    }
    assert (newState.getNewPrefix() != null);
    return newState;
  }

  /**
   * It returns different metrics about the suffixes, to been used in the
   * algorithm of next movement.
   * @param suffixes The possible suffix from the dictionary.
   * @return The metrics.
   */
  private ComputerMetrics calculeDictionaryMetrics(
      final SortedSet<String> suffixes) {
    // This method doesn't need to be called if suffixes contain ""
    assert (!suffixes.contains(""));

    final ComputerMetrics metrics = new ComputerMetrics();

    // The number of even and odd suffixes is well initialised.
    assert ((metrics.getWinnerSuffixes() != null) && metrics
        .getWinnerSuffixes().isEmpty());
    assert ((metrics.getLoserSuffixes() != null) && metrics.getLoserSuffixes()
        .isEmpty());

    /*
     * Not all the suffixes are reachable, because a word can be prefixed by
     * another word, for instance: 'abduce', 'abduced'. Only the reachable
     * suffixes are useful to decide the next movement (we'll never build the
     * word 'abduced').
     */
    final SortedSet<String> reachableSuffixes = getReachableSuffixes(suffixes);
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("Reachable suffixes " + reachableSuffixes);
    }

    /*
     * The player 2 lost if there are only one character reachable suffixes.
     * He's going to build a word, or he's going to build a wrong prefix. In
     * addition, depending on the length of each reachable suffixe, it can be a
     * winner suffix or a loser suffix for the player 2.
     */
    metrics.setOnlyOneCharacterSuffixes(true);
    for (String currentSuffix : reachableSuffixes) {
      final int lengthSuffix = currentSuffix.length();
      if (lengthSuffix > 1) {
        metrics.setOnlyOneCharacterSuffixes(false);
      }
      if (NumberUtils.isEven(lengthSuffix)) {
        metrics.getWinnerSuffixes().add(currentSuffix);
      } else {
        metrics.getLoserSuffixes().add(currentSuffix);
      }
    }

    assert ((metrics.getWinnerInputs() != null) && metrics.getWinnerInputs()
        .isEmpty());
    assert ((metrics.getIntermediateInputs() != null) && metrics
        .getIntermediateInputs().isEmpty());
    assert ((metrics.getLoserInputs() != null) && metrics.getLoserInputs()
        .isEmpty());

    /*
     * Winner inputs are characters than can only build winner suffixes. If
     * player 2 uses one of that character, he's going to win (if he doesn't
     * build a wrong prefix).
     */
    metrics.getWinnerInputs()
        .addAll(
            getWinnerInputs(metrics.getWinnerSuffixes(),
                metrics.getLoserSuffixes()));
    /*
     * Winner inputs are characters than can only build loser suffixes. If
     * player 2 uses one of that character, he's going to loose (if player 1
     * doesn't build a wrong prefix).
     */
    metrics.getLoserInputs()
        .addAll(
            getLoserInputs(metrics.getWinnerSuffixes(),
                metrics.getLoserSuffixes()));
    metrics.getIntermediateInputs().addAll(
        getIntermediateInputs(metrics.getWinnerSuffixes(),
            metrics.getLoserSuffixes()));

    if (LOGGER.isDebugEnabled()) {
      final StringBuilder sbMessage = new StringBuilder(200);
      sbMessage.append("metrics of suffixes obtained: only 1 character <");
      sbMessage.append(metrics.isOnlyOneCharacterSuffixes());
      sbMessage.append(">, even reachable <");
      sbMessage.append(metrics.getWinnerSuffixes());
      sbMessage.append(">, odd reachable <");
      sbMessage.append(metrics.getLoserSuffixes());
      sbMessage.append(">, winner inputs <");
      sbMessage.append(metrics.getWinnerInputs());
      sbMessage.append(">, loser inputs <");
      sbMessage.append(metrics.getLoserInputs());
      sbMessage.append(">, intermediate inputs <");
      sbMessage.append(metrics.getIntermediateInputs());
      sbMessage.append(">");
      LOGGER.debug(sbMessage.toString());
    }
    return metrics;
  }

  /**
   * It returns the reachable suffixes (they don't start with other suffixes)
   * among all the suffixes.
   * @param suffixes Suffixes to analyze.
   * @return The reachable suffixes.
   */
  private SortedSet<String> getReachableSuffixes(
      final SortedSet<String> suffixes) {
    final SortedSet<String> reachableSuffixes = new TreeSet<String>();
    final Iterator<String> itSuffixes = suffixes.iterator();
    while (itSuffixes.hasNext()) {
      final String currentSuffix = itSuffixes.next();
      final SortedSet<String> otherSuffixes = new TreeSet<String>(suffixes);
      otherSuffixes.remove(currentSuffix);
      // otherSuffixes is a copy of suffixes without the current suffix.
      if (!StringUtils.containsSomePrefix(currentSuffix, otherSuffixes)) {
        reachableSuffixes.add(currentSuffix);
      }
    }
    return reachableSuffixes;
  }

  /**
   * It returns the inputs that make the next player win the game. First the
   * inputs that make the shortest words (the more suitable for the next
   * player).
   * @param winnerSuffixes The winner reachable suffixes.
   * @param loserSuffixes The loser reachable suffixes.
   * @return The inputs that make the player win the game.
   */
  private List<Character> getWinnerInputs(
      final Collection<String> winnerSuffixes,
      final Collection<String> loserSuffixes) {

    final List<Character> winners = new ArrayList<Character>();
    final List<String> lengthWinnerSuffixes = new LinkedList<String>(
        winnerSuffixes);
    Collections.sort(lengthWinnerSuffixes,
        ComparatorUtils.FIRST_SHORTER_COMPARATOR);
    /*
     * If a letter makes several suffixes, it only takes into account the
     * shortest one.
     */
    for (String winnerSuffix : lengthWinnerSuffixes) {
      final Character input = Character.valueOf(winnerSuffix.charAt(0));
      if (!StringUtils.containsPrefix(loserSuffixes, input.toString())
          && (!winners.contains(input))) {
        /*
         * The letter is winner because you can't make words in your turn.
         */
        winners.add(input);
      }
    }
    return winners;
  }

  /**
   * It returns the inputs that make the player lose the game. First the inputs
   * that make the longest words (the more suitable for the next player).
   * @param winnerSuffixes The winner reachable suffixes.
   * @param loserSuffixes The loser reachable suffixes.
   * @return The inputs that make the player lose the game.
   */
  private List<Character> getLoserInputs(
      final Collection<String> winnerSuffixes,
      final Collection<String> loserSuffixes) {
    final List<Character> losers = new ArrayList<Character>();
    final List<String> lengthLoserSuffixes = new LinkedList<String>(
        loserSuffixes);
    Collections.sort(lengthLoserSuffixes,
        ComparatorUtils.FIRST_SHORTER_COMPARATOR);
    /*
     * If a letter makes several suffixes, it only takes into account the
     * shortest one.
     */
    for (String loserSuffix : lengthLoserSuffixes) {
      final Character input = Character.valueOf(loserSuffix.charAt(0));
      if (!StringUtils.containsPrefix(winnerSuffixes, input.toString())
          && (!losers.contains(input))) {
        /*
         * The letter is loser because the other player can't make words in his
         * turn.
         */
        losers.add(input);
      }
    }
    // We reverse the inputs to get the right order.
    Collections.reverse(losers);
    return losers;
  }

  /**
   * It returns the inputs that don't assure the victory for any player. It uses
   * a defensive strategy. First the inputs that take to the next player more
   * characters to make a word.
   * @param winnerSuffixes The winner reachable suffixes.
   * @param loserSuffixes The loser reachable suffixes.
   * @return The non-definitive inputs in order.
   */
  private List<Character> getIntermediateInputs(
      final Collection<String> winnerSuffixes,
      final Collection<String> loserSuffixes) {

    final List<Character> intermediate = new ArrayList<Character>();
    final List<String> lengthLoserSuffixes = new LinkedList<String>(
        loserSuffixes);
    Collections.sort(lengthLoserSuffixes,
        ComparatorUtils.FIRST_SHORTER_COMPARATOR);
    /*
     * If a letter makes several suffixes, it only takes into account the
     * shortest one.
     */
    for (String loserSuffix : lengthLoserSuffixes) {
      final Character input = Character.valueOf(loserSuffix.charAt(0));
      if ((StringUtils.containsPrefix(winnerSuffixes, input.toString()))
          && (!intermediate.contains(input))) {
        /*
         * The letter intermediate because is a prefix of both lists of suffix,
         * the even list and the odd list.
         */
        intermediate.add(input);
      }
    }
    // We reverse the inputs to get the right order.
    Collections.reverse(intermediate);
    return intermediate;
  }
}
