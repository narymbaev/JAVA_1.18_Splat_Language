package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Print extends Statement{
    private Expression expr;

    public Print(Token tok, Expression expr){
        super(tok);
        this.expr = expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type type = expr.analyzeAndGetType(funcMap, varAndParamMap);
        if (type == null) {
            throw new SemanticAnalysisException("Something went wrong", this);
        }
    }
}
