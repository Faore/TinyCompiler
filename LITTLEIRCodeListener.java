import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by faore on 4/16/17.
 */
public class LITTLEIRCodeListener extends LITTLEBaseListener {
    //Symbol Table is useful.
    LinkedHashMap<String, LinkedHashMap<String, String>> symbol_table;

    //Store all the IROp
    LinkedList<IRNode> irCode = new LinkedList<IRNode>();
    int nextTemp = 1;

    //Use to store IR Codes part of statements (expression parts)
    Stack<LinkedList<IRNode>> temporaryCodes = new Stack<LinkedList<IRNode>>();
    
    @Override public void enterTokens(LITTLEParser.TokensContext ctx) { }
    
    @Override public void exitTokens(LITTLEParser.TokensContext ctx) { }
    
    @Override public void enterProgram(LITTLEParser.ProgramContext ctx) {
        //Entering a function. Append a label;
        irCode.add(
                IRNode.comment("Start Program")
        );
    }
    
    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
        irCode.add(
                IRNode.comment("End Program")
        );
    }
    
    @Override public void enterId(LITTLEParser.IdContext ctx) { }
    
    @Override public void exitId(LITTLEParser.IdContext ctx) { }
    
    @Override public void enterPgm_body(LITTLEParser.Pgm_bodyContext ctx) { }
    
    @Override public void exitPgm_body(LITTLEParser.Pgm_bodyContext ctx) { }
    
    @Override public void enterDecl(LITTLEParser.DeclContext ctx) { }
    
    @Override public void exitDecl(LITTLEParser.DeclContext ctx) { }
    
    @Override public void enterString_decl(LITTLEParser.String_declContext ctx) { }
    
    @Override public void exitString_decl(LITTLEParser.String_declContext ctx) { }
    
    @Override public void enterStr(LITTLEParser.StrContext ctx) { }
    
    @Override public void exitStr(LITTLEParser.StrContext ctx) { }
    
    @Override public void enterVar_decl(LITTLEParser.Var_declContext ctx) { }
    
    @Override public void exitVar_decl(LITTLEParser.Var_declContext ctx) { }
    
    @Override public void enterVar_type(LITTLEParser.Var_typeContext ctx) { }
    
    @Override public void exitVar_type(LITTLEParser.Var_typeContext ctx) { }
    
    @Override public void enterAny_type(LITTLEParser.Any_typeContext ctx) { }
    
    @Override public void exitAny_type(LITTLEParser.Any_typeContext ctx) { }
    
    @Override public void enterId_list(LITTLEParser.Id_listContext ctx) { }
    
    @Override public void exitId_list(LITTLEParser.Id_listContext ctx) { }
    
    @Override public void enterId_tail(LITTLEParser.Id_tailContext ctx) { }
    
    @Override public void exitId_tail(LITTLEParser.Id_tailContext ctx) { }
    
    @Override public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx) { }
    
    @Override public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx) { }
    
    @Override public void enterParam_decl(LITTLEParser.Param_declContext ctx) { }
    
    @Override public void exitParam_decl(LITTLEParser.Param_declContext ctx) { }
    
    @Override public void enterParam_decl_tail(LITTLEParser.Param_decl_tailContext ctx) { }
    
    @Override public void exitParam_decl_tail(LITTLEParser.Param_decl_tailContext ctx) { }
    
    @Override public void enterFunc_declarations(LITTLEParser.Func_declarationsContext ctx) { }
    
    @Override public void exitFunc_declarations(LITTLEParser.Func_declarationsContext ctx) { }
    
    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        //Label the function.
        irCode.add(
                IRNode.ioAndJump(IROp.LABEL, ctx.id().getText())
        );
        //Link to move the frame pointer.
        irCode.add(
                IRNode.single(IROp.LINK)
        );
    }
    
    @Override public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
        //Unlink/Return
        irCode.add(
                IRNode.single(IROp.RET)
        );
    }
    
    @Override public void enterFunc_body(LITTLEParser.Func_bodyContext ctx) { }
    
    @Override public void exitFunc_body(LITTLEParser.Func_bodyContext ctx) { }
    
    @Override public void enterStmt_list(LITTLEParser.Stmt_listContext ctx) { }
    
    @Override public void exitStmt_list(LITTLEParser.Stmt_listContext ctx) { }
    
    @Override public void enterStmt(LITTLEParser.StmtContext ctx) { }
    
    @Override public void exitStmt(LITTLEParser.StmtContext ctx) { }
    
    @Override public void enterBase_stmt(LITTLEParser.Base_stmtContext ctx) { }
    
    @Override public void exitBase_stmt(LITTLEParser.Base_stmtContext ctx) { }
    
    @Override public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {}
    
    @Override public void exitAssign_stmt(LITTLEParser.Assign_stmtContext ctx) { }
    
    @Override public void enterAssign_expr(LITTLEParser.Assign_exprContext ctx) { }
    
    @Override public void exitAssign_expr(LITTLEParser.Assign_exprContext ctx) {
        //Assign the result with the last temporary
        String id = ctx.id().getText();

        if(symbol_table.get("GLOBAL").get(id).equals("INT")) {
            irCode.add(
                    IRNode.store(IROp.STOREI, ("$T" + (nextTemp - 1)), id)
            );
        } else if(symbol_table.get("GLOBAL").get(id).equals("FLOAT")) {
            irCode.add(
                    IRNode.store(IROp.STOREF, ("$T" + (nextTemp - 1)), id)
            );
        }
        else {
            System.err.println("Failed to generate code for expression: " + ctx.getText());
        }

    }
    
    @Override public void enterRead_stmt(LITTLEParser.Read_stmtContext ctx) { }
    
    @Override public void exitRead_stmt(LITTLEParser.Read_stmtContext ctx) { }
    
    @Override public void enterWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
        //Deal with the first item.
        String firstId = ctx.id_list().id().getText();
        if(symbol_table.get("GLOBAL").get(firstId).equals("INT")) {
            //Write an integer
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITEI, firstId)
            );

        }
        if(symbol_table.get("GLOBAL").get(firstId).equals("FLOAT")) {
            //Write a float
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITEF, firstId)
            );
        }
        if(symbol_table.get("GLOBAL").get(firstId).startsWith("STRING")) {
            //Write a string
            irCode.add(
                    IRNode.ioAndJump(IROp.WRITES, firstId)
            );
        }
        System.out.println("Parsed var: " + ctx.id_list().id().getText());
        LITTLEParser.Id_tailContext currentContext = ctx.id_list().id_tail();
        while(currentContext.id() != null) {
            String id = currentContext.id().getText();
            if(symbol_table.get("GLOBAL").get(id).equals("INT")) {
                //Write an integer
                irCode.add(
                        IRNode.ioAndJump(IROp.WRITEI, id)
                );
            }
            if(symbol_table.get("GLOBAL").get(id).equals("FLOAT")) {
                //Write a float
                irCode.add(
                        IRNode.ioAndJump(IROp.WRITEF, id)
                );

            }
            if(symbol_table.get("GLOBAL").get(id).startsWith("STRING")) {
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
    
    @Override public void exitWrite_stmt(LITTLEParser.Write_stmtContext ctx) { }
    
    @Override public void enterReturn_stmt(LITTLEParser.Return_stmtContext ctx) { }
    
    @Override public void exitReturn_stmt(LITTLEParser.Return_stmtContext ctx) { }

    @Override public void enterExpr(LITTLEParser.ExprContext ctx) {
    }

    @Override public void exitExpr(LITTLEParser.ExprContext ctx) {
    }
    
    @Override public void enterExpr_prefix(LITTLEParser.Expr_prefixContext ctx) { }
    
    @Override public void exitExpr_prefix(LITTLEParser.Expr_prefixContext ctx) { }
    
    @Override public void enterFactor(LITTLEParser.FactorContext ctx) { }
    
    @Override public void exitFactor(LITTLEParser.FactorContext ctx) { }
    
    @Override public void enterFactor_prefix(LITTLEParser.Factor_prefixContext ctx) { }
    
    @Override public void exitFactor_prefix(LITTLEParser.Factor_prefixContext ctx) { }
    
    @Override public void enterPostfix_expr(LITTLEParser.Postfix_exprContext ctx) { }
    
    @Override public void exitPostfix_expr(LITTLEParser.Postfix_exprContext ctx) { }
    
    @Override public void enterCall_expr(LITTLEParser.Call_exprContext ctx) { }
    
    @Override public void exitCall_expr(LITTLEParser.Call_exprContext ctx) { }
    
    @Override public void enterExpr_list(LITTLEParser.Expr_listContext ctx) { }
    
    @Override public void exitExpr_list(LITTLEParser.Expr_listContext ctx) { }
    
    @Override public void enterExpr_list_tail(LITTLEParser.Expr_list_tailContext ctx) { }
    
    @Override public void exitExpr_list_tail(LITTLEParser.Expr_list_tailContext ctx) { }
    
    @Override public void enterPrimary(LITTLEParser.PrimaryContext ctx) { }
    
    @Override public void exitPrimary(LITTLEParser.PrimaryContext ctx) { }
    
    @Override public void enterAddop(LITTLEParser.AddopContext ctx) { }
    
    @Override public void exitAddop(LITTLEParser.AddopContext ctx) { }
    
    @Override public void enterMulop(LITTLEParser.MulopContext ctx) { }
    
    @Override public void exitMulop(LITTLEParser.MulopContext ctx) { }
    
    @Override public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) { }
    
    @Override public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) { }
    
    @Override public void enterElse_part(LITTLEParser.Else_partContext ctx) { }
    
    @Override public void exitElse_part(LITTLEParser.Else_partContext ctx) { }
    
    @Override public void enterCond(LITTLEParser.CondContext ctx) { }
    
    @Override public void exitCond(LITTLEParser.CondContext ctx) { }
    
    @Override public void enterCompop(LITTLEParser.CompopContext ctx) { }
    
    @Override public void exitCompop(LITTLEParser.CompopContext ctx) { }
    
    @Override public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) { }
    
    @Override public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) { }

    @Override public void enterEveryRule(ParserRuleContext ctx) { }
    
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
    
    @Override public void visitTerminal(TerminalNode node) { }
    
    @Override public void visitErrorNode(ErrorNode node) { }

    public void printIRCode() {
        for (IRNode node : irCode) {
            node.print();
        }
    }
}
