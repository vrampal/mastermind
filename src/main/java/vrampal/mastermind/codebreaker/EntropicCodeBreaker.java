package vrampal.mastermind.codebreaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    long maxGuess = board.possibleGuessCount();
    for (long guessIdx = 0; guessIdx < maxGuess; guessIdx++) {
      int[] hypothesis = board.long2Guess(guessIdx);
      if (checkHypothesis(turnIdx, hypothesis)) {
        possibleSecrets.add(guessIdx);
      }
    }
    hypothesisCount += possibleSecrets.size();
    // int possibleSecretsSize = possibleSecrets.size();
    // log.debug("Possible secret: {}", possibleSecretsSize);
    // double remainingEntropy = log2(possibleSecretsSize);
    // log.debug("Remaining entropy: {}", remainingEntropy);

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
    // log.debug("Best entropy: {}", bestEntropy);
    // double expectedEntropy = remainingEntropy - bestEntropy;
    // log.info("Expected entropy: {}", expectedEntropy);
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
    Map<Hint, Integer> possibleOutcomes = new HashMap<>();
    
    for(long value : possibleSecrets) {
      int[] hypothesis = board.long2Guess(value);
      Hint hint = new Hint(secret, hypothesis);
      if (!possibleOutcomes.containsKey(hint)) {
        possibleOutcomes.put(hint, 1);
      } else {
        int count = possibleOutcomes.get(hint);
        count++;
        possibleOutcomes.put(hint, count);
      }
    }
    
    double totalEntropy = 0.0d;
    double possibleSecretsSize = (double) possibleSecrets.size();
    for(Entry<Hint, Integer> entry : possibleOutcomes.entrySet()) {
      int count = entry.getValue();
      double proba = count / possibleSecretsSize;
      double localEntropy = log2(1.0d / proba);
      totalEntropy += proba * localEntropy;
    }
    
    return totalEntropy;
  }

  private double log2(double value) {
    return Math.log(value) / LOG2;
  }
  
}
