
const int GAME_LENGTH = 12; // Number of turns before game end
const int PIN_COUNT = 4;    // Number of pins in the secret
const int MAX_VALUE = 8;    // Pin values in 0..(MAX_VAL-1)


struct Hint {
  byte blackPeg; // Right color at right place
  byte whitePeg; // Right color at wrong place
};

struct Board {
  byte secret[PIN_COUNT];
  byte guesses[GAME_LENGTH][PIN_COUNT];
  Hint hints[GAME_LENGTH];
};


Board board;
byte turnCount;


void computeHint(byte secret[], byte guess[], Hint *hint) {
  boolean secretPinUsed[PIN_COUNT];
  boolean guessPinUsed[PIN_COUNT];

  // Search for black peg
  hint->blackPeg = 0;
  for (byte pinIdx = 0; pinIdx < PIN_COUNT; pinIdx++) {
    if (guess[pinIdx] == secret[pinIdx]) {
      hint->blackPeg++;
      secretPinUsed[pinIdx] = true;
      guessPinUsed[pinIdx] = true;
    } else {
      secretPinUsed[pinIdx] = false;
      guessPinUsed[pinIdx] = false;
    }
  }

  // Search for white peg
  hint->whitePeg = 0;
  for (byte guessPinIdx = 0; guessPinIdx < PIN_COUNT; guessPinIdx++) {
    if (!guessPinUsed[guessPinIdx]) {
      for (byte secretPinIdx = 0; secretPinIdx < PIN_COUNT; secretPinIdx++) {
        if (!secretPinUsed[secretPinIdx] && (guess[guessPinIdx] == secret[secretPinIdx])) {
          hint->whitePeg++;
          secretPinUsed[secretPinIdx] = true;
          break;
        }
      }
    }
  }
}

void randomFill(byte guess[]) {
  for (byte pinIdx = 0; pinIdx < PIN_COUNT; pinIdx++) {
    guess[pinIdx] = random(MAX_VALUE);
  }
}

void playRandom() {
  byte *guess = board.guesses[turnCount];
  randomFill(guess);
}

boolean checkHypothesis(byte hypothesis[]) {
  Hint hint;
  for (byte prevTurnIdx = 0; prevTurnIdx < turnCount; prevTurnIdx++) {
    byte *prevGuess = board.guesses[prevTurnIdx];
    computeHint(hypothesis, prevGuess, &hint);
    Hint *prevHint = board.hints + prevTurnIdx;
    if ((prevHint->blackPeg != hint.blackPeg) || (prevHint->whitePeg != hint.whitePeg)) {
      return false;
    }
  }
  return true;
}

void playBruteForce() {
  byte hypothesis[PIN_COUNT];
  boolean possible;
  do {
    randomFill(hypothesis);
    possible = checkHypothesis(hypothesis);
  } while(!possible);
  byte *guess = board.guesses[turnCount];
  memcpy(hypothesis, guess, PIN_COUNT);
}

boolean checkGuess() {
  byte *guess = board.guesses[turnCount];
  Hint *hint = board.hints + turnCount;
  computeHint(board.secret, guess, hint);
  return (hint->blackPeg == PIN_COUNT);
}

void playOneGame() {
  randomFill(board.secret);

  turnCount = 0;
  boolean found = false;
  while ((turnCount < GAME_LENGTH) && !found) {
    // playRandom();
    playBruteForce();
    found = checkGuess();
    turnCount++;
  }

  if (found) {
    // Code breaker win
  } else {
    // Code maker win
  }
}


void setup() {
  // if analog input pin 0 is unconnected, random analog
  // noise will cause the call to randomSeed() to generate
  // different seed numbers each time the sketch runs.
  // randomSeed() will then shuffle the random function.
  randomSeed(analogRead(0));

  playOneGame();
}

void loop() {
  // Nothing to do
}
