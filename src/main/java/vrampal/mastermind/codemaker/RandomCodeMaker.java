package vrampal.mastermind.codemaker;

import java.util.Random;

import lombok.Setter;
import vrampal.mastermind.Board;
import vrampal.mastermind.CodeMaker;

public class RandomCodeMaker implements CodeMaker {

  private final Random rand = new Random();

  @Setter
  private Board board;

  @Override
  public void play() {
    int[] secret = new int[board.pinCount];
    for (int pinIdx = 0; pinIdx < board.pinCount; pinIdx++) {
      secret[pinIdx] = rand.nextInt(board.maxVal);
    }
    
    board.recordSecret(secret);
  }

}
