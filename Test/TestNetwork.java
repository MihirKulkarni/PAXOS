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

  LinkedList<String>[] queues;
//Stack<String>[] queues=new Stack<String>()[];

  /** Create a network with test_numProposers proposes, test_numAcceptors
   * acceptors, and test_numLearners learners.*/

  @SuppressWarnings("unchecked")
  public TestNetwork(int numProposers, int numAcceptors, int numLearners) {
    super(numProposers,numAcceptors,numLearners);
    test_totalProcesses=numProposers+numAcceptors+numLearners;
    queues=new LinkedList[test_totalProcesses];
    for(int i=0;i<test_totalProcesses;i++) {
      queues[i]=new LinkedList<String>();
    }
    this.test_numProposers=numProposers;
    this.test_numAcceptors=numAcceptors;
    this.test_numLearners=numLearners;
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
     
    System.out.println("Setting value for test_index"+processID+""+c.test_index);
    c.test_network=this;
    return c;
  }
}
