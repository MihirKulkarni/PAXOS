package Test;
import Paxos.*;
import java.lang.Thread;


public class Test {
public static void test_0(){
  try{
    TestNetwork n=new TestNetwork(3,3,3);
    Paxos p=new Paxos(n);
    p.runPaxos();
//    Thread.sleep(10000);
//    n.terminate_run();
//    System.out.println("\n\nTERMINATED PAXOS RUN-1");
  }
  catch(Exception e){}
}
public static void test_1(){
  try{
    TestNetwork n=new TestNetwork(4,3,4);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(1000); //sleep necessary, you cant block channel unless each paxos process have created channels, throws exception
    n.block_channel(8,1);
    Thread.sleep(1000);
    n.block_channel(9,1);
    Thread.sleep(1000);
    n.block_channel(10,1);
    Thread.sleep(1000);
    n.block_channel(8,0);
    Thread.sleep(1000);
    n.block_channel(9,0);
    Thread.sleep(1000);
    n.block_channel(10,0);
    Thread.sleep(10000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1");
  }
  catch(Exception e){}
}


  public static void main(String[] inputs) {
        test_0();
//      test_1();
       
    } 
}
