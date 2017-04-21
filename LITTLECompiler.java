import org.antlr.v4.gui.Trees;
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

                CommonTokenStream tokenStream = new CommonTokenStream(lexer);

                // instantiate the parser with the token stream
                LITTLEParser parser = new LITTLEParser(tokenStream);

                // update the parser's error listener
                parser.removeErrorListeners();
                parser.addErrorListener(errorListener);

                // clear errors
                errors_syntax = 0;

                ParseTree tree;

                try {
                    // parse the program
                    tree = parser.program();
                    //System.out.println(tree.toStringTree(parser));

                    // Step 2
                    // print out the proper message
					/*if (errors_syntax == 0) {
						System.out.println("Accepted");
					} else {
						System.out.println("Not accepted");	
					}*/
                    if (errors_syntax > 0) {
                        tree = null;
                        System.out.println("Syntax Error");
                    }
                } catch (RecognitionException e) {
                    tree = null;
                    System.out.println("Not accepted");
                }
                //If you want a visual tree to see what the heck you're parsing, uncomment this line:
                //Trees.inspect(tree, parser);
                if (tree != null) {
                    LITTLEScopeListener scopeListener = new LITTLEScopeListener();
                    new ParseTreeWalker().walk(scopeListener, tree);
                    SymbolTable symbol_table = scopeListener.get_symbol_table();
                    //symbol_table.printout();

                    LITTLEIRCodeListener irCodeListener = new LITTLEIRCodeListener(symbol_table);
                    new ParseTreeWalker().walk(irCodeListener, tree);
                    System.out.println(";IR Code:");
                    //Print the IROp
                    irCodeListener.printIRCode();

                    LITTLETinyCode finalCode = new LITTLETinyCode(symbol_table, irCodeListener.getIRCode());
                    finalCode.printFinalCode();
                    //Dump the IRCode references for debugging.
                    //for(String ref : irCodeListener.expressionReferences) {
                    //    System.err.println(ref);
                    //}
                }

            }
        } else {
            System.out.println("No Input File Provided.");
        }
    }
}