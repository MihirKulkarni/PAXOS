package Paxos;
import java.util.LinkedList;

public class Channel {
  Network network;
  int index;

  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    synchronized(network.queues[destination]) {
      network.queues[destination].push(message);
    }
  }

  /** Receive a message. */

  public String receiveMessage() {
    synchronized(network.queues[index]) {
      if (!network.queues[index].isEmpty())
	return network.queues[index].pop();
      else
	return null;
    }
  }

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished() {
    if (index<(network.numProposers))
      return true;
    if (index>=network.numProposers)
      throw new Error("Non-proposers should not be asking whether they are distinguished");
    return false;
  }

  /** Call this function to register a decision by a learner. */

  public void decide(int decision) {
    if (index<(network.numProposers+network.numAcceptors))
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
    if (index>=network.numProposers)
      throw new Error("Non-proposers should not be asking for initial value");
    return index;
  }

}
