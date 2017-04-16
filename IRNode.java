/**
 * Created by faore on 4/16/17.
 */
public class IRNode {
    //Structure to hold a 3 address code
    Opcode opcode;
    String op1;
    String op2;
    String result;

    String comment;

    private IRNode(Opcode opcode, String op1, String op2, String result) {
        this.opcode = opcode;
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
        this.comment = null;
    }

    private IRNode(String comment) {
        this.comment = comment;
        this.opcode = null;
        this.op1 = null;
        this.op2 = null;
        this.result = null;
    }

    private IRNode(Opcode opcode) {
        this.comment = null;
        this.opcode = opcode;
        this.op1 = null;
        this.op2 = null;
        this.result = null;
    }

    public static IRNode op(Opcode opcode, String op1, String op2, String result) {
        return new IRNode(opcode, op1, op2, result);
    }

    //For jump, label, reads and writes
    public static IRNode ioAndJump(Opcode opcode, String result) {
        return new IRNode(opcode, null, null, result);
    }

    //For stores
    public static IRNode store(Opcode opcode, String op1, String result) {
        return new IRNode(opcode, op1, null, result);
    }

    //Comments
    public static IRNode comment(String comment) {
        return new IRNode(comment);
    }

    //Link
    public static IRNode link() {
        return new IRNode(Opcode.LINK);
    }

    public void print() {
        if(this.comment != null) {
            System.out.println(";" + this.comment);
        } else if(this.opcode == Opcode.LINK) {
            System.out.println(";" + this.opcode);
        }
        else {
            System.out.println(';' + this.opcode.toString() + " "
                    + (this.op1 != null ? this.op1 + " " : "")
                    + (this.op2 != null ? this.op2 + " " : "")
                    + result);
        }
    }
}