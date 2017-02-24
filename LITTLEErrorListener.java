import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.atn.ATNConfigSet;

import java.util.*;

public class LITTLEErrorListener implements ANTLRErrorListener {
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		System.out.println("LITTLEErrorListener reportAmbiguity");
	}

	public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
		System.out.println("LITTLEErrorListener reportAttemptingFullContext");
	}

	public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		System.out.println("LITTLEErrorListener reportContextSensitivity");
	}

	public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		LITTLECompiler.errors_syntax += 1;
	}
}