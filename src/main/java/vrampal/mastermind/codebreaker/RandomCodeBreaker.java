package vrampal.mastermind.codebreaker;

import java.util.Random;

import lombok.Setter;
import vrampal.mastermind.Board;
import vrampal.mastermind.CodeBreaker;

public class RandomCodeBreaker implements CodeBreaker {

  protected final Random rand = new Random();

  @Setter
  protected Board board;
  
  @Override
  public void play(int turnIdx) {
    int[] guess = randomGen();
    board.recordGuess(turnIdx, guess);
  }
  
  protected final int[] randomGen() {
    int[] guess = new int[board.pinCount];
    for (int pinIdx = 0; pinIdx < board.pinCount; pinIdx++) {
      guess[pinIdx] = rand.nextInt(board.maxVal);
    }
    return guess;
  }
  
}
