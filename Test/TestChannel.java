package Test;
import Paxos.*;
import java.util.LinkedList;
import java.util.Date;

public class TestChannel extends Channel {
  TestNetwork test_network;
  int test_index=-1;
  int terminate=0;//set to 1 to terminate;
  int block_channel=0; //set to 1 to block messages
  int DP_mode=-1; //0->single DP, 1-> All DP, 2-> Cycle DP, 3->make specific proposer as DP
  int requested_DP=-1; // process ID for DP_mode=3
  int lose_msg=0; //0->normal operation 1->lose all message in queue
  int dup_msg = 0; //0->normal operation 1->wait and duplicate message 2->send multiple copies
  int reorder_msg=0; //0->normal operation using FIFO queues 1-> reorder message using LIFO stack

  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    throw_exception();
    if(reorder_msg==0){
      synchronized(test_network.test_queues[destination]) {
        if(dup_msg==0)
          test_network.test_queues[destination].add(message);
        if(dup_msg == 1){
          try{
            Thread.sleep(200);
            for(int i = 3; i>0; i--)
              test_network.test_queues[destination].add(message);
          }catch (Exception e){} 
        }   
        if(dup_msg == 2){
          for(int i = 3; i>0; i--)
            test_network.test_queues[destination].add(message);
        }   
      }
    }  
    if(reorder_msg==1){
      synchronized(test_network.test_stacks[destination]) {
        if(dup_msg==0)
          test_network.test_stacks[destination].push(message);
        if(dup_msg == 1){
          try{
            Thread.sleep(200);
            for(int i = 3; i>0; i--)
              test_network.test_stacks[destination].push(message);
          }catch (Exception e){} 
        }   
        if(dup_msg == 2){
          for(int i = 3; i>0; i--)
            test_network.test_stacks[destination].push(message);
        }   
      }
    }

  }

  /** Receive a message. */

  public String receiveMessage() {
    throw_exception();
    if(block_channel==1){
      return null;
    }
    if(reorder_msg==0){
      synchronized(test_network.test_queues[test_index]) {
        if(lose_msg==1){
          while(!test_network.test_queues[test_index].isEmpty())
            test_network.test_queues[test_index].remove();
//        System.out.println("Removed all msgs for P-"+test_index);
        }
        else{    
          if (!test_network.test_queues[test_index].isEmpty())
  	    return test_network.test_queues[test_index].remove();
          else
	    return null;
        }
      }
    }

    if(reorder_msg==1){
      synchronized(test_network.test_stacks[test_index]) {
        if(lose_msg==1){
          while(!test_network.test_stacks[test_index].isEmpty())
            test_network.test_stacks[test_index].pop();
//        System.out.println("Removed all msgs for P-"+test_index);
        }
        else{    
          if (!test_network.test_stacks[test_index].isEmpty())
  	    return test_network.test_stacks[test_index].pop();
          else
	    return null;
        }
      }
    }
    return null;
  }

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished(){
    throw_exception();
    while(DP_mode==-1){}
    if(DP_mode==0){  
      if (test_index==0)
        return true;
    }
    if(DP_mode==1){  
      if (test_index<test_network.test_numProposers)
        return true;
    }
    if(DP_mode==2){  
      Date d=new Date();
      long cur_time=d.getTime();
      if ((cur_time/1000)%test_network.test_numProposers==test_index){
//        System.out.println("Cycling DP-"+test_index+"  time:"+(cur_time/10000));
        return true;
      } 
    }
    if(DP_mode==3){
      if (test_index==requested_DP)
        return true;
    }
 
    if (test_index>=test_network.test_numProposers)
      throw new Error("Non-proposers should not be asking whether they are distinguished");
    return false;

  }

  /** Call this function to register a decision by a learner. */

  public void decide(int decision) {
    if (test_index<(test_network.test_numProposers+test_network.test_numAcceptors))
      throw new Error("Non-learner should not be deciding a value");

    if (decision>=test_network.test_numProposers)
      throw new Error("The decided value was not an initial value...");

    synchronized(test_network) {
      if (test_network.test_decision==-1)
	test_network.test_decision=decision;
      else {
	if (test_network.test_decision!=decision)
	  System.out.println("Disagreement between Learners. PAXOS BROKEN!!! :)");
      }
    }
    throw_exception();
  }
  /** Call this function to get the initial value for a proposer. */

  public int getInitialValue() {
    throw_exception();
    if (test_index>=test_network.test_numProposers)
      throw new Error("Non-proposers should not be asking for initial value");
    return test_index;
  }
  
  
  public void throw_exception(){
    StopError s=new StopError();  
    if(terminate==1)
      s.throw_error();
  }

	public void sleep() {
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
				//ignore exception
				System.out.println("Interrupted");
			}
	}
}
