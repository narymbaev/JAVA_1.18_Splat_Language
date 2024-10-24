package splat.parser;

import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.statements.*;

import javax.swing.plaf.nimbus.State;

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
			
			List<Statement> stmts = parseStmts();
			
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

		List<Statement> funcStatements = parseStmts();

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

		while(!peekNext("end")) {
			statements.add(parseStatement());
		}

		return statements;
	}

	private Statement parseStatement() throws ParseException {
		if (peekNext("print")) {
			return parsePrint();
		} else if (peekNext("if")) {
			return parseIfThenElse();
		} else if (peekNext("while")) {
			return parseWhile();
		} else if (peekTwoAhead(":=")) {
			return parseAssignment();
		} else {
			throw new ParseException("Statement parse exception", -1, -1);
		}

	}

	private Statement parsePrint() throws ParseException {
		Token printToken = tokens.remove(0);

		return new Print(printToken);
	}

	private Statement parseIfThenElse() throws ParseException {
		Token varIfToken = tokens.remove(0);

		return new IfThenElse(varIfToken);
	}

	private Statement parseWhile() throws ParseException {
		Token whileToken = tokens.remove(0);

		return new While(whileToken);
	}

	private Statement parseAssignment() throws ParseException {
		Token varNameToken = tokens.remove(0);

		checkNext(":=");

		return new Assignment(varNameToken);
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
