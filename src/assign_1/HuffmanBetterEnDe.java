//orit hertzog 300410131, daniel barshay 307833038
package assign_1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class HuffmanBetterEnDe extends HuffmanEncoderDecoder{

		public HuffmanBetterEnDe()
		{
			
		}

		@Override
		public void Compress(String[] input_names, String[] output_names) {
			System.out.println("compress begins...\n");
			
			try{
			
			FileInputStream input=new FileInputStream(input_names[0]);
			FileOutputStream out=new FileOutputStream(output_names[0]);
		    HashMap<String, Integer> frequencyes=frequencyTable2(input, out);
		    input.close();
		    FileInputStream in=new FileInputStream(input_names[0]);
		    HashMap<String, Integer> wordsFreq=freqWords(in);
		   
		    HashMap<String, Integer> mergeMap=mergeTheMaps(frequencyes,wordsFreq);
		    streamingTheDictionary2(mergeMap, out);
		    HuffmanBTree huffman_tree=creatingTheTree2(mergeMap);
			findTheCodingOutFromTheTree(huffman_tree.head,"");
			HashMap<String,String> new_huffman_coding_table=creatingNewTable2(new HashMap<String, String>(),huffman_tree.head);
			FileInputStream IN=new FileInputStream(input_names[0]);
			compressToFile2(IN,out,new_huffman_coding_table);
			  in.close();
		   out.close();
		   System.out.println("done compressing");
		
			
			
			} catch(FileNotFoundException e){
				e.printStackTrace();	
				}
				catch(IOException e){
					e.printStackTrace();
				}
		}
	

		public HashMap<String, Integer> frequencyTable2(FileInputStream input, FileOutputStream out) throws IOException{
			HashMap<String, Integer> hm=new HashMap<String, Integer>();
			 int a=input.read();
			 char s;
			 String key="";
			while(a != -1){
				s=(char) a;
				key+=s;
				if(hm.get(key)==null){
					hm.put(key, 1);
				}
				else{
				hm.put(key, (hm.get(key))+1);
			}
				key="";
				a=input.read();
				
			}
			
		
			//creating new hashmap who is sorted
			HashMap<String,Integer> sortedHM=sortHashMap2(hm);
		
		hm.clear();
		
			return sortedHM;
		}
		
		public HashMap<String, Integer> sortHashMap2(HashMap<String, Integer> unsortedHM){
			LinkedList<Map.Entry<String, Integer>> listOfSets=new LinkedList<Map.Entry<String, Integer>>(unsortedHM.entrySet());
			Collections.sort(listOfSets, new Comparator<Map.Entry<String, Integer>> (){
				public int compare(Map.Entry<String, Integer> o1, 
						Map.Entry<String, Integer> o2) 
		{ 
			return (o1.getValue()).compareTo(o2.getValue()); 
		} 
			});
			//creating new sort hashmap
			HashMap<String, Integer> sortMap=new LinkedHashMap<String, Integer>();
			for (Map.Entry<String, Integer> sets : listOfSets) { 
				sortMap.put(sets.getKey(), sets.getValue());
			} 
			listOfSets.clear();
			return sortMap;
			
		}
		
		public HashMap<String, Integer> freqWords(FileInputStream input) throws IOException{
			HashMap<String, Integer> hm=new HashMap<String, Integer>();
			 int a=input.read();
			 char s=(char) a;
			 String key="";
		
			while(a != -1){
				while(a != 32 && a!=-1){
				key+=s;
				a=input.read();
				s=(char) a;
			}
				if(hm.get(key)==null){
					hm.put(key, 1);
				}
				else{
				hm.put(key, (hm.get(key))+1);
			}
				key="";
				a=input.read();
				s=(char) a;
			}

			//creating new hashmap who is sorted
			HashMap<String,Integer> sortedHM=sortHashMap2(hm);
			
		hm.clear();
			return sortedHM;
		}
		public HashMap<String, Integer> mergeTheMaps(HashMap<String, Integer> freq,HashMap<String, Integer> wFreq) {
			int index=(int) (wFreq.size()*0.99);
			ArrayList<Map.Entry<String, Integer>> list=new ArrayList<Map.Entry<String, Integer>>(wFreq.entrySet());
			for(int i=index; i<list.size(); i++){
				freq.put(list.get(i).getKey(), list.get(i).getValue());
			}
			freq=sortHashMap2(freq);
	
			return freq;
			
			
		}
		public void streamingTheDictionary2(HashMap<String, Integer> freq, FileOutputStream out) throws IOException, NullPointerException {
			DataOutputStream dos=new DataOutputStream(out);
			dos.writeInt(freq.size());
			  for(int value: freq.values()){
				   dos.writeInt(value);
			  }
			 for(String key : freq.keySet()){
				  dos.writeBytes(key);
				 dos.writeByte(-1);
			  }
			 
			
		}
		
		public HuffmanBTree creatingTheTree2(HashMap<String, Integer> frequencyes){
			//creating an arraylist out of the frequency table for easy access while building the huffman tree
			LinkedList<Map.Entry<String, Integer>> list=new LinkedList<Map.Entry<String, Integer>>(frequencyes.entrySet());
			ArrayList<Node> freqList =new ArrayList<Node>(frequencyes.size());
			for (Map.Entry<String, Integer> sets : list) { 
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
				}
					if(index==freqList.size()){
						freqList.add(temp);
						is_insert=true;
					}
					
				if(freqList.size()<=1){
					huffman=new HuffmanBTree(temp);
				}
			}
			list.clear();
			freqList.clear();
		
				return huffman;
			}
		public HashMap<String, String> creatingNewTable2(HashMap<String, String>new_table,Node node) {
		       if(is_leaf(node)){
		    	   new_table.put(node.value, node.new_coding);
		       }
		       if(node.left_chiled!=null)
		    	   creatingNewTable2(new_table,node.left_chiled);   
			   if(node.right_chiled!= null)
				   creatingNewTable2(new_table, node.right_chiled);
			return new_table;
		}
		
		
		
		public void compressToFile2(FileInputStream in,FileOutputStream out, HashMap<String, String> new_huff_table) throws IOException {
			ArrayList<String> string_encoding=new ArrayList<String>();
		int file_reader=in.read();
		 char s=(char) file_reader;
		
		 String key="";
		while(file_reader != -1){
			while(file_reader != 32 && file_reader != -1 ){
				key+=s;
				file_reader=in.read();
				  s=(char) file_reader;

			}
			if(new_huff_table.containsKey(key)){
				string_encoding.add(new_huff_table.get(key));
			}
			else{
				for(int i=0; i<key.length(); i++){
					String str="";
					str+=key.charAt(i);
					string_encoding.add(new_huff_table.get(str));
				}
			}
			key=""+s;

			string_encoding.add(new_huff_table.get(key));
			file_reader=in.read();
			s=(char) file_reader;
			key="";
		}
		
		int index=0;
		byte mask, bit=0;
		//translating from string to byte using bit manipulations
		for(int i=0; i<string_encoding.size()-1;i++){
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
			
			out.write(bit);
			bit=0;
	     	}
		  }

		 }
		string_encoding.clear();
	        out.close();
			in.close();
		
	}
		@Override
		public void Decompress(String[] input_names, String[] output_names)  {
			System.out.println("decompress begins..");
			try {
				HashMap<String, Integer> freqTable=new HashMap<String, Integer>();
				FileOutputStream out=new FileOutputStream(output_names[0]);
				FileInputStream in=new FileInputStream(input_names[0]);
				freqTable=reBuiltTheDictionary2(in, freqTable, out);
				HuffmanBTree huff_tree=creatingTheTree2(freqTable);
				readingOutTheFile2(in, huff_tree.head, out);
			System.out.println("done decompressing");
			
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}

		public HashMap<String, Integer> reBuiltTheDictionary2(FileInputStream in, HashMap<String, Integer> freqTable, FileOutputStream out) throws IOException {
			ArrayList<String> keys=new ArrayList<String>();
			ArrayList<Integer> values=new ArrayList<Integer>();
			DataInputStream dis=new DataInputStream(in);
			int size=dis.readInt();
			int reader;
			
			for(int i=0; i<size; i++){
				reader=dis.readInt();
					values.add(reader);
					
				}
			String str="";
			char ch;
			int val;
			
			
			
		
	for(int i=0; i<size; i++){
		str="";
	val=in.read();
	ch=(char)val;
	while(val != 255){
		str+=ch;
	val=in.read();
	ch=(char)val;
		}
		keys.add(str);			
	}
	
	
		for(int i=0; i<size; i++){
			String key=keys.get(i);
			int value=values.get(i);
			freqTable.put(key, value);
		}
		freqTable=sortHashMap2(freqTable);
	
			return freqTable;
		
		}

		public void readingOutTheFile2(FileInputStream in,Node huff_node, FileOutputStream out) throws IOException {
			
			PrintStream ps=new PrintStream(out);
			int read=in.read();
			int and, res;
			Node tmp=huff_node;
			while(read != -1){
				for(int i=0; i<8; i++){
					and= (1<<7-i);
					res= (and & read);
					if(res==0){
						tmp=tmp.left_chiled;
					}
					else{
						tmp=tmp.right_chiled;
					}
				if(is_leaf(tmp)){
					ps.print(tmp.value);
					tmp=huff_node;
					
				}
				}
				read= in.read();
			}
			in.close();
			out.close();
		}
		

		@Override
		public byte[] CompressWithArray(String[] input_names,String[] output_names){
		
			return null;
			
		}

		@Override
		public byte[] DecompressWithArray(String[] input_names,
				String[] output_names) {
			
			return super.DecompressWithArray(input_names, output_names);
		}

				}
		
		 
		
	

		 
