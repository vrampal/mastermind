package vrampal.mastermind;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString(of = {"blackPeg", "whitePeg"})
public class Hint {

  public final int blackPeg; // Right color at right place

  public final int whitePeg; // Right color at wrong place

  public final boolean victory; // Is it a victory

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
    victory = (size == blackP);
    
    // Search for white peg
    int whiteP = 0;
    if (!victory) {
      for (int guessPinIdx = 0; guessPinIdx < size; guessPinIdx++) {
        if (!guessPinUsed[guessPinIdx]) {
          for (int secretPinIdx = 0; secretPinIdx < size; secretPinIdx++) {
            if (!secretPinUsed[secretPinIdx] && (guess[guessPinIdx] == secret[secretPinIdx])) {
              whiteP++;
              secretPinUsed[secretPinIdx] = true;
              break;
            }
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
