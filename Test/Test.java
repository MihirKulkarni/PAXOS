package Test;
import Paxos.*;

public class Test {
  public static void main(String[] inputs) {
    Network n=new Network(3,3,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
  }
}
