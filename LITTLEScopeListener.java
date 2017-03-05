public class LITTLEScopeListener extends LITTLEBaseListener {
	@Override
	public void enterProgram(LITTLEParser.ProgramContext ctx) {
		// todo new scope
	}
	@Override
	public void exitProgram(LITTLEParser.ProgramContext ctx) {
		// todo pop scope	
	}

	@Override
	public void enterString_decl(LITTLEParser.String_declContext ctx) {
		// store string in current scope
	}
	@Override
	public void exitString_decl(LITTLEParser.String_declContext ctx) {

	}

	@Override
	public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
		// store var in current scope
	}
	@Override
	public void exitVar_decl(LITTLEParser.Var_declContext ctx) {

	}

	@Override
	public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {
		// store param in current scope
	}
	@Override
	public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx) {

	}

	@Override
	public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
		// todo new scope
	}
	@Override
	public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
		// todo pop scope
	}

	@Override
	public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// todo new scope
	}
	@Override
	public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
		// todo pop scope
	}

	@Override
	public void enterElse_part(LITTLEParser.Else_partContext ctx) {
		// todo new scope
	}
	@Override
	public void exitElse_part(LITTLEParser.Else_partContext ctx) {
		// todo pop scope
	}

	@Override
	public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// todo new scope
	}
	@Override
	public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
		// todo pop scope
	}
}