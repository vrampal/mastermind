package vrampal.mastermind;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Hint {

  public final int blackPeg; // Right color at right place

  public final int whitePeg; // Right color at wrong place

  public Hint(int[] secret, int[] guess) {
    int size = secret.length;
    boolean[] secretPinUsed = new boolean[size];
    boolean[] guessPinUsed = new boolean[size];

    // Search for black peg
    int blackP = 0;
    for (int pinIdx = 0; pinIdx < size; pinIdx++) {
      if (guess[pinIdx] == secret[pinIdx]) {
        blackP++;
        secretPinUsed[pinIdx] = true;
        guessPinUsed[pinIdx] = true;
      }
    }
    blackPeg = blackP;

    // Search for white peg
    int whiteP = 0;
    for (int guessPinIdx = 0; guessPinIdx < size; guessPinIdx++) {
      if (!guessPinUsed[guessPinIdx]) {
        for (int secretPinIdx = 0; secretPinIdx < size; secretPinIdx++) {
          if (!secretPinUsed[secretPinIdx] && (guess[guessPinIdx] == secret[secretPinIdx])) {
            whiteP++;
            secretPinUsed[secretPinIdx] = true;
            // guessPinUsed[guessPinIdx] = true;
            break;
          }
        }
      }
    }
    whitePeg = whiteP;
    // nanoDelay(1_000_000L); // Simulate a slow computation
  }

  private void nanoDelay(long delay) {
    long end = System.nanoTime() + delay;
    while (System.nanoTime() < end);
  }
  
}
