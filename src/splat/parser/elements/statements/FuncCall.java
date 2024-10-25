package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class FuncCall extends Statement {
    public FuncCall(Token token){
        super(token);
    }
}
