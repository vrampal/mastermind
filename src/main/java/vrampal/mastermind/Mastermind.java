package vrampal.mastermind;

import lombok.extern.slf4j.Slf4j;
import vrampal.mastermind.codebreaker.*;
import vrampal.mastermind.codemaker.*;

@Slf4j
public class Mastermind {

  public static void main(String[] args) {
    new Mastermind().playOneGame();
  }
  
  private long gameCount = 0;
  
  private long codeBreakerWin = 0;
  
  private long codeMakerWin = 0;
  
  private long turnCount = 0;
  
  private long hypothesisCount = 0;
  
  public void computeStats() {
    for (long gameIdx = 0; gameIdx < 100_000L; gameIdx++) {
      playOneGame();
    }
    
    double codeBreakerWinRate = codeBreakerWin / (double) gameCount;
    log.info("codeBreakerWinRate {}", codeBreakerWinRate);
    double codeMakerWinRate = codeMakerWin / (double) gameCount;
    log.info("codeMakerWinRate {}", codeMakerWinRate);
    double avgTurnCount = turnCount / (double) gameCount;
    log.info("avgTurnCount {}", avgTurnCount);
    double avgHypothesis = hypothesisCount / (double) gameCount;
    log.info("avgHypothesis {}", avgHypothesis);
  }

  public void playOneGame() {
    log.info("Starting new game");
    
    Board board = new Board(12, 4, 8);
    CodeMaker codeMaker = new RandomCodeMaker();
    codeMaker.setBoard(board);
    codeMaker.play();

    BruteForceCodeBreaker codeBreaker = new BruteForceCodeBreaker();
    codeBreaker.setBoard(board);
    
    int turnIdx = 0;
    boolean found = false;
    while ((turnIdx < board.gameLength) && !found) {
      codeBreaker.play(turnIdx);
      found = board.checkGuess(turnIdx);
      turnIdx++;
    }

    gameCount++;
    String winner;
    if (found) {
      winner = "CodeBreaker";
      codeBreakerWin++;
    } else {
      winner = "CodeMaker";
      codeMakerWin++;
    }
    turnCount += turnIdx;
    hypothesisCount += codeBreaker.getHypothesisCount();
    log.info("{} win, {} turns, {} hypothesis", winner, turnIdx, codeBreaker.getHypothesisCount());
  }

}
