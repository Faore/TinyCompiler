import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * Created by faore on 4/16/17.
 */
public class LITTLEIRCodeListener extends LITTLEBaseListener {
    public static LITTLEIRCodeListener instance;
    //Symbol Table is useful.
    protected SymbolTable symbol_table;

    //Store all the IROp
    LinkedList<IRNode> irCode;
    //Keeps track of all the temporaries we're using, and the type of the data they contain.
    HashMap<String, StoreType> TempTypes;
    //Expression load list so we can do things like ADDI a b $T1 instead of using temporaries for everything.
    Stack<String> expressionReferences;

    int nextTemp = 1;

    public static String temp(int i) {
        return "$T" + i;
    }

    public LITTLEIRCodeListener(SymbolTable symbol_table) {
        super();

        LITTLEIRCodeListener.instance = this;

        this.symbol_table = symbol_table;
        expressionReferences = new Stack<String>();
        irCode = new LinkedList<IRNode>();
        TempTypes = new HashMap<String, StoreType>();

        for (String key : symbol_table.get_scope("GLOBAL").getKeys()) {
            if(symbol_table.get_scope("GLOBAL").get(key).type.equals("INT")) {
                TempTypes.put(key, StoreType.INT);
            }
            else if(symbol_table.get_scope("GLOBAL").get(key).type.equals("FLOAT")) {
                TempTypes.put(key, StoreType.INT);
            }
            else if(symbol_table.get_scope("GLOBAL").get(key).type.contains("STRING")) {
                TempTypes.put(key, StoreType.INT);
            }
            else {
                System.err.println("Failed to add variable key " + key + "to TempTypes.");
            }
        }
    }

