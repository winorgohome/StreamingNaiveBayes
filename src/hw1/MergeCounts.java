import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;


public class MergeCounts {

	static String previousKey = null;
	static Integer sumForPreviousKey = 0;


    public static int train_vocab_size = 0;

	public static void OutputPreviousKey(BufferedWriter out) throws IOException{

		 if (previousKey!=null){
			 out.write (previousKey+"\t"+sumForPreviousKey+"\n");
		 }

    }


	public static void main(String[] args) throws IOException{


		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));


		String s;


		while ((s = in.readLine()) != null && s.length() != 0){
			String[] str = s.split("\t");

            String previousToken = "";

			String event = new String (str[0]);
			Integer delta = Integer.parseInt (str[1]);

			if (event.equals(previousKey)) {
				sumForPreviousKey += delta;
			}

		    else{
                String token1 = new String(event.split(",")[0]);

                if (!token1.equals(previousToken)){
                    train_vocab_size++;
                    previousToken = token1;
                }
		    	OutputPreviousKey(out);
		    	previousKey = event;
		    	sumForPreviousKey = delta;
		    }
		}

		OutputPreviousKey(out);
        out.write("TRAIN , VOCAB_SIZE" + "\t" + train_vocab_size+"\n");
		in.close();
		out.close();

	}


}
