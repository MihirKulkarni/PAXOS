import java.util.Collections;
import java.util.Arrays;
class samp{
public static void main(String args[]){
 
// Create an array
String[] array = new String[]{"a", "b", "c"};
 
// Shuffle the elements in the array
Collections.shuffle(Arrays.asList(array));
System.out.println(array[0]+array[1]+array[2]);
}}
