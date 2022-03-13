package vrampal.mastermind;

import lombok.extern.slf4j.Slf4j;
import vrampal.mastermind.codebreaker.BruteForceCodeBreaker;
import vrampal.mastermind.codebreaker.EntropicCodeBreaker;
import vrampal.mastermind.codemaker.RandomCodeMaker;

@Slf4j
public class MastermindApp {

  private static final long GAME_COUNT_FOR_STATS = 10_000L;
  
  public static void main(String[] args) {
    new MastermindApp().playOneGame();
  }
  
  private long gameCount = 0;
  
  private long codeBreakerWin = 0;
  
  private long codeMakerWin = 0;
  
  private long turnCount = 0;
  
  private long hypothesisCount = 0;
  
  public void computeStats() {
    log.info("Computing stats on {} games", GAME_COUNT_FOR_STATS);

    long begin = System.nanoTime();
    for (long gameIdx = 0; gameIdx < GAME_COUNT_FOR_STATS; gameIdx++) {
      playOneGame();
    }
    long elapsed = System.nanoTime() - begin;
    log.info("Elapsed {} ms", elapsed / 1_000_000L);
    
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

    BruteForceCodeBreaker codeBreaker;
    // codeBreaker = new BruteForceCodeBreaker();
    codeBreaker = new EntropicCodeBreaker();
    // codeBreaker.setAccuracy(65); // 65% accuracy to give a chance to human player xD
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
