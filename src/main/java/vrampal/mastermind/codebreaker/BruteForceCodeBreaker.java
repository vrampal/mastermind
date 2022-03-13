package vrampal.mastermind.codebreaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vrampal.mastermind.Hint;

@Slf4j
public class BruteForceCodeBreaker extends RandomCodeBreaker {

  @Setter
  private int accuracy = 100; // Accuracy in percent, lower it to give a change to human player

  private long bruteForceIndex;

  protected List<int[]> possibleSecrets;

  @Override
  public void play(int turnIdx) {
    int[] hypothesis;
    if (turnIdx == 0) {
      hypothesis = randomGen();
    } else {
      hypothesis = compute3(turnIdx);
    }
    board.recordGuess(turnIdx, hypothesis);
  }

  int[] compute1(int turnIdx) {
    int[] hypothesis = randomGen();
    boolean possible = checkHypothesisPossible(turnIdx, hypothesis);

    // Take a random guess and check if it's valid
    while (!possible) {
      hypothesis = randomGen();
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    }

    return hypothesis;
  }

  int[] compute2(int turnIdx) {
    int[] hypothesis = board.long2Guess(bruteForceIndex);
    boolean possible = checkHypothesisPossible(turnIdx, hypothesis);

    // Try all guess until one is valid
    while (!possible) {
      bruteForceIndex++;
      hypothesis = board.long2Guess(bruteForceIndex);
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    }

    return hypothesis;
  }

  int[] compute3(int turnIdx) {
    updatePossibleSecrets(turnIdx);

    // Take a random guess from the valid ones
    int index = rand.nextInt(possibleSecrets.size());
    int[] hypothesis = possibleSecrets.get(index);

    return hypothesis;
  }

  private boolean isAccurate() {
    return (accuracy >= 100) || (rand.nextInt(100) < accuracy);
  }

  protected final boolean checkHypothesisPossible(int turnIdx, int[] hypothesis) {
    for (int prevTurnIdx = 0; prevTurnIdx < turnIdx; prevTurnIdx++) {
      int[] prevGuess = board.getGuess(prevTurnIdx);
      Hint hint = new Hint(hypothesis, prevGuess);
      Hint prevHint = board.getHint(prevTurnIdx);
      if (!prevHint.equals(hint) && isAccurate()) {
        return false;
      }
    }
    return true;
  }

  private void generatePossibleSecrets(int turnIdx) {
    possibleSecrets = new ArrayList<>();

    // Keep only valid guess from all guess possible
    long maxGuess = board.countPossibleGuess();
    for (long guessIdx = 0; guessIdx < maxGuess; guessIdx++) {
      int[] hypothesis = board.long2Guess(guessIdx);
      if (checkHypothesisPossible(turnIdx, hypothesis)) {
        possibleSecrets.add(hypothesis);
      }
    }
  }

  private void filterPossibleSecrets(int turnIdx) {
    Iterator<int[]> iter = possibleSecrets.iterator();
    while (iter.hasNext()) {
      int[] hypothesis = iter.next();
      if (!checkHypothesisPossible(turnIdx, hypothesis)) {
        iter.remove();
      }
    }
  }

  protected final void updatePossibleSecrets(int turnIdx) {
    if (possibleSecrets == null) {
      generatePossibleSecrets(turnIdx);
    } else {
      filterPossibleSecrets(turnIdx);
    }
    log.debug("Possible secret: {}", possibleSecrets.size());
  }

}
