import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

public class LITTLEScopeListener extends LITTLEBaseListener {
	private SymbolTable symbol_table;
	
	private int block;

	public LITTLEScopeListener() {
		this.symbol_table = new SymbolTable();
		
		block = 0;
	}

	@Override
	public void enterProgram(LITTLEParser.ProgramContext ctx) {
		// new GLOBAL scope
		this.symbol_table.increment_scope("GLOBAL");
	}
	@Override
	public void exitProgram(LITTLEParser.ProgramContext ctx) {
		// leave current scope
		this.symbol_table.decrement_scope();
	}

	@Override
	public void enterString_decl(LITTLEParser.String_declContext ctx) {
		String type = ctx.getStart().getText();
		if (ctx.getChildCount() > 3) {
			String name = ctx.getChild(1).getText();
			String value = ctx.getChild(3).getText();
			// store string in current scope
			if (!this.symbol_table.exists(name)) {
				this.symbol_table.add(name, new SymbolTableEntry(type, value));
			} else {
				this.symbol_table.error("DECLARATION ERROR " + name);
			}
		}
	}
	//@Override
	//public void exitString_decl(LITTLEParser.String_declContext ctx) { }

	@Override
	public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
		String type = ctx.getStart().getText();
		if (ctx.getChildCount() > 1) {
			String[] vars = ctx.getChild(1).getText().split(",");
			String name;
			for (int s = 0; s < vars.length; s++) {
				name = vars[s];
				// store var in current scope
				if (!this.symbol_table.exists(vars[s])) {
					this.symbol_table.add(name, new SymbolTableEntry(type));
				} else {
					this.symbol_table.error("DECLARATION ERROR " + name);
				}
			}
		}
	}
	//@Override
	//public void exitVar_decl(LITTLEParser.Var_declContext ctx) { }

	private void add_param_decl_to_symbol_table(ParseTree tree) {
		if (tree.getChildCount() > 1) {
			String type = tree.getChild(0).getText();
			String name = tree.getChild(1).getText();
			// store param in current scope
			if (!this.symbol_table.exists(name)) {
				this.symbol_table.add(name, new SymbolTableEntry(type));
			} else {
				this.symbol_table.error("DECLARATION ERROR " + name);
			}
		} else {
			System.out.println("ERROR ADDED PARAM DECL");
		}
	}

	@Override
	public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
		if (ctx.getChildCount() > 0) {
			add_param_decl_to_symbol_table(ctx.getChild(0));

			if (ctx.getChildCount() > 1) {
				ParseTree current = ctx.getChild(1);
				while (current != null) {
					if (current.getChildCount() > 1) {
						add_param_decl_to_symbol_table(current.getChild(1));
						if (current.getChildCount() > 2) {
							current = current.getChild(2);
						} else {
							current = null;
						}
					} else {
						current = null;
					}
				}
			}
		}
	}
	//@Override
	//public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx) { }

	@Override
	public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
		// new function scope
		if (ctx.getChildCount() > 2) {
			this.symbol_table.increment_scope(ctx.getChild(2).getText());
		} else {
			System.out.println("ERROR ADDING FUNCTION SCOPE");
		}
	}
	@Override
	public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
		// leave current scope
		this.symbol_table.decrement_scope();
	}

	@Override
	public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// new BLOCK scope
		this.symbol_table.increment_scope("BLOCK " + String.valueOf(++block));
	}
	@Override
	public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// leave current scope
		this.symbol_table.decrement_scope();
	}

	@Override
	public void enterElse_part(LITTLEParser.Else_partContext ctx) {
		// if the else is blank then this doesn't apply
		if (ctx.getChildCount() > 0) {
			// new BLOCK scope
			this.symbol_table.increment_scope("BLOCK " + String.valueOf(++block));
		}
	}
	@Override
	public void exitElse_part(LITTLEParser.Else_partContext ctx) {
		// if the else was blank then this doesn't apply
		if (ctx.getChildCount() > 0) {
			// leave current scope
			this.symbol_table.decrement_scope();
		}
	}

	@Override
	public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// new BLOCK scope
		this.symbol_table.increment_scope("BLOCK " + String.valueOf(++block));
	}
	@Override
	public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// leave current scope
		this.symbol_table.decrement_scope();
	}

	public SymbolTable get_symbol_table() {
		return this.symbol_table;
	}
}