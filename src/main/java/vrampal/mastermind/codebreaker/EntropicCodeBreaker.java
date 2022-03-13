package vrampal.mastermind.codebreaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vrampal.mastermind.Board;
import vrampal.mastermind.CodeBreaker;
import vrampal.mastermind.Hint;

@RequiredArgsConstructor
@Slf4j
public class EntropicCodeBreaker implements CodeBreaker {
  
  @AllArgsConstructor
  static final class MutableCounter {
    
    int value;

  }
  
  private static final double LOG2 = Math.log(2.0d);
  
  @Setter
  protected Board board;
  
  @Getter
  private long hypothesisCount = 0;
  
  @Override
  public void play(int turnIdx) {
    int[] hypothesis;
    if (turnIdx == 0) {
      hypothesis = firstTurn();
    } else {
      hypothesis = compute(turnIdx);
    }
    board.recordGuess(turnIdx, hypothesis);
  }
  
  int[] firstTurn() {
    int size = board.pinCount;
    int[] guess = new int[size];
    for(int idx = 0; idx < size; idx++) {
      guess[idx] = size - idx - 1;
    }
    hypothesisCount += 1;
    return guess;
  }
  
  int[] compute(int turnIdx) {
    List<Long> possibleSecrets = new ArrayList<>();
    
    // Keep only valid guess from all guess possible
    long maxGuess = board.countPossibleGuess();
    for (long guessIdx = 0; guessIdx < maxGuess; guessIdx++) {
      int[] hypothesis = board.long2Guess(guessIdx);
      if (checkHypothesis(turnIdx, hypothesis)) {
        possibleSecrets.add(guessIdx);
      }
    }
    int possibleSecretsSize = possibleSecrets.size();
    hypothesisCount += possibleSecretsSize;
    log.debug("Possible secret: {}", possibleSecretsSize);
    if (possibleSecretsSize == 1) {
      return board.long2Guess(possibleSecrets.get(0));
    }
    
    double remainingEntropy = 0.0d;
    if (log.isDebugEnabled()) {
      remainingEntropy = log2(possibleSecretsSize);
      log.debug("Remaining entropy: {}", remainingEntropy);
    }

    // Find the hypothesis with highest entropy
    double bestEntropy = -1.0d;
    int[] bestHypothesis = null;
    for(long value : possibleSecrets) {
      int[] hypothesis = board.long2Guess(value);
      double entropy = computeEntropy(hypothesis, possibleSecrets);
      if (bestEntropy < entropy) {
        bestEntropy = entropy;
        bestHypothesis = hypothesis;
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("Best entropy: {}", bestEntropy);
      double expectedEntropy = remainingEntropy - bestEntropy;
      log.debug("Expected entropy: {}", expectedEntropy);
    }
    return bestHypothesis;
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

  private double computeEntropy(int[] secret, List<Long> possibleSecrets) {
    Map<Hint, MutableCounter> possibleOutcomes = new HashMap<>();
    
    for(long value : possibleSecrets) {
      int[] hypothesis = board.long2Guess(value);
      Hint hint = new Hint(secret, hypothesis);
      if (!possibleOutcomes.containsKey(hint)) {
        possibleOutcomes.put(hint, new MutableCounter(1));
      } else {
        MutableCounter count = possibleOutcomes.get(hint);
        count.value++;
      }
    }
    
    double entropy = 0.0d;
    double possibleSecretsSize = possibleSecrets.size();
    for(MutableCounter counter : possibleOutcomes.values()) {
      double proba = counter.value / possibleSecretsSize;
      entropy += proba * log2(1.0d / proba);
    }
    
    return entropy;
  }

  private double log2(double value) {
    return Math.log(value) / LOG2;
  }
  
}
