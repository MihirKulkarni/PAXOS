import java.lang.Thread;
enum MSG_TYPE {PREPARE, PROMISE, ACCEPT, ACCEPTED};

public class Paxos implements Runnable{
  Network network;

  public Paxos(Network network) {
    this.network=network;
  }

  /** This should start your Paxos implementation and return immediately. */
  public void runPaxos() {
//
    System.out.println(network.totalProcesses+" "+network.numProposers()+" "+network.numAcceptors()+" "+network.numLearners()+" "+network.decision);
    for(int i=0;i<network.totalProcesses;i++){
      Thread t= new Thread(this,""+i);
      t.start();
    }
  }
  public void run(){
    try{
      while(true){
	Thread t= Thread.currentThread();
	Channel c=network.getChannel(Integer.parseInt(t.getName()));
	if(c.index<network.numProposers){
		//Proposer code goes here
	        Message m=new Message(MSG_TYPE.PROMISE,0,c.index,2,12,-1);
		c.sendMessage(2,m.createMessage());

	}
	if(c.index>=network.numProposers && c.index<(network.numProposers+network.numAcceptors)){
		//Acceptor code goes here
	        Message m=new Message(MSG_TYPE.PROMISE,0,0,0,0,0);
		m.parseMessage(c.receiveMessage());
		System.out.println(m.createMessage());

		System.out.println(m.DPID+""+m.AID+""+m.PID);

	}
	if(c.index>=(network.numProposers+network.numAcceptors)){
		//Learner code goes here
	        System.out.println(c.index+"I am learner");
	}
  
        Thread.sleep(1000);
      }
    }
    catch(Exception e){e.printStackTrace();}
  }
}
