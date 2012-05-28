
public class Message{
  MSG_TYPE msg_type;
  public int DPID;
  int PID;
  int AID;
  int Pnum;
  int Value;
  public Message(MSG_TYPE mtype,int dpid,int pid,int aid,int pnum,int val){
    msg_type=mtype;
    DPID=dpid;
    PID=pid;
    AID=aid;
    Pnum=pnum;
    Value=val;
  };
  public void parseMessage(String msg){
    String[] split_msg = msg.split("\\.");
    this.msg_type=MSG_TYPE.valueOf(split_msg[0]);
    this.DPID=Integer.parseInt(split_msg[1]);
    this.PID=Integer.parseInt(split_msg[2]);
    this.AID=Integer.parseInt(split_msg[3]);
    this.Pnum=Integer.parseInt(split_msg[4]);
    this.Value=Integer.parseInt(split_msg[5]);
  };
  public String createMessage(){
    return this.msg_type+"."+this.DPID+"."+this.PID+"."+this.AID+"."+this.Pnum+"."+this.Value;
  };
}

