package pl.serwis.komputerowy.web.error;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) { super(message); }
}
