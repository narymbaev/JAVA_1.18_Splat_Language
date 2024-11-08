package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;

import java.util.Map;

public class Print extends Statement{
    private Expression expr;

    public Print(Token tok, Expression expr){
        super(tok);
        this.expr = expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        expr.analyzeAndGetType(funcMap, varAndParamMap);
    }
}
