package vrampal.mastermind;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Board {
  
  public final int gameLength; // Number of turns before game end
  
  public final int pinCount; // Number of pins in the secret
  
  public final int maxVal; // Pin values in 0..(MAX_VAL-1)
  
  public final int[][] guesses;
  
  public final Hint[] hints;
  
  private int[] secret;
  
  Board(int gameLength, int pinCount, int maxVal) {
    this.gameLength = gameLength;
    this.pinCount = pinCount;
    this.maxVal = maxVal;
    guesses = new int[gameLength][];
    hints = new Hint[gameLength];
  }
  
  public void recordSecret(int[] secret) {
    this.secret = secret;
    log.info("CodeMaker choose {}", secret);
  }
  
  public void recordGuess(int turnIdx, int[] guess) {
    guesses[turnIdx] = guess;
    log.info("CodeBreaker play {}", guess);
  }
  
  boolean checkGuess(int turnIdx) {
    int[] guess = guesses[turnIdx];
    Hint hint = new Hint(secret, guess);
    hints[turnIdx] = hint;
    log.info("Get {}", hint);
    return (hint.blackPeg == pinCount);
  }

}
