package Test;
import Paxos.*;
import java.util.LinkedList;
import java.util.Date;
import java.util.EmptyStackException;

public class TestChannel extends Channel {
  TestNetwork test_network;
  int test_index;
  int terminate=0;//set to 1 to terminate;
  int block_channel=0; //set to 1 to block messages
  int DP_mode=0; //0->single DP, 1-> All DP, 2-> Cycle DP

  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    throw_exception();
    synchronized(test_network.test_queues[destination]) {
      test_network.test_queues[destination].add(message);
    }
  }
  public void blockchannel(){
    System.out.println("CHANNEL for process-"+test_index+" BLOCKED");
    block_channel=1;
  }
  public void releasechannel(){
    System.out.println("CHANNEL for process-"+test_index+" RELEASED");
    block_channel=0;
  }


  /** Receive a message. */

  public String receiveMessage() {
    throw_exception();
    synchronized(test_network.test_queues[test_index]) {
      if(block_channel==1){
        return null;
      }
      else{    
        if (!test_network.test_queues[test_index].isEmpty())
	  return test_network.test_queues[test_index].remove();
        else
	  return null;
      } 
    }
  }

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished() {
    throw_exception();
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
      if ((cur_time/10000)%test_network.test_numProposers==test_index){
//        System.out.println("Cycling DP-"+test_index+"  time:"+(cur_time/10000));
        return true;
      } 
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
  
  public void terminate(){
	terminate=1;
  }
  
  public void throw_exception(){
    if(terminate==1)
      throw new Error("Terminate thread");
  }


}
