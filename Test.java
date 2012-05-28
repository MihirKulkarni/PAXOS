public class Test {
  public static void main(String[] inputs) {
    Network n=new Network(2, 1, 1);
    Paxos p=new Paxos(n);
    p.runPaxos();
  }
}
