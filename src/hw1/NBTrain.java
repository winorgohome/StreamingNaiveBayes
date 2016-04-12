import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


public class NBTrain {

	public static int maxSize = 10000;


	private static HashMap<String, Integer> count;



	private static void init(){
		count = new HashMap<String, Integer>();

	}

	static Vector<String> tokenizeDoc(String cur_doc) {
         String[] words = cur_doc.split("\\s+");
         Vector<String> tokens = new Vector<String>();
         for (int i = 0; i < words.length; i++) {
               words[i] = words[i].replaceAll("\\W", "");
                 if (words[i].length() > 0) {
                	 tokens.add(words[i]);
                 }
         }
         return tokens;
	}

	public static void addToCount(String key, int i){
		if (count.containsKey(key)){
			count.put(key, count.get(key)+i);
		}

		else{
			count.put(key, i);
		}
	}


	public static void process (String s){

		String[] labels;
		Vector<String> features;

		String[] str = s.split("\t");

		labels = str[0].split(",");
		features = tokenizeDoc(str[1]);

		for (String y: labels){
			addToCount("*," + y, 1);
			addToCount("*,*", 1);
			for (String x: features){
				addToCount(x +"," + y, 1);
				}
		}

	}

	public static void printMsg(BufferedWriter out) throws IOException {



		Iterator<Map.Entry<String,Integer>> iterator = count.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,Integer> entry = iterator.next();
			out.write(entry.getKey()+"\t"+entry.getValue()+"\n");


		}
		count.clear();
	}

	public static void main (String[] args) throws IOException{
		init();


		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
	    String s;

	    while ((s = in.readLine()) != null && s.length() != 0){
	    	if (count.size() < maxSize){
	    		process (s);
	    	}

	    	else{
	    		printMsg(out);
	    		count.clear();
	    		process(s);

	    	}


	    }
	    printMsg(out);
		in.close();
		out.close();

	}

}
