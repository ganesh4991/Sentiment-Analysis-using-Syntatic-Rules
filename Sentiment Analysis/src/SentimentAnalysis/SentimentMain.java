package SentimentAnalysis;

import java.io.IOException;
import java.util.Scanner;


public class SentimentMain {

	public static void main(String args[]) throws IOException {
		Scanner o=new Scanner(System.in);
		
		SyntaticEngine engine=new SyntaticEngine();
		
		String s=o.nextLine();
		engine.run(s);
				
		o.close();
	}
}
