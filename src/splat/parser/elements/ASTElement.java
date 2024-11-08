package splat.parser.elements;

import splat.lexer.Token;

public abstract class ASTElement {

	private int line;
	private int column;
	private Token token;
	
	public ASTElement(Token tok) {
		this.line = tok.getLine();
		this.column = tok.getColumn();
		this.token = tok;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}

	public Token getToken() {
		return token;
	}
}
