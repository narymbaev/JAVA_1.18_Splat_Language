package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {
	private Type type;
	// Need to add some fields
	
	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(Token tok, Type type) {
		super(tok);
		this.type = type;
	}

	// Getters?
	public Type getType() {
		return type;
	}
	
	// Fix this as well
	public String toString() {
		return null;
	}
}
