import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by faore on 4/16/17.
 */
public class LITTLEIRCodeListener extends LITTLEBaseListener {
    public static LITTLEIRCodeListener instance;
    //Symbol Table is useful.
    protected SymbolTable symbol_table;

    //Store all the IROp
    LinkedList<IRNode> irCode = new LinkedList<IRNode>();
    //Keeps track of all the temporaries we're using, and the type of the data they contain.
    HashMap<String, StoreType> TempTypes = new HashMap<String, StoreType>();

    int nextTemp = 1;

    public static String temp(int i) {
        return "$T" + i;
    }

    public LITTLEIRCodeListener() {
        super();
        LITTLEIRCodeListener.instance = this;
    }

    @Override
    public void enterTokens(LITTLEParser.TokensContext ctx) {
    }

    @Override
    public void exitTokens(LITTLEParser.TokensContext ctx) {
    }

    @Override
    public void enterProgram(LITTLEParser.ProgramContext ctx) {
        //Entering a function. Append a label;
        irCode.add(
                IRNode.comment("Start Program")
        );
    }

    @Override
    public void exitProgram(LITTLEParser.ProgramContext ctx) {
        irCode.add(
                IRNode.comment("End Program")
        );
    }

    @Override
    public void enterId(LITTLEParser.IdContext ctx) {
    }

    @Override
    public void exitId(LITTLEParser.IdContext ctx) {
    }

    @Override
    public void enterPgm_body(LITTLEParser.Pgm_bodyContext ctx) {
    }

    @Override
    public void exitPgm_body(LITTLEParser.Pgm_bodyContext ctx) {
    }

    @Override
    public void enterDecl(LITTLEParser.DeclContext ctx) {
    }

    @Override
    public void exitDecl(LITTLEParser.DeclContext ctx) {
    }

    @Override
    public void enterString_decl(LITTLEParser.String_declContext ctx) {
    }

    @Override
    public void exitString_decl(LITTLEParser.String_declContext ctx) {
    }

    @Override
    public void enterStr(LITTLEParser.StrContext ctx) {
    }

    @Override
    public void exitStr(LITTLEParser.StrContext ctx) {
    }

