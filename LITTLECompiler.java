import org.antlr.v4.runtime.*;
import java.util.*;
import java.io.File;
import java.util.Scanner;

public class LITTLECompiler {
	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			// Read the entire file supplied into the input stream.
			//	This will take the argument from the command line.
			//	Run as: java LITTLECompiler inputs/loop.micro
			ANTLRInputStream input = new ANTLRInputStream(new Scanner(new File(args[0])).useDelimiter("\\Z").next());
			LITTLELexer lexer = new LITTLELexer(input);
			Vocabulary vocabulary = lexer.getVocabulary();

			Token token = null;
			do {
				token = lexer.nextToken();
				// print out the token information
				System.out.print("Token Type: ");
				System.out.println(vocabulary.getSymbolicName(token.getType()));
				System.out.print("Value: ");
				System.out.println(token.getText());
			} while (token.getType() != Token.EOF);
		} else {
			System.out.println("No Input Provided.");
		}
	}
}
