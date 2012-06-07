package Test;
import Paxos.*;
import java.util.LinkedList;
import java.util.Stack;
public class TestNetwork extends Network {
  int test_totalProcesses;
  int test_numProposers;
  int test_numAcceptors;
  int test_numLearners;
  int test_decision=-1;
  TestChannel channels[]=new TestChannel[100];
  Stack<String>[] test_queues;
//Stack<String>[] test_queues=new Stack<String>()[];

  /** Create a network with test_numProposers proposes, test_numAcceptors
   * acceptors, and test_numLearners learners.*/

  @SuppressWarnings("unchecked")
  public TestNetwork(int numProposers, int numAcceptors, int numLearners) {
    super(numProposers,numAcceptors,numLearners);
    test_totalProcesses=numProposers+numAcceptors+numLearners;
    test_queues=new Stack[test_totalProcesses];
    for(int i=0;i<test_totalProcesses;i++) {
      test_queues[i]=new Stack<String>();
    }
    this.test_numProposers=numProposers;
    this.test_numAcceptors=numAcceptors;
    this.test_numLearners=numLearners;
    channels=new TestChannel[test_totalProcesses];
  }
  
  public int numAcceptors() {
    return test_numAcceptors;
  }

  public int numProposers() {
    return test_numProposers;
  }

  public int numLearners() {
    return test_numLearners;
  }

  /** getChannel returns a communication channel for process processID.
   *
   *   Process ids:
   *   0 through test_numProposers-1 should be Proposers
   *
   *   test_numProposers through numAccepters+numProposes-1 should be Acceptors
   *
   *   numAccepters+numProposes through
   *   numAccepters+numProposes+test_numLearners-1 should be Learners */

  public TestChannel getChannel(int processID) {
    if (processID<0 || processID>= test_totalProcesses) {
      throw new Error("Invalid process ID.");
    }
    TestChannel c=new TestChannel();
    c.test_index=processID;
    channels[processID]=c; 
    c.test_network=this;
    return c;
  }
  
  public void block_channel(int processID,int action){ //action 0->release, 1->block
    if(action==0) 
      channels[processID].releasechannel();   
    if(action==1) 
      channels[processID].blockchannel();   
//    System.out.println("CHANNEL TO BLOCK"+);
  }

  public void terminate_run(){
    for(int i=0;i<test_totalProcesses;i++)
      channels[i].terminate=1;
  }
  
  public void change_DPmode(int mode,int PID){
    for(int i=0;i<test_numProposers;i++){
      channels[i].DP_mode=mode;
      channels[i].requested_DP=PID; 
    }
  }

  public void lossy_channel(int PID,int flag){
    channels[PID].lose_msg=flag;
  }

	public void dup_msg(int PID, int flag) {
		channels[PID].dup_msg = flag;
	}

}

