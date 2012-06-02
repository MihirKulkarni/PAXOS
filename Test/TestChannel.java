package Test;
import Paxos.*;
import java.util.LinkedList;

public class TestChannel extends Channel {
  TestNetwork test_network;
  int test_index;

  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    synchronized(test_network.queues[destination]) {
      test_network.queues[destination].add(message);
//System.out.println(destination);
    }
  }

  /** Receive a message. */

  public String receiveMessage() {
    synchronized(test_network.queues[test_index]) {
//System.out.println(test_network.queues[test_index].size()+""+test_index);

      if (!test_network.queues[test_index].isEmpty())
	return test_network.queues[test_index].remove();
      else
	return null;
    }
  }

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished() {
    if (test_index<(test_network.test_numProposers))
      return true;
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
	  System.out.println("Disagreement between Learners");
      }
    }
  }

  /** Call this function to get the initial value for a proposer. */

  public int getInitialValue() {
    if (test_index>=test_network.test_numProposers)
      throw new Error("Non-proposers should not be asking for initial value");
    return test_index;
  }
}
