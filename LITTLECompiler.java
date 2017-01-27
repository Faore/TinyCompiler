import org.antlr.v4.runtime.*;
import java.util.*;

public class LITTLECompiler {
	public static void main(String[] args) {
		System.out.println();
		
		ANTLRInputStream input = new ANTLRInputStream("test = \"est string\";");
		LITTLELexer lexer = new LITTLELexer(input);
		Vocabulary vocabulary = lexer.getVocabulary();

		Token token = null;
		do {
			token = lexer.nextToken();
			System.out.println(vocabulary.getSymbolicName(token.getType()));
			System.out.println(token.getText());
		} while (token.getType() != Token.EOF);

		// original attempt
		/*CommonTokenStream tokens = new CommonTokenStream(lexer);
		List<Token> token_list = tokens.getTokens();
		
		System.out.print("\t");
		System.out.print(token_list.size());
		System.out.println(" Tokens Found.");
		
		for (Token t : token_list) {
			System.out.println(t.getType());
			System.out.println(t.getText());
		}
		//LITTLEParser parser = new LITTLEParser(tokens);
		//parser.eval();*/
	}
}
