public class SymbolTableEntry {
	public static final String TYPE_STRING = "STRING";
	public static final String TYPE_INT = "INT";
	public static final String TYPE_FLOAT = "FLOAT";

	public static final String NULL = "_NULL_";

	protected String type;
	protected String value;

	public SymbolTableEntry(String type) {
		this.type = type;
		this.value = NULL;
	}

	public SymbolTableEntry(String type, String value) {
		this.type = type;
		this.value = value;
	}
}