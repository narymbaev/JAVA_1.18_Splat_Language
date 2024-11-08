package splat.parser.elements;

import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;


public class FunctionDecl extends Declaration {
	private StringBuilder label;
	private Type returnType;
	private List<VariableDecl> funcParams;
	private List<VariableDecl> funcLocalVars;
	private List<Statement> funcStatements;
	// Need to add some fields
	
	// Need to add extra arguments for setting fields in the constructor 
	public FunctionDecl(Token tok, Type returnType, List<VariableDecl> funcParams, List<VariableDecl> funcLocalVars, List<Statement> funcStatements) {
		super(tok);
		this.label = new StringBuilder(tok.getValue());
		this.returnType = returnType;
		this.funcParams = funcParams;
		this.funcLocalVars = funcLocalVars;
		this.funcStatements = funcStatements;
	}

	// Getters?
	public List<Statement> getStmts() {
		return funcStatements;
	}

	public List<VariableDecl> getFuncParams() {
		return funcParams;
	}

	public List<Type> getParameterTypes() {
		List<Type> funcParamTypes = new ArrayList<>();
		for (VariableDecl param: getFuncParams()) {
			funcParamTypes.add(param.getType());
		}

		return funcParamTypes;
	}

	public List<VariableDecl> getFuncLocalVars() {
		return funcLocalVars;
	}

	public Type getReturnType() {
		return returnType;
	}
	
	// Fix this as well
	public String toString() {
		return label.toString();
	}
}
