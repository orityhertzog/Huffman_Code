//Orit Hertzog - 300410131, Daniel Barshay - 307833038
package assign_1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import base.Compressor;

public class HuffmanEncoderDecoder implements Compressor{


	public HuffmanEncoderDecoder(){
		
	}
	
	@Override
	public void Compress(String[] input_names, String[] output_names)
	{
		
		System.out.println("compress begins...\n");
		
		try{
			
		FileInputStream input=new FileInputStream(input_names[0]);
		FileOutputStream out=new FileOutputStream(output_names[0]);
	    HashMap<Integer, Integer> frequencyes=frequencyTable(input);
	    input.close();
	    streamingTheDictionary(frequencyes, out);
		HuffmanBTree huffman_tree=creatingTheTree(frequencyes);
		findTheCodingOutFromTheTree(huffman_tree.head,"");
		HashMap<Integer,String> new_huffman_coding_table=creatingNewTable(new HashMap<Integer, String>(),huffman_tree.head);
	  
		
		//creating the compress file
	FileInputStream in=new FileInputStream(input_names[0]);
	compressToFile(in,out,new_huffman_coding_table);
    System.out.println("\ndone compressing.\n\n");
    
		} catch(FileNotFoundException e){
		e.printStackTrace();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
		}

//reading out from the file and building the frequency table
public HashMap<Integer, Integer> frequencyTable(FileInputStream input) throws IOException{
	HashMap<Integer, Integer> hm=new HashMap<Integer, Integer>();
	 int num=input.read();
	while(num != -1){
		if(hm.get(num)==null){
			hm.put(num, 1);
		}
		else{
		hm.put(num, (hm.get(num))+1);
	}
		num=input.read();
	}

	//creating new hashmap who is sorted
	HashMap<Integer,Integer> sortedHM=sortHashMap(hm);

	hm.clear();
	return sortedHM;
}


public HashMap<Integer, Integer> sortHashMap(HashMap<Integer, Integer> unsortedHM){
	LinkedList<Map.Entry<Integer, Integer>> listOfSets=new LinkedList<Map.Entry<Integer, Integer>>(unsortedHM.entrySet());
	Collections.sort(listOfSets, new Comparator<Map.Entry<Integer, Integer>> (){
		public int compare(Map.Entry<Integer, Integer> o1, 
				Map.Entry<Integer, Integer> o2) 
{ 
	return (o1.getValue()).compareTo(o2.getValue()); 
} 
	});
	//creating new sort hashmap
	HashMap<Integer, Integer> sortMap=new LinkedHashMap<Integer, Integer>();
	for (Map.Entry<Integer, Integer> sets : listOfSets) { 
		sortMap.put(sets.getKey(), sets.getValue());
	} 
	listOfSets.clear();
	return sortMap;
	
}


//streaming the frequency table into the compress file 
public void streamingTheDictionary(HashMap<Integer, Integer> freq, FileOutputStream out) throws IOException {
 DataOutputStream dos=new DataOutputStream(out);
 int bytes_amount=0;
dos.writeInt(freq.size());

  for(int key : freq.keySet()){
	  out.write((byte)(key));

  }
 
  for(int value: freq.values()){
	   dos.writeInt(value);
	   bytes_amount+=value;
  }
  dos.writeInt(bytes_amount);
  
}

public HuffmanBTree creatingTheTree(HashMap<Integer, Integer> frequencyes){
	//creating an arraylist out of the frequency table for easy access while building the huffman tree
	LinkedList<Map.Entry<Integer, Integer>> list=new LinkedList<Map.Entry<Integer, Integer>>(frequencyes.entrySet());
	ArrayList<Node> freqList =new ArrayList<Node>(frequencyes.size());
	for (Map.Entry<Integer, Integer> sets : list) { 
		freqList.add(new Node(sets.getKey(), sets.getValue())); 
	} 
	
	HuffmanBTree huffman=new HuffmanBTree();
	while(freqList.size()>1){
		int index=0;
		boolean is_insert=false;
		Node temp=new Node();
		temp.left_chiled = freqList.get(0);
		temp.right_chiled = freqList.get(1);
		temp.frequences = freqList.get(0).frequences+freqList.get(1).frequences;
		freqList.remove(1);
		freqList.remove(0);
		while(!is_insert && index<freqList.size()){
			if(temp.frequences > freqList.get(index).frequences)
				index++;
			else{
				freqList.add(index, temp);
				is_insert=true;
			}
			if(index==freqList.size()){
				freqList.add(temp);
				is_insert=true;
			}
		}
		if(freqList.size()<1){
			huffman=new HuffmanBTree(temp);
		}
		}
	list.clear();
	freqList.clear();
		return huffman;
	}

	//creating recursively the new encoding using the huffman tree
public void findTheCodingOutFromTheTree(Node node, String new_code){

	if(is_leaf(node))
		node.new_coding=new_code;
if(node.left_chiled!=null)
	findTheCodingOutFromTheTree(node.left_chiled, new_code+"0");
	if(node.right_chiled!=null)
		findTheCodingOutFromTheTree(node.right_chiled, new_code+"1");
}

public HashMap<Integer, String> creatingNewTable(HashMap<Integer, String>new_table,Node node) {
       if(is_leaf(node)){
    	   new_table.put(node.ascii_value, node.new_coding);
       }
       if(node.left_chiled!=null)
    	   creatingNewTable(new_table,node.left_chiled);   
	   if(node.right_chiled!= null)
		   creatingNewTable(new_table, node.right_chiled);
	   
		
	return new_table;
}

public void compressToFile(FileInputStream in,FileOutputStream out, HashMap<Integer, String> new_huffman_coding_table) throws IOException {
	ArrayList<String> string_encoding=new ArrayList<String>();
	int file_reader=in.read();
	//reading the original file and insert new encoding stream into string array
	while(file_reader != -1){
		string_encoding.add(new_huffman_coding_table.get(file_reader));
		file_reader=in.read();
	}
	
	int index=0;
	byte mask, bit=0;
	ArrayList<Byte> compress_array=new ArrayList<Byte>();
	//translating from string to byte using bit manipulations
	for(int i=0; i<string_encoding.size();i++){
		for(int j=0; j<string_encoding.get(i).length(); j++ ){
		  if(string_encoding.get(i).charAt(j)=='1'){
			 mask= (byte) (1<<(7-(index % 8)));
			 bit= (byte) (bit | mask);
			 index++;
		    }
		  else{
		index++;
		 }
	 if(index%8==0){
		compress_array.add(bit);
		out.write(compress_array.get(compress_array.size()-1));
		bit=0;
     	}
	  }

	 }
	string_encoding.clear();
        out.close();
		in.close();
	
}


	@Override
	public void Decompress(String[] input_names, String[] output_names)
	{
		try {
			HashMap<Integer, Integer> freqTable=new HashMap<Integer, Integer>();
			FileOutputStream out=new FileOutputStream(output_names[0]);
			FileInputStream in=new FileInputStream(input_names[0]);
			freqTable=reBuiltTheDictionary(in, freqTable);
			HuffmanBTree huffman_tree=creatingTheTree(freqTable);
		    readingOutTheFile(in, huffman_tree.head, out);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public HashMap<Integer, Integer> reBuiltTheDictionary(FileInputStream in,HashMap<Integer, Integer> freqTable ) throws IOException {
		
		ArrayList<Integer> keys=new ArrayList<Integer>();
		ArrayList<Integer> values=new ArrayList<Integer>();
		DataInputStream dis=new DataInputStream(in);
		int key, value, size;
		size=dis.readInt();
		for(int i=0; i<size; i++){
			key=in.read();
			keys.add(key);
		
		}
	
		for(int i=0; i<size; i++){
			value=dis.readInt();
			values.add(value);
		}
	for(int i=0; i<size; i++){
		key=keys.get(i);
		value=values.get(i);
		freqTable.put(key, value);
	}
	freqTable=sortHashMap(freqTable);
	
		return freqTable;
		
	
		
		
		
	}

	public void readingOutTheFile(FileInputStream in,Node huff_node, FileOutputStream out) throws IOException {
		System.out.println("\ndecompressing the file...");
		DataInputStream dis=new DataInputStream(in);
		int bytes=dis.readInt();
		int read=in.read();
		int and, res;
		Node tmp=huff_node;
		while(bytes>0){
			for(int i=0; i<8; i++){
				and= (1<<7-i);
				res= (and & read);
				if(res==0){
					tmp=tmp.left_chiled;
				}
				else if(res != 0){
					tmp=tmp.right_chiled;
				}
			if(is_leaf(tmp)){
				out.write(tmp.ascii_value);
				tmp=huff_node;
				bytes--;
				
			}
			}
			read= in.read();
		}
		System.out.println("\ndone decompressing");
		in.close();
		out.close();
	}
	
	public boolean is_leaf(Node node) {
		if(node != null && node.left_chiled==null && node.right_chiled==null){
			return true;
		}
		else
		return false;
	}

	@Override
	public byte[] CompressWithArray(String[] input_names, String[] output_names)
	{
		return null;
	}

	@Override
	public byte[] DecompressWithArray(String[] input_names, String[] output_names)
	{
		return null;
	}

}
