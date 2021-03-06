package Paxos;
public class Message{
  MSG_TYPE msg_type;
  int PID;
  int AID;
  int Pnum;
  int Value_Pnum;
  String Value;
  public Message(MSG_TYPE mtype,int pid,int aid,int pnum,int val_pnum,String val){
    msg_type=mtype;
    PID=pid;
    AID=aid;
    Pnum=pnum;
    Value_Pnum=val_pnum;
    Value=val;

  };
  public void parseMessage(String msg){
    String[] split_msg = msg.split("\\.");
    this.msg_type=MSG_TYPE.valueOf(split_msg[0]);
    this.PID=Integer.parseInt(split_msg[1]);
    this.AID=Integer.parseInt(split_msg[2]);
    this.Pnum=Integer.parseInt(split_msg[3]);
    this.Value_Pnum=Integer.parseInt(split_msg[4]); 
    this.Value=split_msg[5];
  };
  public String createMessage(){
    return this.msg_type+"."+this.PID+"."+this.AID+"."+this.Pnum+"."+this.Value_Pnum+"."+this.Value;
  };
}

