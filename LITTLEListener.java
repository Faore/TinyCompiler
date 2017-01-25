// Generated from LITTLE.g4 by ANTLR 4.6
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LITTLEParser}.
 */
public interface LITTLEListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LITTLEParser#tokens}.
	 * @param ctx the parse tree
	 */
	void enterTokens(LITTLEParser.TokensContext ctx);
	/**
	 * Exit a parse tree produced by {@link LITTLEParser#tokens}.
	 * @param ctx the parse tree
	 */
	void exitTokens(LITTLEParser.TokensContext ctx);
}