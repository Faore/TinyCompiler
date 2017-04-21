import java.util.*;

public class LITTLETinyCode {
	private SymbolTable symbolTable;
	private LinkedList<IRNode> ir;

	public LITTLETinyCode(SymbolTable symbolTable, LinkedList<IRNode> ir) {
		this.symbolTable = symbolTable;
		this.ir = ir;
	}

	public void printFinalCode() {
		SingleSymbolTable<SymbolTableEntry> global_table = symbolTable.get_scope("GLOBAL");
		ArrayList<String> variables = global_table.getKeys();
		for (String var : variables) {
			SymbolTableEntry entry = global_table.get(var);
			if (entry.type.equals(SymbolTableEntry.TYPE_STRING)) {
				System.out.println("str " + var + " " + entry.value);
			} else {
				System.out.println("var " + var);
			}
		}
		Iterator<IRNode> iterator = ir.iterator();
		while (iterator.hasNext()) {
			String code = irToFinalCode(iterator.next());
			if (code.length() > 0) {
				System.out.println(code);
			}
		}
	}

	private String irToFinalCode(IRNode node) {
		//node.print();
		switch (node.IROp) {
			case ADDI:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"addi " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case ADDF:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"addr " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case SUBI:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"subi " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case SUBF:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"subr " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case MULTI:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"muli " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case MULTF:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"mulr " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case DIVI:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"divi " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case DIVF:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result) + "\n" + 
					"divr " + op_to_register_or_mem(node.op2) + " " + op_to_register_or_mem(node.result);
			case STOREI:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result);
			case STOREF:
				return "move " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.result);
			case READI:
				return "sys readi " + node.result;
			case READF:
				return "sys readr " + node.result;
			case WRITEI:
				return "sys writei " + node.result;
			case WRITEF:
				return "sys writer " + node.result;
			case WRITES:
				return "sys writes " + node.result;
			case JUMP:
				return "jmp " + node.result;
			case LABEL:
				return "label " + node.result;
			case LINK:
				return "";
			case RET:
				return "sys halt\nend";
			case GTI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jgt " + node.result;
			case GEI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jge " + node.result;
			case LTI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jlt " + node.result;
			case LEI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jle " + node.result;
			case NEI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jne " + node.result;
			case EQI:
				return "cmpi " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
					"jeq " + node.result;
			case GTF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jgt " + node.result;
			case GEF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jge " + node.result;
			case LTF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jlt " + node.result;
			case LEF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jle " + node.result;
			case NEF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jne " + node.result;
			case EQF:
				return "cmpf " + op_to_register_or_mem(node.op1) + " " + op_to_register_or_mem(node.op2) + "\n" +
						"jeq " + node.result;
			default:
				return "; TODO TODO TODO TODO TODO TODO TODO TODO " + node.op1 + " " + node.result;
		}
	}

	private String op_to_register_or_mem(String op) {
		if (op.startsWith("$T")) {
			return "r" + op.substring(2);
		}
		return op;
	}
}