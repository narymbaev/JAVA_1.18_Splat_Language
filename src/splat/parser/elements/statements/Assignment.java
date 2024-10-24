package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class Assignment extends Statement {
    public Assignment(Token tok) {
        super(tok);
    }
}
