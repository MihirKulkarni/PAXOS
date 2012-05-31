package Test;
import Paxos.*;

public class Test {
  public static void main(String[] inputs) {
    Network n=new Network(4, 1, 1);
    Paxos p=new Paxos(n);
    p.runPaxos();
  }
}
