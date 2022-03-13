package vrampal.mastermind.codebreaker;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vrampal.mastermind.Hint;

@Slf4j
public class EntropicCodeBreaker extends BruteForceCodeBreaker {
  
  @AllArgsConstructor
  static final class MutableCounter {
    
    int value;

  }
  
  private static final double LOG2 = Math.log(2.0d);
  
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
    return guess;
  }
  
  int[] compute(int turnIdx) {
    updatePossibleSecrets(turnIdx);
    
    int possibleSecretsSize = possibleSecrets.size();
    if (possibleSecretsSize == 1) {
      return possibleSecrets.get(0);
    }
    
    double remainingEntropy = 0.0d;
    if (log.isDebugEnabled()) {
      remainingEntropy = log2(possibleSecretsSize);
      log.debug("Remaining entropy: {}", remainingEntropy);
    }

    // Find the hypothesis with highest entropy
    double bestEntropy = -1.0d;
    int[] bestHypothesis = null;
    for(int[] hypothesis : possibleSecrets) {
      double entropy = computeEntropy(hypothesis);
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
  
  private double computeEntropy(int[] secret) {
    Map<Hint, MutableCounter> possibleOutcomes = new HashMap<>();
    
    for(int[] hypothesis : possibleSecrets) {
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
