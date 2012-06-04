package Test;
import Paxos.*;
import java.lang.Thread;


public class Test {
public static void test_0(){
  try{
    TestNetwork n=new TestNetwork(20,20,10);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(100000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1");
  }
  catch(Exception e){}
}
public static void test_1(){
  try{
    System.out.println("\n\nTestCase-1\n\n");
    Thread.sleep(1000);
    TestNetwork n=new TestNetwork(20,20,10);
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
		TestNetwork n = new TestNetwork(50, 50, 50);
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
//        test_0();
//        test_1();
//        test_2();
//        test_3();
//				test_4();
//				test_5();
       test_6();
       
    } 
}
