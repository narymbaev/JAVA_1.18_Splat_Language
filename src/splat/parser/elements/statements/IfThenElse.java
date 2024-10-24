package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class IfThenElse extends Statement {
    public IfThenElse(Token tok) {
        super(tok);
    }
}