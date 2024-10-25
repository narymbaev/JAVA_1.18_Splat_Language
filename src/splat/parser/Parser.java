package splat.parser;

import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.statements.*;

public class Parser {

	private List<Token> tokens;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			
			checkNext("program");
			
			List<Declaration> decls = parseDecls();
			
			checkNext("begin");

//			System.out.println("MAIN begin");

			List<Statement> stmts = parseStmts();

//			System.out.println("MAIN end");


			checkNext("end");
			checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		System.out.println("Function declaration");
		// TODO Auto-generated method stub
		Token funcNameToken = tokens.remove(0);

		checkNext("(");

		List<VariableDecl> funcParams = parseFuncParams();

		checkNext(")");
		checkNext(":");

		String funcReturnType = tokens.remove(0).getValue();

		checkNext("is");

		List<VariableDecl> funcLocalVars = parseFuncLocalVars();

		checkNext("begin");

		System.out.println("FUNC begin parse");

		List<Statement> funcStatements = parseStmts();

		System.out.println("FUNC end parse");

		checkNext("end");
		checkNext(";");

		return new FunctionDecl(funcNameToken);
	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		// TODO Auto-generated method stub
		Token varNameToken = tokens.remove(0);

		if (isKeyword(varNameToken.getValue())) {
			throw new ParseException("variable expected, got " + varNameToken.getValue(), varNameToken);
		};

		checkNext(":");

		String varType = tokens.remove(0).getValue();

		checkNext(";");

