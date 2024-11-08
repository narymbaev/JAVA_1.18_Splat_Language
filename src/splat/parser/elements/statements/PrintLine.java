package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;

import java.util.Map;

public class PrintLine extends Statement {
    public PrintLine(Token token) {
        super(token);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        // No need to check
    }
}
