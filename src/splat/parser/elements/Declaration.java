package splat.parser.elements;

import splat.lexer.Token;

public abstract class Declaration extends ASTElement {
	private StringBuilder label;

	public Declaration(Token tok) {
		super(tok);
		this.label = new StringBuilder(tok.getValue());
	}

	public StringBuilder getLabel() {
		return label;
	}
}
