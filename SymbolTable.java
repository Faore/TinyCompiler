import java.util.*;

public class SymbolTable {
	private Stack<String> scopes;

	private SingleSymbolTable<SingleSymbolTable<SymbolTableEntry>> symbol_table;
	private SingleSymbolTable<SymbolTableEntry> current_symbol_table;

	private boolean did_error;
	private ArrayList<String> error_messages;

	public SymbolTable() {
		this.scopes = new Stack<String>();
		this.symbol_table = new SingleSymbolTable<SingleSymbolTable<SymbolTableEntry>>();

		this.did_error = false;
		this.error_messages = new ArrayList<String>();
	}

	public void increment_scope(String name) {
		// make new symbol table for this scope
		this.current_symbol_table = new SingleSymbolTable<SymbolTableEntry>();
		// add it to the hash of symbol tables
		this.symbol_table.add(name, this.current_symbol_table);
		// add current scope to the stack
		this.scopes.push(name);
	}

	public void decrement_scope() {
		// leave current scope
		this.scopes.pop();
		if (this.scopes.empty()) {
			// should be empty at the very end
			this.current_symbol_table = null;
		} else {
			// current scope is now at top of stack
			this.current_symbol_table = this.symbol_table.get(this.scopes.peek());
		}
	}

	// pass responsibilty to SingleSymbolTable
	public boolean exists(String name) {
		return this.current_symbol_table.exists(name);
	}

	// pass responsibilty to SingleSymbolTable
	public void add(String name, SymbolTableEntry entry) {
		this.current_symbol_table.add(name, entry);
	}

	// pass responsibilty to SingleSymbolTable
	public SymbolTableEntry get(String name) {
		return this.current_symbol_table.get(name);
	}

	public void error(String message) {
		this.error_messages.add(message);
		this.did_error = true;
	}

	public boolean did_error() {
		return this.did_error;
	}

	public void printout() {
		if (this.did_error) {
			System.out.println(this.error_messages.get(0));
		} else {
			boolean is_first = true;

			ArrayList<String> table_keys = this.symbol_table.getKeys();
			// go through all symbol tables
			for (String table_key : table_keys) {
				if (is_first) {
					is_first = false;
				} else {
					System.out.println("\n");
				}

				SingleSymbolTable<SymbolTableEntry> sub_table = symbol_table.get(table_key);
				System.out.print("Symbol table ");
				System.out.print(table_key);
				
				ArrayList<String> sub_table_keys = sub_table.getKeys();
				// go through all symbols in each table
				for (String symbol_key : sub_table_keys) {
					SymbolTableEntry sub_table_entry = sub_table.get(symbol_key);
					System.out.print("\nname ");
					System.out.print(symbol_key);
					System.out.print(" type ");
					System.out.print(sub_table_entry.type);
					if (!sub_table_entry.value.equals(SymbolTableEntry.NULL)) {
						System.out.print(" value ");
						System.out.print(sub_table_entry.value);
					}
				}
			}
		}
	}
}