import org.antlr.v4.runtime.*;

import java.util.*;
import java.io.*;

public class LITTLECompiler {
	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			// Read the entire file supplied into the input stream.
			//	This will take the argument from the command line.
			//	Run as: java LITTLECompiler inputs/loop.micro
			ANTLRInputStream inputStream = null;
			try {
				inputStream = new ANTLRInputStream(new Scanner(new File(args[0])).useDelimiter("\\Z").next());
			} catch (FileNotFoundException e) {
				System.out.println("File '" + args[0] + "' Not Found");
			}
			
			if (inputStream != null) {
				LITTLELexer lexer = new LITTLELexer(inputStream);

				CommonTokenStream tokenStream = new CommonTokenStream(lexer);
				
				LITTLEParser parser = new LITTLEParser(tokenStream);
				
				//parser.eval();

				// get the vocabulary
				/*Vocabulary vocabulary = lexer.getVocabulary();

				// get the first token
				Token token = lexer.nextToken();
				do {
					// print out the token information
					System.out.print("Token Type: ");
					System.out.println(vocabulary.getSymbolicName(token.getType()));
					System.out.print("Value: ");
					System.out.println(token.getText());

					// get the next token
					token = lexer.nextToken();
				} while (token.getType() != Token.EOF);*/
			}
		} else {
			System.out.println("No Input File Provided.");
		}
	}
}