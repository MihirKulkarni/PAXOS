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
System.out.println("$$"+PID+"$$"+cur_Pnum);
    int i=0;
    while(true){
      if(DisjointProposalNum[i]<=cur_Pnum)
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
      int myindex=Integer.parseInt(t.getName());
      Channel c=network.getChannel(myindex);
      if(myindex<network.numProposers){
	//Proposer code goes here
	//Proposer Initiated 
	//Send Prepare 
        int cur_Pnum=0;
        int CONSENSUS=0; //1->after reaching 
        Message promise_queue[]=new Message[network.numAcceptors()]; 
        int Acceptor_decisions[]=new int[network.numAcceptors()]; //1->Accept , -1->NACK_Accept
        int guaranteed_value=-1;
        int guaranteed_Pnum=-1;  
	int guaranteed_flag=0;
	while(true){
  	  if(c.isDistinguished()){ 
	  //I am distinguished proposer, if not I shall be idle and querying constantly if I become one.
	    while(c.receiveMessage()!=null){} //Clear all previous messages and start fresh prepare
            cur_Pnum=nextProposalNumber(myindex,cur_Pnum);
	    for(int a=network.numProposers();a<(network.numProposers+network.numAcceptors);a++){
 	      Message m_prepare=new Message(MSG_TYPE.PREPARE,myindex,a,cur_Pnum,-1,-1);
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
	        Message m=new Message(MSG_TYPE.PREPARE,-1,-1,-1,-1,-1); //create a dummy Message object
   	        m.parseMessage(msg); 
//parse the received message to the Message object.
                switch(m.msg_type){
                  case PROMISE: //if the proposer received promise, send a accept only if it contains Pnum=cur_Pnum.
                    if(guaranteed_flag==1 && m.Pnum==guaranteed_Pnum){ //We already got a guarantee from majority so just accept what we already agreed
          	      Message m_accept=new Message(MSG_TYPE.ACCEPT,myindex,m.AID,m.Pnum,-1,guaranteed_value);   
                      c.sendMessage(m.AID,m_accept.createMessage());
                      System.out.println("DP P-"+myindex+" sending ACCEPT msg for past run to A-"+m.AID+" with PNum: "+m.Pnum+""+cur_Pnum+" and value: "+guaranteed_value);
                    }
                    if(m.Pnum==cur_Pnum){   //Ignore Promise for past run, we will anyways get PROMISE for current run.
  		      
		   //if guarantee reached reply back accepted value and cur_Pnum. Set the flag when you start new run.
			//if guarantee reached, f 
				//if PROMISE has no value(-1) ==> send ACCEPT with value and cur_Pnum > PNum
				//if PROMISE has value and PNum<cur_Pnum ==> send ACCEPT with value and cur_Pnum
				//if PROMISE has value and PNum>cur_Pnum ==> start new run and clear local queue and reset guarantee flag, clear promise_queue
			//if guarantee not reached,
				//if no msg in index ==> Add message to queue
				//if no value(-1) and m.Pnum>msg_promQ.Pnum ==> Replace message in queue.
                                //if has value and m.Pnum>=msg_promQ.PNum ==> Replace message in queue.
                                //check queue,
					//if find max val_Pnum with a value, send that value in ACCEPT 
                                        //else generate new value and send ACCEPT to all in majority queue.
                                        //set guarantee flag  
	            
		    //check promise_queue if we got majority and can guarantee
                    if(guaranteed_flag==0){
                      Message msg_promQ=promise_queue[m.AID-network.numProposers()]; 

                      //Add messages to local queue or discard those messages
                      if(msg_promQ==null){
			promise_queue[m.AID-network.numProposers()]=m; //new PROMISE recvd
	                System.out.println("P-"+myindex+": Recvd new PROMISE from A-"+m.AID+" and added to empty queue");
                      }
                      else{
                        if(m.Value==-1 && msg_promQ.Value==-1 && msg_promQ.Pnum<m.Pnum){
                          promise_queue[m.AID-network.numProposers()]=m; //new latest PROMISE recvd to replace existing unknown value.
	                  System.out.println("P-"+myindex+": Recvd new PROMISE from A-"+m.AID+" and added to empty queue");
                        }
                        if(m.Value!=-1 && msg_promQ.Value!=-1 && msg_promQ.Pnum<m.Pnum){
                          promise_queue[m.AID-network.numProposers()]=m; //new latest PROMISE recvd to replace existing known value.
	                  System.out.println("P-"+myindex+": Recvd new PROMISE from A-"+m.AID+" and added to empty queue");
                        }
                        if(m.Value!=-1 && msg_promQ.Value==-1){
                          promise_queue[m.AID-network.numProposers()]=m; //new latest PROMISE recvd with value to replace existing unknown value.
	                  System.out.println("P-"+myindex+": Recvd new PROMISE from A-"+m.AID+" and added to empty queue");
                        }
                      }
		      //Done with adding messages to local queue
  
		      //loop through all message in local queue and check for majority
                      int valid_msg=0;
                      for(int a=network.numProposers();a<(network.numProposers()+network.numAcceptors());a++){
			if(promise_queue[a-network.numProposers()]!=null){
                	  Message m_promise_queue=promise_queue[a-network.numProposers()];//create a dummy Message object
                          valid_msg++;
                          if(m_promise_queue.Value!=-1){
                            if(guaranteed_Pnum<m_promise_queue.Value_Pnum){
                              guaranteed_value=m_promise_queue.Value;
                              guaranteed_Pnum=m_promise_queue.Value_Pnum;
                            }
                          }
                        } 
		      //Determining majority phase ends	
                      } 

		      //If we got majority, go ahead and send ACCEPT for all acceptors who contributed to majority 
                      if(valid_msg>(network.numAcceptors()/2)){ 
                        if(guaranteed_value==-1){
    		          guaranteed_value=c.getInitialValue();
                          guaranteed_Pnum=cur_Pnum;
                        }  
                        if(c.isDistinguished()){                    
  	                  for(int a=network.numProposers();a<(network.numProposers+network.numAcceptors);a++){
			    if(promise_queue[a-network.numProposers()]!=null){
              	              Message m_accept=new Message(MSG_TYPE.ACCEPT,myindex,a,m.Pnum,-1,guaranteed_value);   
                              c.sendMessage(a,m_accept.createMessage());
                              System.out.println("P-"+myindex+"Sent ACCEPT message to A-"+m_accept.AID+" with PNum: "+m.Pnum+""+cur_Pnum+" and value: "+guaranteed_value);
                            }
                          }
                        }
                      guaranteed_flag=1; //flag set 
                      }
		    }  
                    } 
                    break;
                  case NACK_PROMISE: 
 		    if(c.isDistinguished() && guaranteed_Pnum<=m.Pnum){                
	              cur_Pnum=nextProposalNumber(myindex,m.Pnum); //Acceptor shoud send the highest promised Pnum so far. so that Proposer can generate a even higher Pnum.
   		      if(cur_Pnum==-1)
                        System.out.println("Reached MAX_PROPNUM, bail out by throwing exception");
 	              Message m_newprepare=new Message(MSG_TYPE.PREPARE,myindex,m.AID,cur_Pnum,-1,-1);
                      c.sendMessage(m.AID,m_newprepare.createMessage());
	              System.out.println("NACK_Promise recvd so DP sending Prepare Message to A-"+m_newprepare.AID +" with PNum:"+cur_Pnum);
                      promise_queue[m.AID-network.numProposers()]=null;
                    }
                    break;
                  case ACCEPTED: //value has been accepted. Keep quiet.
                    System.out.println("P-"+myindex+"Recvd ACCEPTED from A-"+m.AID+" for PNum:"+m.Pnum+""+cur_Pnum+" and value: "+m.Value);
		    Acceptor_decisions[m.AID-network.numProposers()]=1;
                    int decision_made=0;
		    for(int i=0;i<network.numAcceptors();i++){
                      if(Acceptor_decisions[m.AID-network.numProposers()]==1)
                        decision_made++;
                    }
                    if(decision_made>network.numAcceptors()/2) //now start a new run reset all state variables
                      CONSENSUS=1;
              	    break;
                  case NACK_ACCEPTED: 
		    Acceptor_decisions[m.AID-network.numProposers()]=-1;
                    int rerun=0;
		    for(int i=0;i<network.numAcceptors();i++){
                      if(Acceptor_decisions[m.AID-network.numProposers()]==-1)
                        rerun++;
                    }
                    if(rerun>network.numAcceptors()/2){ //now start a new run reset all state variables
                      CONSENSUS=0; //1->after reaching 
                      promise_queue=new Message[network.numAcceptors()]; 
                      Acceptor_decisions=new int[network.numAcceptors()]; //1->Accept , -1->NACK_Accept
                      guaranteed_value=-1;
                      guaranteed_Pnum=-1;  
	              guaranteed_flag=0;

                      System.out.println("P-"+myindex+"Recvd NACK_ACCEPTED from from majority for PNum: "+m.Pnum+""+cur_Pnum+" and value: "+m.Value+", hence send fresh PREPARE");
		      cur_Pnum=nextProposalNumber(myindex,cur_Pnum);
System.out.println(cur_Pnum+"$$"+myindex);
		      if(cur_Pnum==-1)
                        System.out.println("Reached MAX_PROPNUM, bail out by throwing exception");
 	              for(int a=network.numProposers();a<(network.numProposers+network.numAcceptors);a++){
 	                Message m_prepare=new Message(MSG_TYPE.PREPARE,myindex,a,cur_Pnum,-1,-1);
                        c.sendMessage(a,m_prepare.createMessage());
	                System.out.println("DP sending Prepare Message to A-"+m_prepare.AID +" after NACK_ACCEPT with PNum:"+cur_Pnum);
                      }
                    }
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
      // Code for Acceptor Behaviour
      if(c.index>=network.numProposers && c.index<(network.numProposers+network.numAcceptors)) {
      /* Initialize the highest proposal number received so far
			 * and the value if any proposal has been accepted */

	int max_pnum = -1;								// highest pnum for which acceptor has send a PROMISE
	int val = -1;											// value it has accepted
	int val_pnum = -1;								// corresponding pnum for the accepted value above
	boolean hasAccepted = false;
	//Acceptor code goes here
	while(true) {
	  Message m1 = new Message(MSG_TYPE.PROMISE, -1, -1, -1, -1, -1);
	  String msg_st = c.receiveMessage();
	  Message m2;
	  if(msg_st != null) {
	    m1.parseMessage(msg_st);
	    // switch case to handle all msg_type
	    switch (m1.msg_type) {
	      case PREPARE:
	      /*	This is when the message received is PREPARE.
	       *	If the proposal number is greater than highest proposal number this acceptor has PROMISEd,
	       *	it will send a PROMISE message.
	       *
	       *	It will also handle the case when the acceptor has accepted a value but the proposal number
	       *	is greater than it has PROMISEd, by sending a PROMISE with the val and corresponding val_pnum
	       *	set to one it has accepted. */
              if(m1.Pnum >= max_pnum) {				// change max proposal number promised only when it is greater than current
		max_pnum = m1.Pnum;
		m2 = new Message(MSG_TYPE.PROMISE, m1.PID, m1.AID, max_pnum, val_pnum, val);
		c.sendMessage(m1.PID, m2.createMessage());
	      }
	      else {
		m2 = new Message(MSG_TYPE.NACK_PROMISE, m1.PID, m1.AID, max_pnum, -1, -1);		// sending a NACK_PROMISE when proposal's pnum < max_pnum
		c.sendMessage(m1.PID, m2.createMessage());
	      }
	      break;
	      case ACCEPT:
		if(m1.Pnum == max_pnum) {
 		  val = m1.Value;
		  val_pnum = m1.Pnum;
		  hasAccepted = true;
		  /* Send the accepted value to learner only when it is accepted for the first time
		   * hasAccepted boolean takes care of this */
		  Message m3 = new Message(MSG_TYPE.LEARN, m1.PID, m1.AID, max_pnum, val_pnum, val);
		  /* send message to all learners */
 	          for(int i=network.numProposers+network.numAcceptors; i<network.totalProcesses; i++) {
		    c.sendMessage(i, m3.createMessage());
		  }
		  m2 = new Message(MSG_TYPE.ACCEPTED, m1.PID, m1.AID, max_pnum, val_pnum, val);
		}
		else {
		/* Send a NACK_ACCEPTED message with current max_pnum when received Pnum in ACCEPT message is less than max_pnum
		 * which was promised by the acceptor. */
		  m2 = new Message(MSG_TYPE.NACK_ACCEPTED, m1.PID, m1.AID, max_pnum, -1,-1);
		}
		c.sendMessage(m1.PID, m2.createMessage());
		break;
	      default:
		break;
            }
	  }
	}
      }
      if(myindex>=(network.numProposers+network.numAcceptors)){
	//Learner code goes here
	System.out.println("I am learner : "+myindex);
	while(true){
          String msg_learn=c.receiveMessage();
          if(msg_learn!=null){
  	    Message Msg_Learn=new Message(MSG_TYPE.PREPARE,0,0,0,0,0); //create a dummy Message object
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
