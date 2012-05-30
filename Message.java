public class Message{
  MSG_TYPE msg_type;
  int PID;
  int AID;
  int Pnum;
  int Value;
  public Message(MSG_TYPE mtype,int pid,int aid,int pnum,int val){
    msg_type=mtype;
    PID=pid;
    AID=aid;
    Pnum=pnum;
    Value=val;
  };
  public void parseMessage(String msg){
    String[] split_msg = msg.split("\\.");
    this.msg_type=MSG_TYPE.valueOf(split_msg[0]);
    this.PID=Integer.parseInt(split_msg[1]);
    this.AID=Integer.parseInt(split_msg[2]);
    this.Pnum=Integer.parseInt(split_msg[3]);
    this.Value=Integer.parseInt(split_msg[4]);
  };
  public String createMessage(){
    return this.msg_type+"."+this.PID+"."+this.AID+"."+this.Pnum+"."+this.Value;
  };
}

