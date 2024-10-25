package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class Return extends Statement {
    public Return(Token tok) {
        super(tok);
    }
}
