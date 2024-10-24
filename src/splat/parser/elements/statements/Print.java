package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class Print extends Statement{
    public Print(Token tok){
        super(tok);
    }
}
