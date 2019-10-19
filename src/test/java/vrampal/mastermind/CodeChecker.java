package vrampal.mastermind;

import lombok.RequiredArgsConstructor;
import vrampal.mastermind.Hint;

@RequiredArgsConstructor
public class CodeChecker {

  private final int[] secret;
  
  Hint giveHint(int[] proposal) {
    return new Hint(secret, proposal);
  }
  
}
