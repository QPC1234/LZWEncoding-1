import java.io.*;
import java.util.*;

public class lzwDecoder {
	private HashMap<Integer, String> encodingTable;
	final static int INITIAL_TABLE_SIZE = 128;
	
	public lzwDecoder() {}
	
	public void decode(String inputFileName, String outputFileName) throws IOException {
		BufferedReader br = new BufferedReader (new FileReader(inputFileName));
		encodingTable = new HashMap<Integer, String>();
		PrintWriter pw = new PrintWriter(new File(outputFileName));
		//Fills table with the initial characters
		for(int a = 0; a < INITIAL_TABLE_SIZE; a++) {
			encodingTable.put(a, new String((char)a + ""));
		}
		
		//First iteration, priming variables to loop
		int nextValue = INITIAL_TABLE_SIZE;
		int currentCode = br.read(); //the char being decoded, represents an encoded string
		String previousStr = ""; //represents the most recently printed set of chars
		String currentStr = ""; //represents the set of chars to be printed & added to the hashmap
		
		//Puts first combination (the first two letters) into the hashmap
		currentStr = (char) currentCode +"";
		currentCode = br.read();
		currentStr += (char) currentCode +"";
		encodingTable.put(nextValue, currentStr);
		pw.print(currentStr);
		
		//Resets strings to accept the next encoding
		previousStr = currentStr.substring(1);
		currentStr = "";
		nextValue++;
		
		while(br.ready()) {
			currentCode = br.read();
			if (encodingTable.containsKey(currentCode)) { //if current code is already in the hashmap, add previous characters + 1st letter of current to hashmap
				currentStr = encodingTable.get(currentCode);
				encodingTable.put(nextValue, previousStr + currentStr.substring(0,1));
			} 
			else { //otherwise, add the previous string + its first letter
				currentStr = previousStr + previousStr.substring(0,1);
				encodingTable.put(nextValue, currentStr);
			}
			pw.print(currentStr);//print the current string
			
			//Resets strings to accept the next encoding
			previousStr = encodingTable.get(currentCode);
			currentStr = "";
			nextValue++;
		}
		pw.close();
		br.close();
	}
}