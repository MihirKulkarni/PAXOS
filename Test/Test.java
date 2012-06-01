package Test;
import Paxos.*;

public class Test {
  public static void main(String[] inputs) {
    Network n=new TestNetwork(2,2,1);
//    Paxos p=new Paxos(n);
		System.out.println(n.numAcceptors());
//    p.runPaxos();
  }
}
