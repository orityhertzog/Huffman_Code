//Orit Hertzog - 300410131, Daniel Barshay - 307833038
package assign_1;

public class Node {
Node left_chiled;
Node right_chiled;
int frequences;
int ascii_value;
String value="";
String new_coding="";		

public Node(){
left_chiled=null;
right_chiled=null;	
}
public Node(int ascii, int freq){
	left_chiled=null;
	right_chiled=null;		
	frequences=freq;
	ascii_value=ascii;
}
public Node(String key, int freq){
	left_chiled=null;
	right_chiled=null;		
	frequences=freq;
	value=key;
	
	
	
}



}