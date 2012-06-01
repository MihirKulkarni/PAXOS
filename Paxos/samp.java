package Paxos;
import java.util.HashMap; 
class samp{
public static void main(String args[]){
HashMap<Integer, Integer> learn = new HashMap<Integer, Integer>();
learn.put(3,4);
System.out.println(learn.get(3));
learn.put(3,(learn.get(5))+1);
System.out.println(learn.get(3));

 }
}