    @Override
    public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        //Label the function.
        irCode.add(
                IRNode.ioAndJump(IROp.LABEL, ctx.id().getText())
        );
        //Link to move the frame pointer.
        irCode.add(
                IRNode.single(IROp.LINK)
        );
    }

    @Override
    public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
        //Unlink/Return
        irCode.add(
                IRNode.single(IROp.RET)
        );
    }

    @Override
    public void exitAssign_expr(LITTLEParser.Assign_exprContext ctx) {
        //Assign the result with the last temporary
        String id = ctx.id().getText();

        if (symbol_table.get_scope("GLOBAL").get(id).type.equals("INT")) {
            irCode.add(
                    IRNode.store(IROp.STOREI, expressionReferences.peek(), id, StoreType.INT)
            );
        } else if (symbol_table.get_scope("GLOBAL").get(id).type.equals("FLOAT")) {
            irCode.add(
                    IRNode.store(IROp.STOREF, expressionReferences.peek(), id, StoreType.FLOAT)
            );
        } else {
            System.err.println("Failed to generate code for expression: " + ctx.getText());
        }

    }

    @Override
    public void exitWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
        //Deal with the first item.
        String firstId = ctx.id_list().id().getText();
        if (symbol_table.get_scope("GLOBAL").get(firstId).type.equals("INT")) {
            //Write an integer
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITEI, firstId)
            );

        }
        if (symbol_table.get_scope("GLOBAL").get(firstId).type.equals("FLOAT")) {
            //Write a float
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITEF, firstId)
            );
        }
        if (symbol_table.get_scope("GLOBAL").get(firstId).type.equals("STRING")) {
            //Write a string
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITES, firstId)
            );
        }
        LITTLEParser.Id_tailContext currentContext = ctx.id_list().id_tail();
        while (currentContext.id() != null) {
            String id = currentContext.id().getText();
            if (symbol_table.get_scope("GLOBAL").get(id).type.equals("INT")) {
                //Write an integer
                irCode.add(
                        IRNode.ioAndJump(IROp.WRITEI, id)
                );
            } else if (symbol_table.get_scope("GLOBAL").get(id).type.equals("FLOAT")) {
                //Write a float
                irCode.add(
                        IRNode.ioAndJump(IROp.WRITEF, id)
                );

            } else if (symbol_table.get_scope("GLOBAL").get(id).type.contains("STRING")) {
                //Write a string
                irCode.add(
                        IRNode.ioAndJump(IROp.WRITES, id)
                );

            } else {
                System.err.println("Encountered unrecognized type to WRITE.");
            }
            //Change the context.
            currentContext = currentContext.id_tail();
        }
    }

    @Override
    public void exitExpr(LITTLEParser.ExprContext ctx) {
        handleExpressionPrefix(ctx.expr_prefix());
    }

    @Override
    public void exitExpr_prefix(LITTLEParser.Expr_prefixContext ctx) {
        handleExpressionPrefix(ctx.expr_prefix());
    }

    @Override
    public void exitFactor(LITTLEParser.FactorContext ctx) {
        handleFactorPrefix(ctx.factor_prefix());
    }

    @Override
    public void exitFactor_prefix(LITTLEParser.Factor_prefixContext ctx) {
        handleFactorPrefix(ctx.factor_prefix());
    }

    private void handleExpressionPrefix(LITTLEParser.Expr_prefixContext expr_prefix) {
        //Check if the child expression prefix exists, if so, perform its operation.
        if(expr_prefix != null && !expr_prefix.getText().isEmpty()) {
            //The result of the expression prefix should be stored 2 below (since factors can only be primaries)
            if(expr_prefix.addop().getText().equals("+")) {
                //If doing math on int and int, store as int, otherwise store float.
                if(getType(expressionReferences.get(expressionReferences.size() - 2)) == StoreType.INT && getType(expressionReferences.peek()) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.ADDI, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.INT)
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.ADDF, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.FLOAT)
                    );
                }
                expressionReferences.pop();
                expressionReferences.pop();
                expressionReferences.add(temp(nextTemp));
                nextTemp++;
            } else {
                //If doing math on int and int, store as int, otherwise store float.
                if(getType(expressionReferences.get(expressionReferences.size() - 2)) == StoreType.INT && getType(expressionReferences.peek()) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.SUBI, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.INT)
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.SUBF, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.FLOAT)
                    );
                }
                expressionReferences.pop();
                expressionReferences.pop();
                expressionReferences.add(temp(nextTemp));
                nextTemp++;
            }
        }
    }

    private void handleFactorPrefix(LITTLEParser.Factor_prefixContext factor_prefix) {
        //Check if the child expression prefix exists, if so, perform its operation.
        if(factor_prefix != null && !factor_prefix.getText().isEmpty()) {
            //The result of the expression prefix should be stored 2 below (since factors can only be primaries)
            if(factor_prefix.mulop().getText().equals("*")) {
                //If doing math on int and int or int and float, store as int, otherwise store float.
                if(getType(expressionReferences.get(expressionReferences.size() - 2)) == StoreType.INT || getType(expressionReferences.peek()) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.MULTI, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.INT)
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.MULTF, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.FLOAT)
                    );
                }
                expressionReferences.pop();
                expressionReferences.pop();
                expressionReferences.add(temp(nextTemp));
                nextTemp++;
            } else {
                //If doing math on int and int, store as int, otherwise store float.
                if(getType(expressionReferences.get(expressionReferences.size() - 2)) == StoreType.INT && getType(expressionReferences.peek()) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.DIVI, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.INT)
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.DIVF, expressionReferences.get(expressionReferences.size() - 2), expressionReferences.peek(), temp(nextTemp), StoreType.FLOAT)
                    );
                }
                expressionReferences.pop();
                expressionReferences.pop();
                expressionReferences.add(temp(nextTemp));
                nextTemp++;
            }
        }
    }

    private StoreType getType(String ref) {
        if(Character.isDigit(ref.charAt(0))) {
            //This isn't a variable reference, its a direct value.
            if(ref.contains(".")) {
                return StoreType.FLOAT;
            } else {
                return StoreType.INT;
            }
        } else {
            return TempTypes.get(ref);
        }
    }

    @Override
    public void exitPrimary(LITTLEParser.PrimaryContext ctx) {
        if (ctx.id() != null) {
            if (symbol_table.get_scope("GLOBAL").get(ctx.id().getText()).type.equals("INT")) {
                //Don't need to store a variable into a temporary when using it as a primary,
                //just add a reference to it.
                expressionReferences.add(ctx.id().getText());
            }
        }
        if (ctx.INTLITERAL() != null) {
            expressionReferences.add(ctx.INTLITERAL().getText());
        }
        if (ctx.FLOATLITERAL() != null) {
            expressionReferences.add(ctx.FLOATLITERAL().getText());
        }
        //Primaries with expressions should parse themselves. I hope.
    }

    public void printIRCode() {
        for (IRNode node : irCode) {
            node.print();
        }
    }
}
