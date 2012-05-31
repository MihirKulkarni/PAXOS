package Paxos;
class samp{
public static void main(String args[]){
System.out.println(5/2);
      Message promise_queue[]=new Message[5]; 
promise_queue[1]=new Message(MSG_TYPE.PREPARE,0,0,0,0); 
   	        
	if(promise_queue[0]!=null||promise_queue[0].AID==1)
		System.out.println("not empty replace");
	else
		System.out.println("empty");

}
}
