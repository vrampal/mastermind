package vrampal.mastermind.codebreaker;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vrampal.mastermind.Hint;

@RequiredArgsConstructor
public class BruteForceCodeBreaker extends RandomCodeBreaker {
  
  @Setter
  private int accuracy = 100; // Accuracy in percent, lower it to give a change to human player
  
  @Getter
  protected long hypothesisCount = 0;
  
  @Override
  public void play(int turnIdx) {
    int[] hypothesis;
    if (turnIdx == 0) {
      hypothesis = randomGen();
      hypothesisCount++;
    } else {
      hypothesis = compute1(turnIdx);
    }
    board.recordGuess(turnIdx, hypothesis);
  }
  
  int[] compute1(int turnIdx) {
    int[] hypothesis;
    boolean possible;
    
    // Take a random guess and check if it's valid
    do {
      hypothesis = randomGen();
      hypothesisCount++;
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    } while (!possible);
    
    return hypothesis;
  }
  
  int[] compute2(int turnIdx) {
    int[] hypothesis = board.long2Guess(hypothesisCount);
    boolean possible = checkHypothesisPossible(turnIdx, hypothesis);
    
    // Try all guess until one is valid
    while (!possible) {
      hypothesisCount++;
      hypothesis = board.long2Guess(hypothesisCount);
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    }
    
    return hypothesis;
  }

  int[] compute3(int turnIdx) {
    List<Long> possibleGuess = generatePossibleGuess(turnIdx);
    hypothesisCount += possibleGuess.size();
    
    // Take a random guess from the valid ones
    int index = rand.nextInt(possibleGuess.size());
    int[] hypothesis = board.long2Guess(possibleGuess.get(index));
    return hypothesis;
  }

  private boolean isAccurate() {
    return (accuracy >= 100) || (rand.nextInt(100) < accuracy);
  }

  protected final boolean checkHypothesisPossible(int turnIdx, int[] hypothesis) {
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
  
  protected final List<Long> generatePossibleGuess(int turnIdx) {
    List<Long> possibleGuess = new ArrayList<>();
    
    // Keep only valid guess from all guess possible
    long maxGuess = board.countPossibleGuess();
    for (long guessIdx = 0; guessIdx < maxGuess; guessIdx++) {
      int[] hypothesis = board.long2Guess(guessIdx);
      if (checkHypothesisPossible(turnIdx, hypothesis)) {
        possibleGuess.add(guessIdx);
      }
    }

    return possibleGuess;
  }
  
}
