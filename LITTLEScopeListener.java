import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

public class LITTLEScopeListener extends LITTLEBaseListener {
	private Stack<String> scopes;
	private LinkedHashMap<String, LinkedHashMap<String, String>> symbol_table;
	private LinkedHashMap<String, String> current_symbol_table;
	
	private int block;
	private boolean did_error;
	private ArrayList<String> error_messages;

	public LITTLEScopeListener() {
		scopes = new Stack<String>();
		symbol_table = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		
		block = 0;
		did_error = false;
		error_messages = new ArrayList<String>();
	}

	private void increment_scope(String name) {
		// make new symbol table for this scope
		current_symbol_table = new LinkedHashMap<String, String>();
		// add it to the hash of symbol tables
		symbol_table.put(name, current_symbol_table);
		// add current scope to the stack
		scopes.push(name);
	}

	private void decrement_scope() {
		// leave current scope
		scopes.pop();
		if (scopes.empty()) {
			// should be empty at the very end
			current_symbol_table = null;
		} else {
			// current scope is now at top of stack
			current_symbol_table = symbol_table.get(scopes.peek());
		}
	}

	@Override
	public void enterProgram(LITTLEParser.ProgramContext ctx) {
		// new GLOBAL scope
		increment_scope("GLOBAL");
	}
	@Override
	public void exitProgram(LITTLEParser.ProgramContext ctx) {
		// leave current scope
		decrement_scope();
	}

	@Override
	public void enterString_decl(LITTLEParser.String_declContext ctx) {
		String type = ctx.getStart().getText();
		if (ctx.getChildCount() > 3) {
			String name = ctx.getChild(1).getText();
			String value = ctx.getChild(3).getText();
			// store string in current scope
			if (!current_symbol_table.containsKey(name)) {
				current_symbol_table.put(name, type + " value " + value);
			} else {
				error_messages.add("DECLARATION ERROR " + name);
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
				if (!current_symbol_table.containsKey(vars[s])) {
					current_symbol_table.put(name, type);
				} else {
					error_messages.add("DECLARATION ERROR " + name);
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
			if (!current_symbol_table.containsKey(name)) {
				current_symbol_table.put(name, type);
			} else {
				error_messages.add("DECLARATION ERROR " + name);
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
			increment_scope(ctx.getChild(2).getText());
		} else {
			System.out.println("ERROR ADDING FUNCTION SCOPE");
		}
	}
	@Override
	public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
		// leave current scope
		decrement_scope();
	}

	@Override
	public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// new BLOCK scope
		increment_scope("BLOCK " + String.valueOf(++block));
	}
	@Override
	public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// leave current scope
		decrement_scope();
	}

	@Override
	public void enterElse_part(LITTLEParser.Else_partContext ctx) {
		// if the else is blank then this doesn't apply
		if (ctx.getChildCount() > 0) {
			// new BLOCK scope
			increment_scope("BLOCK " + String.valueOf(++block));
		}
	}
	@Override
	public void exitElse_part(LITTLEParser.Else_partContext ctx) {
		// if the else was blank then this doesn't apply
		if (ctx.getChildCount() > 0) {
			// leave current scope
			decrement_scope();
		}
	}

	@Override
	public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// new BLOCK scope
		increment_scope("BLOCK " + String.valueOf(++block));
	}
	@Override
	public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// leave current scope
		decrement_scope();
	}

	protected boolean did_error() {
		return this.did_error;
	}

	protected void print_symbol_tables() {
		if (this.error_messages.size() > 0) {
			System.out.println(this.error_messages.get(0));
			this.did_error = true;
		} else {
			boolean is_first = true;

			Iterator<String> table_iterator = symbol_table.keySet().iterator();
			// go through all symbol tables
			while (table_iterator.hasNext()) {
				if (is_first) {
					is_first = false;
				} else {
					System.out.println("\n");
				}

				String table_key = table_iterator.next();
				LinkedHashMap<String, String> sub_table = symbol_table.get(table_key);
				System.out.print("Symbol table ");
				System.out.print(table_key);
				
				Iterator<String> symbol_iterator = sub_table.keySet().iterator();
				// go through all symbols in each table
				while (symbol_iterator.hasNext()) {
					String symbol_key = symbol_iterator.next();
					System.out.print("\nname ");
					System.out.print(symbol_key);
					System.out.print(" type ");
					System.out.print(sub_table.get(symbol_key));
				}
			}
		}
	}
}