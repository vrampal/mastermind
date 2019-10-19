package vrampal.mastermind.codebreaker;

import java.util.Random;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vrampal.mastermind.Hint;

@RequiredArgsConstructor
public class BruteForceCodeBreaker extends RandomCodeBreaker {
  
  private final Random rand = new Random();
  
  private final int accuracy;
  
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
      if (!prevHint.equals(hint) && isAccurate()) {
        return false;
      }
    }
    return true;
  }
  
  private boolean isAccurate() {
    return (rand.nextInt(100) < accuracy);
  }
  
}
