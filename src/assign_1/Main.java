//Orit Hertzog - 300410131, Daniel Barshay - 307833038
package assign_1;
public class Main {

	public static void main(String[] args) {
		System.out.println("**Huffman encoding-decoding assignment**\n");
		
         String[] The_original_File={"C:\\Users\\Orit\\workspace\\Assign_1\\OnTheOrigin.txt"};
         String[] The_compress_File={"C:\\Users\\Orit\\workspace\\Assign_1\\new_compress_file.bin"};
         String[] The_decompress_file={"C:\\Users\\Orit\\workspace\\Assign_1\\new_decompress_file.txt"};
         String[] better_compress_file={"C:\\Users\\Orit\\workspace\\Assign_1\\better_compress.bin"};
         String[] better_decompress_file={"C:\\Users\\Orit\\workspace\\Assign_1\\better_decompress_file.txt"};
        	HuffmanEncoderDecoder HED=new HuffmanEncoderDecoder();
		HED.Compress(The_original_File, The_compress_File);
		HED.Decompress(The_compress_File, The_decompress_file);
		HuffmanBetterEnDe j=new HuffmanBetterEnDe();
		j.Compress(The_original_File, better_compress_file);
		j.Decompress(better_compress_file, better_decompress_file);
	}
	
}
