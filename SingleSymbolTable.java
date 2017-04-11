import java.util.*;

public class SingleSymbolTable<E> {
	private LinkedHashMap<String, E> table;

	public SingleSymbolTable() {
		this.table = new LinkedHashMap<String, E>();
	}

	public boolean exists(String name) {
		return this.table.containsKey(name);
	}

	public void add(String name, E entry) {
		this.table.put(name, entry);
	}

	public E get(String name) {
		if (exists(name)) {
			return this.table.get(name);
		}
		return null;
	}

	public ArrayList<String> getKeys() {
		ArrayList<String> keys = new ArrayList<String>();

		Iterator<String> table_iterator = table.keySet().iterator();
		while (table_iterator.hasNext()) {
			keys.add(table_iterator.next());
		}

		return keys;
	}
}