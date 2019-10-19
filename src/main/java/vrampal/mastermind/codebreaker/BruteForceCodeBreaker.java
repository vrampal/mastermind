package vrampal.mastermind.codebreaker;

import lombok.Getter;
import vrampal.mastermind.Hint;

public class BruteForceCodeBreaker extends RandomCodeBreaker {
  
  @Getter
  private long hypothesisCount = 0;
  
  @Override
  public void play(int turnIdx) {
    int[] hypothesis;
    boolean possible;
    do {
      hypothesis = randomGuess();
      hypothesisCount++;
      possible = checkHypothesis(turnIdx, hypothesis);
    } while (!possible);
    
    board.recordGuess(turnIdx, hypothesis);
  }

  private boolean checkHypothesis(int turnIdx, int[] hypothesis) {
    for (int prevTurnIdx = 0; prevTurnIdx < turnIdx; prevTurnIdx++) {
      int[] prevGuess = board.guesses[prevTurnIdx];
      Hint hint = new Hint(hypothesis, prevGuess);
      Hint prevHint = board.hints[prevTurnIdx];
      if (!prevHint.equals(hint)) {
        return false;
      }
    }
    return true;
  }
  
}
