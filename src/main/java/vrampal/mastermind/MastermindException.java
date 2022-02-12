package vrampal.mastermind;

public class MastermindException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  MastermindException(String message) {
    super(message);
  }

  MastermindException(String message, Throwable cause) {
    super(message, cause);
  }

}
