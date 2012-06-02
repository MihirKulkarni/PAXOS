package Test;
import Paxos.*;

public class Test {
  public static void main(String[] inputs) {
    TestNetwork n=new TestNetwork(4,3,4);
    Paxos p=new Paxos(n);
    System.out.println(n.numAcceptors());
    p.runPaxos();
  }
}
