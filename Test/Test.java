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
    n.change_DPmode(0,0);
    Thread.sleep(3000);
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
    n.change_DPmode(-1,1);
    Thread.sleep(3000);
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
    n.change_DPmode(-1,2);
    Thread.sleep(3000);
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
    n.change_DPmode(2,3);
    Thread.sleep(3000);
    if(n.test_decision!=2)    
      System.out.println("Learnt a wrong value, Proposer-2 is active hence value should be 2 but Learnt value is "+n.test_decision);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-1C");
  }
  catch(Exception e){}
}
public static void test_1(){
  test_1a();
  test_1b();
  test_1c();
}


// Total time estimate: 20 seconds
public static void test_2(){
  try{
    System.out.println("\n\nTestCase-2\n\n");
    TestNetwork n=new TestNetwork(4,4,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(-1,1);
    for(int i=0;i<10;i++)    //Block the channel of all processes
      n.block_channel(i, 1); 
    Thread.sleep(500);
    for(int i=0;i<4;i++)    //Release all Proposers
      n.block_channel(i,0); 
    Thread.sleep(1000);
    for(int i=4;i<8;i++)   //Release all Acceptors
      n.block_channel(i,0);
    Thread.sleep(1000);
    n.block_channel(9,0); //Release one of the learner's channel
    Thread.sleep(1000);  
    n.block_channel(8,0);  //Lets release the next learner's channel
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-2");
  }
  catch(Exception e){}
}
// Total time estimate: 20 seconds
public static void test_3a(){
  try{
    System.out.println("\n\nTestCase-3A\n\n");
    TestNetwork n=new TestNetwork(4,4,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(-1,1);
    for(int i=0;i<10;i++)    //Block the channel of all processes
      n.block_channel(i, 1); 
    Thread.sleep(500);
    for(int i=0;i<4;i++)    //Release all Proposers
      n.block_channel(i,0); 
    Thread.sleep(1000);
    for(int i=0;i<9;i++)   //Shuffling all message queues
      n.shuffle_msg(i);
    for(int i=4;i<8;i++)   //Release all Acceptors
      n.block_channel(i,0);
    Thread.sleep(1000);
    n.block_channel(9,0); //Release one of the learner's channel
    Thread.sleep(1000);  
    n.block_channel(8,0);  //Lets release the next learner's channel
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-3A");
  }
  catch(Exception e){}
}
// Total time estimate: 20 seconds
public static void test_3b(){
  try{
    System.out.println("\n\nTestCase-3B\n\n");
    TestNetwork n=new TestNetwork(4,4,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(-1,1);
    for(int i=0;i<10;i++){    //Block the channel of all processes
      n.block_channel(i, 1); 
      n.reorder_msg(i,1);     //Use LIFO stack as message queue  
    }
    Thread.sleep(500);
    for(int i=0;i<4;i++)    //Release all Proposers
      n.block_channel(i,0); 
    Thread.sleep(1000);
    for(int i=0;i<9;i++)   //Shuffling all message queues
      n.shuffle_msg(i);
    for(int i=4;i<8;i++)   //Release all Acceptors
      n.block_channel(i,0);
    Thread.sleep(1000);
    n.block_channel(9,0); //Release one of the learner's channel
    Thread.sleep(1000);  
    n.block_channel(8,0);  //Lets release the next learner's channel
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-3B");
  }
  catch(Exception e){}
}
public static void test_3() {
  test_3a();
  test_3b();
}


public static void test_4a() {
	/* Duplicate messages sent by proposer to acceptor */
	try {
		TestNetwork n = new TestNetwork(5, 5, 2);
		Paxos p = new Paxos(n);
		p.runPaxos();
		Thread.sleep(1000);
		n.change_DPmode(1, 1);		// All proposers are Distinguished
		n.dup_msg(0, 2);					// Duplicate messages sent by proposer 0
		n.dup_msg(1, 2);					// Duplicate messages sent by proposer 1
		n.dup_msg(3, 2);					// Duplicate messages sent by proposer 3
		Thread.sleep(5000);
		n.terminate_run();
		System.out.println("\n\nTERMINATED PAXOS RUN-4a");

	} catch(Exception e) {}
}

public static void test_4b() {
	/* Duplicate messages sent by acceptor */
	try {
		TestNetwork n = new TestNetwork(5, 5, 2);
		Paxos p = new Paxos(n);
		p.runPaxos();
		n.change_DPmode(1, 1);
		n.dup_msg(5, 2);
		n.dup_msg(7, 2);
		n.dup_msg(9, 2);
		Thread.sleep(5000);
		n.terminate_run();
		System.out.println("\n\nTERMINATED PAXOS RUN-4b");

	} catch(Exception e) {}
}

public static void test_4() {
	test_4a();
	test_4b();
}

public static void test_5a() {
	try {
    TestNetwork n=new TestNetwork(3,3,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(500);
    n.change_DPmode(2,3);
    n.lossy_channel(2,1);
    n.lossy_channel(6,1);
    n.lossy_channel(7,1);
    Thread.sleep(500);
    n.lossy_channel(2,0);
    n.lossy_channel(6,0);
    n.lossy_channel(7,0);
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-5a");
	} catch(Exception e) {}
}

public static void test_5b() {
	try {
    TestNetwork n=new TestNetwork(3,3,2);
    Paxos p=new Paxos(n);
    p.runPaxos();
    Thread.sleep(500);
    n.change_DPmode(0,1);		// All are distinguished proposers
    n.lossy_channel(0,1);			// Proposer 0 lose all messages
    n.lossy_channel(1,1);			// Proposer 1 lose all message
    n.dup_msg(2,2);
    Thread.sleep(1000);				// Give time for proposer 2 to complete with duplicate messages
    n.lossy_channel(6,1);			// Learner 1 lose all messages
    n.lossy_channel(7,1);			// Learner 2 lose all messages
    Thread.sleep(1000);
    n.lossy_channel(0,0);			// Proposer 0 starts getting messages
    n.lossy_channel(1,0);			// Proposer 1 starts getting messages
    n.lossy_channel(6,0);			// Learner 1 starts getting messages
    n.lossy_channel(7,0);     // Learner 2 starts getting messages
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-5b");
	} catch(Exception e) {}
}

public static void test_5() {
	test_5a();
	test_5b();
}

public static void test_6(){
  try{
    for(int i=0;i<6;i++){
    TestNetwork n=new TestNetwork(5,5,3);
    Paxos p=new Paxos(n);
    p.runPaxos();
    if(i%2==0)
      n.change_DPmode(2,3);
    else
      n.change_DPmode(1,1);
    n.change_init_logic(i);
    Thread.sleep(5000);
    n.terminate_run();
    System.out.println("\n\nTERMINATED PAXOS RUN-6."+i);
    }
  }
  catch(Exception e){}
}

public static void test_final(){
    System.out.println("Test Case - FINAL :");
    TestNetwork n=new TestNetwork(50,50,50);
    Paxos p=new Paxos(n);
    p.runPaxos();
    n.change_DPmode(0,0);

}
  public static void main(String[] inputs) {
/*
Date d1=new Date();
      test_0();  //basic test to make sure paxos works in normal conditions
      test_1();  //change DP modes
      test_2();  //block channel and release channel (case to chk learners releasing channel succesively)
      test_3();  //reorder messages (have a mix of this in all cases)
	test_4();  //duplicate messages 
      test_5();  //drop messages ..lossy channel
      test_6();  //play with initial value, try few cases sending MAX_INTs and MIN_INTs 
//      test_7();  //placeholder for more crap
Date d2=new Date();
System.out.println(d2.getTime()-d1.getTime());
*/
      test_final();  //stress test with tons of processes and let it end when it wishes. DONT TERMINATE!!!
       
  } 
}
