package Paxos;
import java.lang.Thread;
import java.util.Random;

enum MSG_TYPE {PREPARE, PROMISE, ACCEPT, ACCEPTED,LEARN,NACK_PROMISE,NACK_ACCEPTED};

public class Paxos implements Runnable{
  Network network;
  int MAX_PROPNUM=20000;
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
        int cur_Pnum=-1;
	while(true){
  	  if(c.isDistinguished()){ 
	  //I am distinguished proposer, if not I shall be idle and querying constantly if I become one.
	    while(c.receiveMessage()!=null){} //Clear all previous messages and start fresh prepare
            cur_Pnum=nextProposalNumber(c.index,cur_Pnum);
	    for(int a=network.numProposers();a<(network.numProposers+network.numAcceptors);a++){
 	      Message m_prepare=new Message(MSG_TYPE.PREPARE,c.index,a,cur_Pnum,-1);
              c.sendMessage(a,m_prepare.createMessage());
	      System.out.println("DP sending Prepare Message to A-"+m_prepare.AID +" with PNum:"+cur_Pnum);
            }
	    while(true){
              if(!c.isDistinguished())
                break;
	      String msg=c.receiveMessage(); // infintely query Message buffer for new message;
//              if(timeout)
// 		should resend prepare
              if(msg!=null){
	        Message m=new Message(MSG_TYPE.PREPARE,0,0,0,0); //create a dummy Message object
   	        m.parseMessage(msg); //parse the received message to the Message object
                switch(m.msg_type){
                  case PROMISE: //if the proposer received promise, send a accept only if it contains Pnum=cur_Pnum.
                    System.out.println("Recvd PROMISE from A-"+m.AID);
                    int value=-1;
                    if(m.Value==-1)
		      value=c.getInitialValue();
		    else
                      value=m.Value;
		    Message m_accept=new Message(MSG_TYPE.ACCEPT,c.index,m.AID,m.Pnum,value);   //what if hacker changes c.index?        
                    if(c.isDistinguished() && m.Pnum==cur_Pnum){                    
                      c.sendMessage(m.AID,m_accept.createMessage());
                      System.out.println("P-"+c.index+"Sent ACCEPT message to A-"+m.AID+" with PNum: "+m.Pnum+""+cur_Pnum+" and value: "+value);
                    }
                    break;
                  case NACK_PROMISE: //proposal rejected coz Accepter has promised higher proposal
                    int next_Pnum=nextProposalNumber(c.index,m.Pnum);
		    if(next_Pnum==-1)
                       System.out.println("Reached MAX_PROPNUM, bail out by throwing exception");
 		    Message m_newprepare=new Message(MSG_TYPE.PREPARE,c.index,m.AID,next_Pnum,-1);  //what if hacker changes c.index?        
                    if(c.isDistinguished() && m.Pnum==cur_Pnum)                    
                      c.sendMessage(m.AID,m_newprepare.createMessage());
                    break;
                  case ACCEPTED: //value has been accepted. Keep quiet.
                    System.out.println("P-"+c.index+"Recvd ACCEPTED from A-"+m.AID+" for PNum:"+m.Pnum+""+cur_Pnum+" and value: "+m.Value);
              	    break;
                  case NACK_ACCEPTED: 
		    System.out.println("P-"+c.index+"Recvd NACK_ACCEPTED from A-"+m.AID+" for PNum: "+m.Pnum+""+cur_Pnum+" and value: "+m.Value+", hence send fresh PREPARE");
		    cur_Pnum=nextProposalNumber(c.index,m.Pnum);
		    if(cur_Pnum==-1)
                       System.out.println("Reached MAX_PROPNUM, bail out by throwing exception");
 		    m_newprepare=new Message(MSG_TYPE.PREPARE,c.index,m.AID,cur_Pnum,m.Value);  //what if hacker changes c.index?        
                    if(c.isDistinguished())                    
                      c.sendMessage(m.AID,m_newprepare.createMessage());
                    break;
                }
              }
              else{ 
	        Thread.sleep(1000); //Message queue empty so sleeping.
              }  
	    }
	  }
	  else{
	    System.out.println("I am not Distinguished Proposer, so sitting idle");
	    Thread.sleep(1000);
	  }
	}
      }
      // Code for Acceptor Behaviour
      if(c.index>=network.numProposers && c.index<(network.numProposers+network.numAcceptors)) {
        // Initialize the highest proposal number received so far
	// and the value if any proposal has been accepted
	int max_pnum = -1;
	int val = -1;
	boolean hasAccepted = false;
	//Acceptor code goes here
	while(true) {
    	  Message m1 = new Message(MSG_TYPE.PROMISE, -1, -1, -1, -1);
	  String msg_st = c.receiveMessage();
	  Message m2;
	  if(msg_st != null) {
	    m1.parseMessage(msg_st);
	    // Check for error conditions
	    // switch case to handle all msg_type
	    switch (m1.msg_type) {
	      case PREPARE:
	/*	This is when the message received is PREPARE
	 *	If the proposal number is greater than highest proposal number this acceptor has seen,
	 *	it will send a PROMISE message.
	 *	It will also handle the case when the acceptor has accepted a value but the proposal number
	 *	is greater, by sending a PROMISE with the Value set to one it has accepted. */
	        if(m1.Pnum >= max_pnum) {				// change max proposal number promised only when it is greater than current
		  max_pnum = m1.Pnum;
  		  m2 = new Message(MSG_TYPE.PROMISE, m1.PID, m1.AID, max_pnum, val);
		  c.sendMessage(m1.PID, m2.createMessage());
	        }
		else {
		  m2 = new Message(MSG_TYPE.NACK_PROMISE, m1.PID, m1.AID, max_pnum, val);		// sending a NACK_PROMISE when proposal's pnum < max_pnum
		  c.sendMessage(m1.PID, m2.createMessage());
		}
		break;
	      case ACCEPT:
		if(m1.Pnum == max_pnum) {
		  if(hasAccepted) {
		    if(val != m1.Value) {
		      Message m4 = new Message(MSG_TYPE.NACK_ACCEPTED, m1.PID, m1.AID, max_pnum, val);
  		      c.sendMessage(m1.PID, m4.createMessage());
		      break;
		    }
		  }
		  else
		    val = m1.Value;
  		    /* Send the accepted value to learner only when it is accepted for the first time
		     * hasAccepted boolean takes care of this */
 		  if(hasAccepted == false) {
		    hasAccepted = true;
		    Message m3 = new Message(MSG_TYPE.LEARN, m1.PID, m1.AID, max_pnum, val);
   	      	    // send message to all learners
		    for(int i=network.numProposers+network.numAcceptors; i<network.totalProcesses; i++) {
			c.sendMessage(i, m3.createMessage());
		    }
		  }
 		  m2 = new Message(MSG_TYPE.ACCEPTED, m1.PID, m1.AID, max_pnum, val);
		}
		else {
		/* Send a PROMISE message with current max_pnum when received Pnum in ACCEPT message is less than max_pnum
		 * which was promised by the acceptor.
		 * If the acceptor has accepted a value, it sends the same or sets val = -1 in the PROMISE message.
		 * Thereby the proposer comes to know that its ACCEPT was rejected and starts with a new PREPARE. */
		  m2 = new Message(MSG_TYPE.NACK_ACCEPTED, m1.PID, m1.AID, max_pnum, val);
		}
		c.sendMessage(m1.PID, m2.createMessage());
	      break;
	    default:
 	      break;
            }
	  }
	}
      }
      if(c.index>=(network.numProposers+network.numAcceptors)){
	//Learner code goes here
	System.out.println("I am learner : "+c.index);
	while(true){
          String msg_learn=c.receiveMessage();
          if(msg_learn!=null){
  	    Message Msg_Learn=new Message(MSG_TYPE.PREPARE,0,0,0,0); //create a dummy Message object
            Msg_Learn.parseMessage(msg_learn);
	    c.decide(Msg_Learn.Value);
            System.out.println("Value Learnt:"+ Msg_Learn.Value);
          }
          Thread.sleep(1000);
        }
      }
    }
    catch(Exception e){e.printStackTrace();}
  }
}
