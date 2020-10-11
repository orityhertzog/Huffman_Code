//Orit Hertzog - 300410131, Daniel Barshay - 307833038
package assign_1;

public class HuffmanBTree {
Node head;

public HuffmanBTree(Node node){
	head=node;
	head.ascii_value=node.ascii_value;
	head.value=node.value;
	head.left_chiled=node.left_chiled;
	head.right_chiled=node.right_chiled;
	head.frequences=node.frequences;
	head.new_coding=node.new_coding;
}
public HuffmanBTree(){}


}