		return new VariableDecl(varNameToken);
	}

	private VariableDecl parseFuncVarDecl() throws ParseException {
		Token varNameToken = tokens.remove(0);

		checkNext(":");

		String varType = tokens.remove(0).getValue();

		return new VariableDecl(varNameToken);
	}
	
	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {
		// TODO Auto-generated method stub
		List<Statement> statements = new ArrayList<>();

		while(!peekNext("end") && !peekNext("else")) {
			statements.add(parseStatement());
		}

		return statements;
	}

	private Statement parseStatement() throws ParseException {
		System.out.println("STATEMENT " + tokens.get(0).getValue() + " " + tokens.get(1).getValue());

		if (peekNext("print")) {
			return PrintStatement();
		} else if (peekNext("return")){
			return ReturnStatement();
		} else if (peekNext("print_line")){
			return PrintLineStatement();
		} else if (peekNext("if")) {
			return IfThenElseStatement();
		} else if (peekNext("while")) {
			return WhileStatement();
		} else if (peekTwoAhead(":=")) {
			return AssignmentStatement();
		} else if (isFunctionCall()) {
			return FunctionCallStatement();
		} else {
				throw new ParseException("Statement parse exception", -1, -1);
		}

	}

	private Statement PrintStatement() throws ParseException {
		Token printToken = tokens.remove(0);

//		if (isLiteral(printToken)) {
//
//		}
		Expression expr = parseSimpleExpr();

		checkNext(";");

		return new Print(printToken);
	}

	private Statement PrintLineStatement() throws ParseException {
		Token printLineToken = tokens.remove(0);

		checkNext(";");

		return new PrintLine(printLineToken);
	}

	private Statement ReturnStatement() throws ParseException {
		Token returnToken = tokens.remove(0);

		if (!peekNext(";")){
			Expression expr = parseSimpleExpr();
		}

		checkNext(";");

		return new Return(returnToken);
	}

	private Statement IfThenElseStatement() throws ParseException {
		Token varIfToken = tokens.remove(0);

		Expression condition = parseExpr();

		checkNext("then");

		List<Statement> thenStatements = parseStmts();

		List<Statement> elseStatements = null;

		if (peekNext("else")) {
			checkNext("else");
			elseStatements = parseStmts();
		}

		checkNext("end");
		checkNext("if");
		checkNext(";");


		return new IfThenElse(varIfToken);
	}

	private Statement WhileStatement() throws ParseException {
		Token whileToken = tokens.remove(0);

		if (!peekNext("(")){
			checkNext("(");
		};

		Expression condition = parseExpr();

		checkNext("do");

		List<Statement> whileStatements = parseStmts();

		checkNext("end");

		checkNext("while");

		checkNext(";");

		return new While(whileToken);
	}

	private Statement AssignmentStatement() throws ParseException {
		Token varNameToken = tokens.remove(0);

//		boolean closeBraket = false;
//		if (!peekTwoAhead(";")){
//			closeBraket = true;
//			checkNext("(");
//		}

		checkNext(":=");

		Expression expr = parseExpr();

//		if (closeBraket){
//			checkNext(")");
//			closeBraket = false;
//		}

		checkNext(";");

		return new Assignment(varNameToken);
	}

	private Statement FunctionCallStatement() throws ParseException {
		Token functionNameToken = tokens.get(0);

		Expression expr = parseFuncCall();

		checkNext(";");

		return new FuncCall(functionNameToken);
	}

	private Expression parseExpr() throws ParseException {
		if (peekNext("(")) {

			System.out.println("while parse START");

			checkNext("(");

			Expression expr = parseCompoundExpr();  // Parse the compound expression inside the parentheses

			System.out.println("while parse END");

			checkNext(")");

			return expr;
		} else {
			// If no '(', treat it as a simple expression without operators
			return parseSimpleExpr();
		}
	}

	private Expression parseSimpleExpr() throws ParseException {
		if (peekNext("-")) {
			Token minusToken = tokens.remove(0);  // Consume '-'
			Expression factor = parseExpr();  // Parse the next factor
			return new UnaryExpr("-", factor, minusToken);  // Create a unary expression node
		} else if (peekTwoAhead("(")) {  // Function call detection
			return parseFuncCall();  // Parse as a function call
		} else if (isLiteral(tokens.get(0))) {
			return parseLiteral();  // Parse literals like numbers or booleans
		} else {
			return parseVariable();  // Parse variable names
		}
	}

	private Expression parseCompoundExpr() throws ParseException {
		Expression left = parseExpr();

		while (peekNext("+") || peekNext("-") || peekNext("*") || peekNext("/") || peekNext("%")
		|| peekNext(">") || peekNext("<") || peekNext("<=") || peekNext(">=")) {
			Token operatorToken = tokens.remove(0);
			Expression right = parseExpr();
			left = new BinaryExpr(left, operatorToken.getValue(), right, operatorToken);
		}

		return left;

//		Expression left = parseTerm();
//
//		while (peekNext("+") || peekNext("-")) {
//			Token operator = tokens.remove(0);
//			Expression right = parseTerm();
//			left = new BinaryExpr(left, operator.getValue(), right, operator);
//		}
//
//		return left;
	}

	private Expression parseTerm() throws ParseException {
		Expression left = parseFactor();  // Parse the first factor

		while (peekNext("*") || peekNext("/")) {
			Token operator = tokens.remove(0);  // Get the operator ('*' or '/')
			Expression right = parseFactor();  // Parse the next factor
			left = new BinaryExpr(left, operator.getValue(), right, operator);  // Create a binary expression node
		}

		return left;
	}

	private Expression parseFactor() throws ParseException {
//		if (peekNext("(")) {
//			checkNext("(");  // Consume '('
//			Expression expr = parseExpr();  // Parse the expression inside the parentheses
//			checkNext(")");  // Consume ')'
//			return expr;
//		} else
		if (peekNext("-")) {
			Token minusToken = tokens.remove(0);  // Consume '-'
			Expression factor = parseFactor();  // Parse the next factor
			return new UnaryExpr("-", factor, minusToken);  // Create a unary expression node
		} else if (peekTwoAhead("(")) {  // Function call detection
			return parseFuncCall();  // Parse as a function call
		} else if (isLiteral(tokens.get(0))) {
			return parseLiteral();  // Parse literals like numbers or booleans
		} else {
			return parseVariable();  // Parse variable names
		}
	}

	private boolean isLiteral(Token token) {
		return (!isKeyword(token.getValue()) || Returnable(token.getValue()));
	}

	private boolean isKeyword(String value) {
		String[] keywords = {
				"program", "begin", "end", "is", "while", "do", "if", "then", "else",
				"print", "print_line", "return", "and", "or", "not", "true", "false",
				"void", "Integer", "Boolean", "String"
		};
		for (String keyword : keywords) {
			if (value.equals(keyword)) {
				return true;
			}
		}
		return false;
	}

	private boolean Returnable(String value) {
		String[] keywords = {
				"true", "false"
		};

		for (String keyword : keywords) {
			if (value.equals(keyword)) {
				return true;
			}
		}

		return false;
	}

	private boolean isFunctionCall() {
		return peekTwoAhead("(");
	}

	private Expression parseLiteral() throws ParseException {
		Token literalToken = tokens.remove(0);  // Get the literal token
		return new LiteralExpr(literalToken);  // Create a literal expression node
	}

	private Expression parseVariable() throws ParseException {
		Token varToken = tokens.remove(0);  // Get the variable token

		if (isKeyword(varToken.getValue())) {
			throw new ParseException("variable expected, got " + varToken.getValue(), tokens.get(0));
		}

		return new VariableExpr(varToken);  // Create a variable expression node
	}

	private Expression parseFuncCall() throws ParseException {
		Token funcNameToken = tokens.remove(0);  // Get the function name
		checkNext("(");  // Consume '('

		List<Expression> args = new ArrayList<>();
		if (!peekNext(")")) {  // Check for arguments
			args.add(parseExpr());  // Parse the first argument

			while (peekNext(",")) {  // Additional arguments separated by commas
				checkNext(",");
				args.add(parseExpr());
			}
		}

		checkNext(")");  // Consume ')'
		return new FunctionCallExpr(funcNameToken.getValue(), args, funcNameToken);  // Return a function call expression
	}

	private List<VariableDecl> parseFuncParams() throws ParseException {
		List<VariableDecl> funcParams = new ArrayList<>();

		if (!peekNext(")")){
			funcParams.add(parseFuncVarDecl());

			while(peekNext(",")){
				checkNext(",");
				funcParams.add(parseFuncVarDecl());
			}
		}

		return funcParams;
	}

	private List<VariableDecl> parseFuncLocalVars() throws ParseException {
		List<VariableDecl> localVars = new ArrayList<>();
		while (!peekNext("begin")) {
			localVars.add(parseVarDecl());
		}
		return localVars;
	}



}
