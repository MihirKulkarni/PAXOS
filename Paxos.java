import java.lang.Thread;
import java.util.Random;

enum MSG_TYPE {PREPARE, PROMISE, ACCEPT, ACCEPTED};
enum STATE {PREPARE,PROMISE_ACK,PROMISE_NACK,ACCEPT,ACCEPTED};


public class Paxos implements Runnable{
  Network network;
  int MAX_PROPNUM=200;
  int[] DisjointProposalNum=new int[MAX_PROPNUM];
  public Paxos(Network network) {
    this.network=network;
    Random rand = new Random(20120528);
    for(int i=0;i<MAX_PROPNUM;i++){
      DisjointProposalNum[i]=i;
    }
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
  
  public int nextProposalNumber(int PID,int cur_Pnum){
    int i=0;
    while(true){
      if(DisjointProposalNum[i]<cur_Pnum)
	i++;
      else{
        if(DisjointProposalNum[i]%network.numProposers()==PID)
          return DisjointProposalNum[i];
        else
          i++;
      }
      if(i==MAX_PROPNUM)
        return -1;
    }
  }
  public void run(){
    try{
	Thread t= Thread.currentThread();
	Channel c=network.getChannel(Integer.parseInt(t.getName()));
	if(c.index<network.numProposers){
		//Proposer code goes here
		//Proposer Initiated 
		//Send Prepare 
		STATE state=STATE.PROMISE_ACK;
                int cur_Pnum=-1;
		while(true){
			if(c.isDistinguished()){ 
//I am distinguished proposer, if not I shall be idle and querying constantly if I become one.
				while(true){
					switch(state){
						case PREPARE:
							Message m=new Message(MSG_TYPE.PROMISE,c.index,2,nextProposalNumber(c.index,cur_Pnum),-1);
							for(int a=network.numProposers();a<(network.numProposers+network.numAcceptors);a++)
								c.sendMessage(a,m.createMessage());
							break;
						case PROMISE_ACK:
							System.out.println("I have nt recvd ACK yet");
							break;
					}
				Thread.sleep(1000);
			}
			}
			else{
				System.out.println("I am not Distinguished Proposer, so sitting idle");
				Thread.sleep(1000);
			}
	
			
		}
	}
	if(c.index>=network.numProposers && c.index<(network.numProposers+network.numAcceptors)){
		//Acceptor code goes here
		while(true){
	        Message m=new Message(MSG_TYPE.PROMISE,0,0,0,0);
		String msg=c.receiveMessage());
		if(msg!=null){
			msg.parseMessage()
		System.out.println("Acceptor- "+c.index+"received: "+m.createMessage());
//		System.out.println(m.DPID+""+m.AID+""+m.PID);
		Thread.sleep(1000);
		}

	}
	if(c.index>=(network.numProposers+network.numAcceptors)){
		//Learner code goes here
	        System.out.println(c.index+"I am learner");
	}

    }
    catch(Exception e){e.printStackTrace();}
  }
}
