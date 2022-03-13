package vrampal.mastermind;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Board {

  private static final boolean SAFETY_CHECK = true; // Disable for maximum performance

  public final int gameLength; // Number of turns before game end

  public final int pinCount; // Number of pins in the secret

  public final int maxVal; // Pin values in 0..(MAX_VAL-1)

  public final int[][] guesses;

  public final Hint[] hints;

  private int currentTurnIdx = 0;

  private int[] secret = null;

  Board(int gameLength, int pinCount, int maxVal) {
    this.gameLength = gameLength;
    this.pinCount = pinCount;
    this.maxVal = maxVal;
    guesses = new int[gameLength][];
    hints = new Hint[gameLength];
  }

  public long countPossibleGuess() {
    return (long) Math.pow(maxVal, pinCount);
  }

  public int[] long2Guess(long value) {
    if (SAFETY_CHECK && ((value < 0) || (value >= countPossibleGuess()))) {
      throw new MastermindException("Invalid value");
    }
    int[] guess = new int[pinCount];
    for (int pinIdx = 0; pinIdx < pinCount; pinIdx++) {
      guess[pinIdx] = (int) (value % maxVal);
      value = value / maxVal;
    }
    return guess;
  }

  boolean validGuess(int[] guess) {
    if ((guess == null) || (guess.length != pinCount)) {
      return false;
    }
    for (int pinIdx = 0; pinIdx < pinCount; pinIdx++) {
      if ((guess[pinIdx] < 0) || (guess[pinIdx] >= maxVal)) {
        return false;
      }
    }
    return true;
  }

  public void recordSecret(int[] secret) {
    if (SAFETY_CHECK && !validGuess(secret)) {
      throw new MastermindException("Invalid secret");
    }
    if (SAFETY_CHECK && (this.secret != null)) {
      throw new MastermindException("Secret already set");
    }
    this.secret = secret;
    log.info("CodeMaker choose {}", secret);
  }

  public void recordGuess(int turnIdx, int[] guess) {
    if (SAFETY_CHECK && (turnIdx != currentTurnIdx)) {
      throw new MastermindException("Invalid turnIdx");
    }
    if (SAFETY_CHECK && !validGuess(guess)) {
      throw new MastermindException("Invalid guess");
    }
    if (SAFETY_CHECK && (this.secret == null)) {
      throw new MastermindException("Secret has not been set yet");
    }
    guesses[turnIdx] = guess;
    log.info("CodeBreaker proposed {}", guess);
    currentTurnIdx++;
  }

  boolean checkGuess(int turnIdx) {
    int[] guess = guesses[turnIdx];
    Hint hint = new Hint(secret, guess);
    hints[turnIdx] = hint;
    log.info("Get {}", hint);
    return hint.isVictory();
  }

}
