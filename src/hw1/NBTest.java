import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class NBTest {

	private static HashSet<String> needed;
	private static HashSet<String> domain;
	private static HashMap<String,Integer> count;
	private static double allY;

    private static int train_vocab_size = 0;

	public static void init(){
		needed = new HashSet<String>();
		domain = new HashSet<String>();
		count = new HashMap<String,Integer>();

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

    public static void process (String s){

        Vector<String> features;

        String[] str = s.split("\t");

        features = tokenizeDoc(str[1]);

        for (String x: features){
            needed.add(x);
        }


    }

	public static void extract (String s){
		String event;
		Integer i;

        event = s.split("\t")[0];

        String token1 = event.split(",")[0];;
        String token2 = event.split(",")[1];

        i = Integer.parseInt(s.split("\t")[1]);

        if (event.contains("TRAIN , VOCAB_SIZE")){
            train_vocab_size = i;

        }

        else{

        if (token1.contains("*")) {
            count.put(event, i);
            //token1 = event.split(",")[0];
            //token2 = event.split(",")[1];

            if (!token2.contains("*")){
                domain.add(token2);
            }
        }


        else{

            for (String x : needed){
                if (token1.contains(x)){
                    //System.out.print(count.size() + "\n");
                    count.put(event, i);
                    break;
		 	    }

			}
		}
        }


	}

	public static void compute (String s, BufferedWriter out) throws IOException{


		Vector<String> features;
		double maxP = Double.NEGATIVE_INFINITY;
		String best = "";
		double prob ;

        int vocab_size = train_vocab_size;

		double qx = 1 / (double) vocab_size;
		double qy = (1 / domain.size());
        double m = vocab_size;

		double YX;
		double classY;

		String[] str = s.split("\t");

		features = tokenizeDoc(str[1]);

        if (count.containsKey("*,*")) {

            allY = count.get("*,*");
        }
        else{
            allY = 0;
        }

		for (String l : domain){

            if (count.containsKey("*,"+l)){
                classY = count.get("*,"+l);
            }
            else {
                classY = 0;
            }


            prob = 0;

			for (String f: features){

                if (count.containsKey(f+","+l)){
                    YX = count.get(f+","+l);
                }
                else{
                    YX = 0;
                }


				prob += Math.log((YX+m*qx)/(classY+m));
			}

            prob += Math.log((classY+m*qy)/(allY+m));


           // out.write(prob+"\t" +addOn +  "\n");

            if (maxP < prob) {
				maxP = prob;
				best = l;
			}
            //else {
            //    out.write (l + "\t" + prob+"\t"+maxP +"\n");

            //}
		}
		out.write (best + "\t" + maxP + "\n");
	}


	public static void main (String[] args) throws IOException{

		init();

		String filename = args[0];



		BufferedReader in = new  BufferedReader(new FileReader(filename));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));


	    String s;

	    while ((s = in.readLine()) != null && s.length() != 0){

	    	process(s);

	    }

//        for (String x : classes){
//           out.write(x+"\n");
//        }
//

		in = new BufferedReader (new InputStreamReader(System.in));


//        out.write(in.readLine());

	    while ((s = in.readLine()) != null && s.length() != 0){


	    	extract(s);

	    }

		in = new BufferedReader(new FileReader(filename));

	    while ((s = in.readLine()) != null && s.length() != 0){

	    	compute(s,out);

	    }
	    in.close();
	    out.close();
	}
}
