package Test;
public class StopError extends Error {
  public void throw_error(){
    throw new Error("Terminate thread");
  }
}

