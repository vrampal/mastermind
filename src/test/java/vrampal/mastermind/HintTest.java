package vrampal.mastermind;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HintTest {

  @Test
  public void testGame1() {
    int[] secret = new int[] { 1, 2, 3, 4 };

    assertEquals(new Hint(4, 0), new Hint(secret, (new int[] { 1, 2, 3, 4 })));

    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 1, 2, 3, 5 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 1, 2, 3, 1 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 5, 2, 3, 4 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 4, 2, 3, 4 })));

    assertEquals(new Hint(2, 0), new Hint(secret, (new int[] { 1, 5, 3, 5 })));
    assertEquals(new Hint(2, 0), new Hint(secret, (new int[] { 1, 1, 3, 3 })));
    assertEquals(new Hint(2, 1), new Hint(secret, (new int[] { 1, 4, 3, 5 })));
    assertEquals(new Hint(2, 2), new Hint(secret, (new int[] { 1, 4, 3, 2 })));

    assertEquals(new Hint(1, 0), new Hint(secret, (new int[] { 5, 5, 3, 5 })));
    assertEquals(new Hint(1, 0), new Hint(secret, (new int[] { 3, 3, 3, 3 })));
    assertEquals(new Hint(1, 1), new Hint(secret, (new int[] { 5, 1, 3, 5 })));
    assertEquals(new Hint(1, 2), new Hint(secret, (new int[] { 5, 1, 3, 2 })));
    assertEquals(new Hint(1, 3), new Hint(secret, (new int[] { 4, 1, 3, 2 })));

    assertEquals(new Hint(0, 0), new Hint(secret, (new int[] { 5, 5, 5, 5 })));
  }

  @Test
  public void testGame2() {
    int[] secret = new int[] { 1, 0, 0, 1 };

    assertEquals(new Hint(4, 0), new Hint(secret, (new int[] { 1, 0, 0, 1 })));

    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 1, 0, 5, 1 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 1, 0, 1, 1 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 5, 0, 0, 1 })));
    assertEquals(new Hint(3, 0), new Hint(secret, (new int[] { 0, 0, 0, 1 })));

    assertEquals(new Hint(2, 0), new Hint(secret, (new int[] { 1, 5, 0, 5 })));

    assertEquals(new Hint(0, 0), new Hint(secret, (new int[] { 5, 5, 5, 5 })));
  }

}
