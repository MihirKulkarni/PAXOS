package Test;
import Paxos.*;
import java.util.LinkedList;
import java.util.Stack;
public class TestNetwork extends Network {
  int totalProcesses;
  int numProposers;
  int numAcceptors;
  int numLearners;
  int decision=-1;

  LinkedList<String>[] queues;
//Stack<String>[] queues=new Stack<String>()[];

  /** Create a network with numProposers proposes, numAcceptors
   * acceptors, and numLearners learners.*/

  @SuppressWarnings("unchecked")
  public TestNetwork(int numProposers, int numAcceptors, int numLearners) {
    super(numProposers,numAcceptors,numLearners);
    totalProcesses=numProposers+numAcceptors+numLearners;
    queues=new LinkedList[totalProcesses];
    for(int i=0;i<totalProcesses;i++) {
      queues[i]=new LinkedList<String>();
    }
    this.numProposers=numProposers;
    this.numAcceptors=numAcceptors;
    this.numLearners=numLearners;
  }
  
  public int numAcceptors() {
    return numAcceptors;
  }

  public int numProposers() {
    return numProposers;
  }

  public int numLearners() {
    return numLearners;
  }

  /** getChannel returns a communication channel for process processID.
   *
   *   Process ids:
   *   0 through numProposers-1 should be Proposers
   *
   *   numProposers through numAccepters+numProposes-1 should be Acceptors
   *
   *   numAccepters+numProposes through
   *   numAccepters+numProposes+numLearners-1 should be Learners */
 
  public TestChannel getChannel(int processID) {
    if (processID<0 || processID>= totalProcesses) {
      throw new Error("Invalid process ID.");
    }
    TestChannel c=new TestChannel();
    c.test_index=processID;
     
    System.out.println("Setting value for test_index"+processID+""+c.test_index);
    c.network=this;
    return c;
  }
}
