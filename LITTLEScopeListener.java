// http://stackoverflow.com/questions/15050137/once-grammar-is-complete-whats-the-best-way-to-walk-an-antlr-v4-tree

import org.antlr.v4.runtime.*;

import java.util.*;

public class LITTLEScopeListener extends LITTLEBaseListener {
	private HashMap<String, String> symbols = new HashMap<String, String>();

	@Override
	public void enterProgram(LITTLEParser.ProgramContext ctx) {
		// todo new scope
		System.out.println("Entering program");
	}
	@Override
	public void exitProgram(LITTLEParser.ProgramContext ctx) {
		System.out.println("Exiting program");
		// todo pop scope
	}

	@Override
	public void enterString_decl(LITTLEParser.String_declContext ctx) {
		System.out.println("Entering string decl");
		// store string in current scope
		System.out.println(ctx.getText());
	}
	@Override
	public void exitString_decl(LITTLEParser.String_declContext ctx) {
		System.out.println("Exiting string decl");
	}

	@Override
	public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
		System.out.println("Entering var decl");
		// store var in current scope
		System.out.println(ctx.getText());
		//System.out.println(ctx.var_type());
		//System.out.println(ctx.id_list());
	}
	@Override
	public void exitVar_decl(LITTLEParser.Var_declContext ctx) {
		System.out.println("Exiting var decl");
	}

	@Override
	public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
		System.out.println("Entering param decl");
		// store param in current scope
		System.out.println(ctx.getText());
	}
	@Override
	public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
		System.out.println("Exiting param decl");
	}

	@Override
	public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
		System.out.println("Entering func decl");
		// todo new scope
	}
	@Override
	public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
		// todo pop scope
		System.out.println("Exiting func decl");
	}

	@Override
	public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
		System.out.println("Entering if stmt");
		// todo new scope
	}
	@Override
	public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// todo pop scope
		System.out.println("Exiting if stmt");
	}

	@Override
	public void enterElse_part(LITTLEParser.Else_partContext ctx) {
		System.out.println("Entering else part");
		// todo new scope
	}
	@Override
	public void exitElse_part(LITTLEParser.Else_partContext ctx) {
		// todo pop scope
		System.out.println("Exiting else part");
	}

	@Override
	public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		System.out.println("Entering while stmt");
		// todo new scope
	}
	@Override
	public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// todo pop scope
		System.out.println("Exiting while stmt");
	}

	/*@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println("Entering rule");
		System.out.println(ctx.getText());
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		System.out.println("Exiting rule");
	}*/
}