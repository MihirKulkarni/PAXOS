package Test;
import Paxos.*;
import java.util.LinkedList;

public class TestNetwork extends Network {
//	int totalProcesses;
//	int numProposers;
//	int numAcceptors;
//	int numLearners;
//	int decision=-1;

	LinkedList<String>[] queues;

	/** Create a network with numProposers proposes, numAcceptors
	 *    * acceptors, and numLearners learners.*/

	@SuppressWarnings("unchecked")
	public TestNetwork(int numProposers, int numAcceptors, int numLearners) {
		super(numProposers, numAcceptors, numLearners);
		System.out.println("In TestNetwork constructor");
//		super.totalProcesses=numProposers+numAcceptors+numLearners;
//		queues=new LinkedList[totalProcesses];
///		for(int i=0;i<totalProcesses;i++) {
//			queues[i]=new LinkedList<String>();
//		}   
//		this.numProposers=numProposers;
//		this.numAcceptors=numAcceptors;
//		this.numLearners=numLearners;
	}
							  
	public int numAcceptors() {
		System.out.println("derived numAcceptors");
		return super.numAcceptors();
	}

	public int numProposers() {
	  return super.numProposers();
	}

	public int numLearners() {
	  return super.numLearners();
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
 
	public Channel getChannel(int processID) {
//		return (TestChannel)super.getChannel(processID);
		if (processID<0 || processID>= totalProcesses) {
			throw new Error("Invalid process ID.");
		}
		Channel c = new TestChannel();
		c.index = processID;
		c.network = this;
		return c;
	}
}