    @Override
    public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
    }

    @Override
    public void exitVar_decl(LITTLEParser.Var_declContext ctx) {
    }

    @Override
    public void enterVar_type(LITTLEParser.Var_typeContext ctx) {
    }

    @Override
    public void exitVar_type(LITTLEParser.Var_typeContext ctx) {
    }

    @Override
    public void enterAny_type(LITTLEParser.Any_typeContext ctx) {
    }

    @Override
    public void exitAny_type(LITTLEParser.Any_typeContext ctx) {
    }

    @Override
    public void enterId_list(LITTLEParser.Id_listContext ctx) {
    }

    @Override
    public void exitId_list(LITTLEParser.Id_listContext ctx) {
    }

    @Override
    public void enterId_tail(LITTLEParser.Id_tailContext ctx) {
    }

    @Override
    public void exitId_tail(LITTLEParser.Id_tailContext ctx) {
    }

    @Override
    public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
    }

    @Override
    public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
    }

    @Override
    public void enterParam_decl(LITTLEParser.Param_declContext ctx) {
    }

    @Override
    public void exitParam_decl(LITTLEParser.Param_declContext ctx) {
    }

    @Override
    public void enterParam_decl_tail(LITTLEParser.Param_decl_tailContext ctx) {
    }

    @Override
    public void exitParam_decl_tail(LITTLEParser.Param_decl_tailContext ctx) {
    }

    @Override
    public void enterFunc_declarations(LITTLEParser.Func_declarationsContext ctx) {
    }

    @Override
    public void exitFunc_declarations(LITTLEParser.Func_declarationsContext ctx) {
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
    public void enterFunc_body(LITTLEParser.Func_bodyContext ctx) {
    }

    @Override
    public void exitFunc_body(LITTLEParser.Func_bodyContext ctx) {
    }

    @Override
    public void enterStmt_list(LITTLEParser.Stmt_listContext ctx) {
    }

    @Override
    public void exitStmt_list(LITTLEParser.Stmt_listContext ctx) {
    }

    @Override
    public void enterStmt(LITTLEParser.StmtContext ctx) {
    }

    @Override
    public void exitStmt(LITTLEParser.StmtContext ctx) {
    }

    @Override
    public void enterBase_stmt(LITTLEParser.Base_stmtContext ctx) {
    }

    @Override
    public void exitBase_stmt(LITTLEParser.Base_stmtContext ctx) {
    }

    @Override
    public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
    }

    @Override
    public void exitAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
    }

    @Override
    public void enterAssign_expr(LITTLEParser.Assign_exprContext ctx) {
    }

    @Override
    public void exitAssign_expr(LITTLEParser.Assign_exprContext ctx) {
        //Assign the result with the last temporary
        String id = ctx.id().getText();

        if (symbol_table.get_scope("GLOBAL").get(id).type.equals("INT")) {
            irCode.add(
                    IRNode.store(IROp.STOREI, ("$T" + (nextTemp - 1)), id, StoreType.INT)
            );
        } else if (symbol_table.get_scope("GLOBAL").get(id).type.equals("FLOAT")) {
            irCode.add(
                    IRNode.store(IROp.STOREF, ("$T" + (nextTemp - 1)), id, StoreType.FLOAT)
            );
        } else {
            System.err.println("Failed to generate code for expression: " + ctx.getText());
        }

    }

    @Override
    public void enterRead_stmt(LITTLEParser.Read_stmtContext ctx) {
    }

    @Override
    public void exitRead_stmt(LITTLEParser.Read_stmtContext ctx) {
    }

    @Override
    public void enterWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
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
        System.out.println("Parsed var: " + ctx.id_list().id().getText());
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
    public void exitWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
    }

    @Override
    public void enterReturn_stmt(LITTLEParser.Return_stmtContext ctx) {
    }

    @Override
    public void exitReturn_stmt(LITTLEParser.Return_stmtContext ctx) {
    }

    @Override
    public void enterExpr(LITTLEParser.ExprContext ctx) {
    }

    @Override
    public void exitExpr(LITTLEParser.ExprContext ctx) {
        //Check if the child expression prefix exists, if so, perform its operation.
        if(ctx.expr_prefix() != null && !ctx.expr_prefix().getText().isEmpty()) {
            //The result of the expression prefix should be stored 2 below (since factors can only be primaries)
            if(ctx.expr_prefix().addop().getText().equals("+")) {
                //If doing math on int and int, store as int, otherwise store float.
                if(TempTypes.get("$T" + (nextTemp - 2)) == StoreType.INT && TempTypes.get("$T" + (nextTemp - 1)) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.ADDI, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.ADDF, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                }
                nextTemp++;
            } else {
                //If doing math on int and int, store as int, otherwise store float.
                if(TempTypes.get("$T" + (nextTemp - 2)) == StoreType.INT && TempTypes.get("$T" + (nextTemp - 1)) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.SUBI, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.SUBF, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                }
                nextTemp++;
            }
        }
    }

    @Override
    public void enterExpr_prefix(LITTLEParser.Expr_prefixContext ctx) {
    }

    @Override
    public void exitExpr_prefix(LITTLEParser.Expr_prefixContext ctx) {
        //Check if the child expression prefix exists, if so, perform its operation.
        if(ctx.expr_prefix() != null && !ctx.expr_prefix().getText().isEmpty()) {
            //The result of the expression prefix should be stored 2 below (since factors can only be primaries)
            if(ctx.expr_prefix().addop().getText().equals("+")) {
                //If doing math on int and int, store as int, otherwise store float.
                if(TempTypes.get("$T" + (nextTemp - 2)) == StoreType.INT && TempTypes.get("$T" + (nextTemp - 1)) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.ADDI, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.ADDF, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                }
                nextTemp++;
            } else {
                //If doing math on int and int, store as int, otherwise store float.
                if(TempTypes.get("$T" + (nextTemp - 2)) == StoreType.INT && TempTypes.get("$T" + (nextTemp - 1)) == StoreType.INT) {
                    irCode.add(
                            IRNode.op(IROp.SUBI, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                } else {
                    irCode.add(
                            IRNode.op(IROp.SUBF, temp(nextTemp - 2), temp(nextTemp - 1), temp(nextTemp))
                    );
                }
                nextTemp++;
            }
        }
    }

    @Override
    public void enterFactor(LITTLEParser.FactorContext ctx) {
    }

    @Override
    public void exitFactor(LITTLEParser.FactorContext ctx) {
    }

    @Override
    public void enterFactor_prefix(LITTLEParser.Factor_prefixContext ctx) {
    }

    @Override
    public void exitFactor_prefix(LITTLEParser.Factor_prefixContext ctx) {
    }

    @Override
    public void enterPostfix_expr(LITTLEParser.Postfix_exprContext ctx) {
    }

    @Override
    public void exitPostfix_expr(LITTLEParser.Postfix_exprContext ctx) {
    }

    @Override
    public void enterCall_expr(LITTLEParser.Call_exprContext ctx) {
    }

    @Override
    public void exitCall_expr(LITTLEParser.Call_exprContext ctx) {
    }

    @Override
    public void enterExpr_list(LITTLEParser.Expr_listContext ctx) {
    }

    @Override
    public void exitExpr_list(LITTLEParser.Expr_listContext ctx) {
    }

    @Override
    public void enterExpr_list_tail(LITTLEParser.Expr_list_tailContext ctx) {
    }

    @Override
    public void exitExpr_list_tail(LITTLEParser.Expr_list_tailContext ctx) {
    }

    @Override
    public void enterPrimary(LITTLEParser.PrimaryContext ctx) {
    }

    @Override
    public void exitPrimary(LITTLEParser.PrimaryContext ctx) {
        if (ctx.id() != null) {
            if (symbol_table.get_scope("GLOBAL").get(ctx.id().getText()).type.equals("INT")) {
                //Storing an integer variable
                irCode.add(
                        IRNode.store(IROp.STOREI, ctx.id().getText(), "$T" + nextTemp, StoreType.INT)
                );
                //Because we used up a temp, increment it.
                nextTemp++;
            }
        }
        if (ctx.INTLITERAL() != null) {
            //Storing an integer
            irCode.add(
                    IRNode.store(IROp.STOREI, ctx.INTLITERAL().getText(), "$T" + nextTemp, StoreType.INT)
            );
            //Because we used up a temp, increment it.
            nextTemp++;
        }
        if (ctx.FLOATLITERAL() != null) {
            //Storing a float
            irCode.add(
                    IRNode.store(IROp.STOREF, ctx.FLOATLITERAL().getText(), "$T" + nextTemp, StoreType.FLOAT)
            );
            //Because we used up a temp, increment it.
            nextTemp++;
        }
        //Primaries with expressions should parse themselves. I hope.
    }


    @Override
    public void enterAddop(LITTLEParser.AddopContext ctx) {
    }

    @Override
    public void exitAddop(LITTLEParser.AddopContext ctx) {
    }

    @Override
    public void enterMulop(LITTLEParser.MulopContext ctx) {
    }

    @Override
    public void exitMulop(LITTLEParser.MulopContext ctx) {
    }

    @Override
    public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
    }

    @Override
    public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
    }

    @Override
    public void enterElse_part(LITTLEParser.Else_partContext ctx) {
    }

    @Override
    public void exitElse_part(LITTLEParser.Else_partContext ctx) {
    }

    @Override
    public void enterCond(LITTLEParser.CondContext ctx) {
    }

    @Override
    public void exitCond(LITTLEParser.CondContext ctx) {
    }

    @Override
    public void enterCompop(LITTLEParser.CompopContext ctx) {
    }

    @Override
    public void exitCompop(LITTLEParser.CompopContext ctx) {
    }

    @Override
    public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
    }

    @Override
    public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    public void printIRCode() {
        for (IRNode node : irCode) {
            node.print();
        }
    }
}
