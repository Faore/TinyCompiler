/**
 * Created by faore on 4/16/17.
 */
public class IRNode {
    //Structure to hold a 3 address code
    IROp IROp;
    String op1;
    String op2;
    String result;

    String comment;

    private IRNode(IROp IROp, String op1, String op2, String result) {
        this.IROp = IROp;
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
        this.comment = null;
    }

    private IRNode(String comment) {
        this.comment = comment;
        this.IROp = null;
        this.op1 = null;
        this.op2 = null;
        this.result = null;
    }

    private IRNode(IROp IROp) {
        this.comment = null;
        this.IROp = IROp;
        this.op1 = null;
        this.op2 = null;
        this.result = null;
    }

    public static IRNode op(IROp IROp, String op1, String op2, String result) {
        return new IRNode(IROp, op1, op2, result);
    }

    //For jump, label, reads and writes
    public static IRNode ioAndJump(IROp IROp, String result) {
        return new IRNode(IROp, null, null, result);
    }

    //For stores
    public static IRNode store(IROp IROp, String op1, String result) {
        return new IRNode(IROp, op1, null, result);
    }

    //Comments
    public static IRNode comment(String comment) {
        return new IRNode(comment);
    }

    //Link
    public static IRNode single(IROp IROp) {
        return new IRNode(IROp);
    }

    public void print() {
        if(this.comment != null) {
            System.out.println(";" + this.comment);
        } else if(this.IROp == IROp.LINK || this.IROp == IROp.RET) {
            System.out.println(";" + this.IROp);
        }
        else {
            System.out.println(';' + this.IROp.toString() + " "
                    + (this.op1 != null ? this.op1 + " " : "")
                    + (this.op2 != null ? this.op2 + " " : "")
                    + result);
        }
    }
}