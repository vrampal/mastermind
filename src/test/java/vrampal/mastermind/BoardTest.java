package vrampal.mastermind;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BoardTest {

  private final Board board1 = new Board(12, 4, 8);

  private final Board board2 = new Board(12, 6, 10);

  @Test
  public void testValidGuess1() {
    assertFalse(board1.validGuess(null));
    assertFalse(board1.validGuess(new int[] {}));

    assertFalse(board1.validGuess(new int[] { 0, 0, 0 }));
    assertTrue(board1.validGuess(new int[] { 0, 0, 0, 0 }));
    assertFalse(board1.validGuess(new int[] { 0, 0, 0, 0, 0 }));

    assertTrue(board1.validGuess(new int[] { 1, 2, 3, 4 }));
    assertTrue(board1.validGuess(new int[] { 1, 2, 7, 4 }));
    assertFalse(board1.validGuess(new int[] { 1, 2, 8, 4 }));
    assertFalse(board1.validGuess(new int[] { 1, 2, -1, 4 }));
  }

  @Test
  public void testValidGuess2() {
    assertFalse(board2.validGuess(null));
    assertFalse(board2.validGuess(new int[] {}));

    assertFalse(board2.validGuess(new int[] { 0, 0, 0, 0, 0 }));
    assertTrue(board2.validGuess(new int[] { 0, 0, 0, 0, 0, 0 }));
    assertFalse(board2.validGuess(new int[] { 0, 0, 0, 0, 0, 0, 0 }));

    assertTrue(board2.validGuess(new int[] { 1, 2, 3, 4, 5, 6 }));
    assertTrue(board2.validGuess(new int[] { 1, 2, 3, 9, 5, 6 }));
    assertFalse(board2.validGuess(new int[] { 1, 2, 3, 10, 5, 6 }));
    assertFalse(board2.validGuess(new int[] { 1, 2, 3, -1, 5, 6 }));
  }

  @Test
  public void testPossibleGuessCount() {
    assertEquals(4096L, board1.countPossibleGuess());
    assertEquals(1_000_000L, board2.countPossibleGuess());
  }

  @Test
  public void testLong2Guess() {
    assertArrayEquals(new int[] { 0, 0, 0, 0 }, board1.long2Guess(0L));
    assertArrayEquals(new int[] { 1, 0, 0, 0 }, board1.long2Guess(1L));
    assertArrayEquals(new int[] { 0, 1, 0, 0 }, board1.long2Guess(8L));
    assertArrayEquals(new int[] { 0, 0, 1, 0 }, board1.long2Guess(64L));
    assertArrayEquals(new int[] { 0, 0, 0, 1 }, board1.long2Guess(512L));
    assertArrayEquals(new int[] { 7, 7, 7, 7 }, board1.long2Guess(4095L));
  }
}
