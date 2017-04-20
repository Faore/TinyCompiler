import java.util.*;

public class LITTLETinyCode {
	private LinkedList<IRNode> ir;

	public LITTLETinyCode(LinkedList<IRNode> ir) {
		this.ir = ir;
	}

	public void printFinalCode() {
		Iterator<IRNode> iterator = ir.iterator();
		while (iterator.hasNext()) {
			System.out.println(irToFinalCode(iterator.next()));
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
				// TODO need to define strings and vars in IR, then in this class
				return "sys writes " + node.result;
			case JUMP:
				return "jmp " + node.result;
			case LABEL:
				return "label " + node.result;
			case LINK:
				return "link 1"; // TODO
			case RET:
				return "sys halt\nend";
			/*case IROp.STOREI:
				return "";
			case IROp.STOREI:
				return "";
			case IROp.STOREI:
				return "";
			case IROp.STOREI:
				return "";
			case IROp.STOREI:
				return ""
			case IROp.STOREI:
				return ""
			case IROp.STOREI:
				return "";*/
		}
		return "";
	}

	private String op_to_register_or_mem(String op) {
		if (op.startsWith("$T")) {
			return "r" + op.substring(2);
		}
		return op;
	}
}