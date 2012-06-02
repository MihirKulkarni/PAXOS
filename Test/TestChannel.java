package Test;
import Paxos.*;
import java.util.LinkedList;

public class TestChannel extends Channel {
  TestNetwork network;
  int test_index;

  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    synchronized(network.queues[destination]) {
      network.queues[destination].add(message);
//System.out.println(destination);
    }
  }

  /** Receive a message. */

  public String receiveMessage() {
    synchronized(network.queues[test_index]) {
//System.out.println(network.queues[test_index].size()+""+test_index);

      if (!network.queues[test_index].isEmpty())
	return network.queues[test_index].remove();
      else
	return null;
    }
  }

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished() {
    if (test_index<(network.numProposers))
      return true;
    if (test_index>=network.numProposers)
      throw new Error("Non-proposers should not be asking whether they are distinguished");
    return false;
  }

  /** Call this function to register a decision by a learner. */

  public void decide(int decision) {
    if (test_index<(network.numProposers+network.numAcceptors))
      throw new Error("Non-learner should not be deciding a value");

    if (decision>=network.numProposers)
      throw new Error("The decided value was not an initial value...");

    synchronized(network) {
      if (network.decision==-1)
	network.decision=decision;
      else {
	if (network.decision!=decision)
	  System.out.println("Disagreement between Learners");
      }
    }
  }

  /** Call this function to get the initial value for a proposer. */

  public int getInitialValue() {
    if (test_index>=network.numProposers)
      throw new Error("Non-proposers should not be asking for initial value");
    return test_index;
  }
}
