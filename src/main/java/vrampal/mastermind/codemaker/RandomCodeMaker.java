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
    int[] secret = randomGen();
    board.recordSecret(secret);
  }

  protected final int[] randomGen() {
    int[] guess = new int[board.pinCount];
    for (int pinIdx = 0; pinIdx < board.pinCount; pinIdx++) {
      guess[pinIdx] = rand.nextInt(board.maxVal);
    }
    return guess;
  }
  
}
