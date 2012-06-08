package Test;
import Paxos.*;
import java.lang.Thread;
import java.util.Date;


public class Test {
public static void test_0(){
  try{
    System.out.println("Test Case - 0 :");
    TestNetwork n=new TestNetwork(3,3,3);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(10000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-0");
  }
  catch(Exception e){}
}

public static void test_1a(){
  try{
    System.out.println("\n\nTestCase-1A\n\n");
    TestNetwork n=new TestNetwork(3,3,3); //0->single DP, 1-> All DP, 2-> Cycle DP, 3->make specific proposer as DP
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(1,-1);
    Thread.sleep(10000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1A");
  }
  catch(Exception e){}
}
public static void test_1b(){
  try{
    System.out.println("\n\nTestCase-1B\n\n");
    TestNetwork n=new TestNetwork(3,3,3); //0->single DP, 1-> All DP, 2-> Cycle DP, 3->make specific proposer as DP
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(2,-1);
    Thread.sleep(10000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1B");
  }
  catch(Exception e){}
}
public static void test_1c(){
  try{
    System.out.println("\n\nTestCase-1C\n\n");
    TestNetwork n=new TestNetwork(3,3,3); //0->single DP, 1-> All DP, 2-> Cycle DP, 3->make specific proposer as DP
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(3,2);
    Thread.sleep(10000);
    if(n.test_decision!=2)    
      System.out.println("Learnt a wrong value, Proposer-2 is active hence value should be 2 but Learnt value is "+n.test_decision);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1C");
  }
  catch(Exception e){}
}
public static void test_1(){
//  test_1a();
  test_1b();
//  test_1c();
}

public static void test_2(){
  try{
    TestNetwork n=new TestNetwork(5,2,3);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(1000);
    n.change_DPmode(2, -1);
    Thread.sleep(15000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1");
  }
  catch(Exception e){}
}

public static void test_3() {
	try {
		TestNetwork n = new TestNetwork(2,5,2);
		Paxos p = new Paxos(n);
		p.runPaxos();
		Thread.sleep(500);
		n.block_channel(1, 1);
		n.block_channel(4, 1);
		n.block_channel(6, 1);
		n.change_DPmode(3, 0);
		Thread.sleep(5000);
		n.block_channel(2, 1);
		n.block_channel(5, 1);
		n.block_channel(1, 0);
		n.block_channel(4, 0);
		n.block_channel(6, 0);
		n.change_DPmode(3, 1);
		Thread.sleep(15000);
		n.terminate_run();
		System.out.println("\n\nTERMINATED PAXOS RUN IN TEST CASE 3");
	}
	catch (Exception e) {}
}

public static void test_4() {
	try {
		TestNetwork n = new TestNetwork(10, 10, 10);
		Paxos p = new Paxos(n);
		p.runPaxos();
		Thread.sleep(500);
		n.change_DPmode(3, 1);
		Thread.sleep(100);
		n.change_DPmode(3, 2);
		Thread.sleep(100);
		n.change_DPmode(3, 3);
		Thread.sleep(100);
		n.change_DPmode(3, 4);
		Thread.sleep(100);
		n.change_DPmode(1, -1);
		Thread.sleep(150000);
		n.terminate_run();
		System.out.println("\n\nTERMINATED PAXOS RUN IN TEST CASE 4");
	}
	catch (Exception e) {}
}

public static void test_5() {
	try {
		TestNetwork n = new TestNetwork(3,3,3);
		Paxos p = new Paxos(n);
		p.runPaxos();
		Thread.sleep(500);
		n.change_DPmode(1, -1);
		Thread.sleep(100000);
		n.terminate_run();
		System.out.println("\n\nTERMINATED PAXOS RUN IN TEST CASE 5");
	}
	catch (Exception e) {}
}

public static void test_6(){
  try{

    TestNetwork n=new TestNetwork(3,3,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(500);
   n.change_DPmode(3,2);
    n.lossy_channel(6,1);
    n.lossy_channel(7,1);
    Thread.sleep(1000);
    n.lossy_channel(6,0);
    n.lossy_channel(7,0);
    Thread.sleep(100000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1");
  }
  catch(Exception e){}
}

  public static void main(String[] inputs) {
//        test_0();  //basic test to make sure paxos works in normal conditions
        test_1();  //change DP modes
//      test_2();  //block channel and release channel (case to chk learners releasing channel succesively)
//      test_3();  //reorder messages (have a mix of this in all cases)
//	test_4();  //duplicate messages 
//      test_5();  //drop messages ..lossy channel
//      test_6();  //play with initial value, try few cases sending MAX_INTs and MIN_INTs 
//      test_7();  //placeholder for more crap

//      test_final();  //stress test with tons of processes and let it end when it wishes. DONT TERMINATE!!!
       
    } 
}
