import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;
import java.io.*;

public class LITTLECompiler {
	protected static int errors_syntax = 0;

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
				// create a custom error listener
				LITTLEErrorListener errorListener = new LITTLEErrorListener();

				LITTLELexer lexer = new LITTLELexer(inputStream);

				//lexer.removeErrorListeners();
				//lexer.addErrorListener(errorListener);

				// Step 1				
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


				// Step 2

				CommonTokenStream tokenStream = new CommonTokenStream(lexer);

				// instantiate the parser with the token stream
				LITTLEParser parser = new LITTLEParser(tokenStream);

				// update the parser's error listener
				parser.removeErrorListeners();
				parser.addErrorListener(errorListener);

				// clear errors
				errors_syntax = 0;
				
				try {
					// parse the program
					ParserRuleContext ctx = parser.program();
					//ParseTree parseTree = parser.program();
					//System.out.println(parser.getNumberOfSyntaxErrors());
					//System.out.println(parseTree.toStringTree(parser));
					
					// print out the proper message
					/*if (errors_syntax == 0) {
						System.out.println("Accepted");
					} else {
						System.out.println("Not accepted");	
					}*/
					if (errors_syntax > 0) {
						System.out.println("Syntax Error");
					}
				} catch (RecognitionException e) {
					System.out.println("Not accepted");
				}

				LITTLEScopeListener scopeListener = new LITTLEScopeListener();

				new ParseTreeWalker().walk(scopeListener, parser.program());


			}
		} else {
			System.out.println("No Input File Provided.");
		}
	}
}