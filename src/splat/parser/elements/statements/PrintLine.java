package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class PrintLine extends Statement {
    public PrintLine(Token token) {
        super(token);
    }
}
