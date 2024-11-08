package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Assignment extends Statement {
    private String varName;
    private Expression expr;

    public Assignment(Token tok, Expression expr) {
        super(tok);
        this.varName = tok.getValue();
        this.expr = expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!varAndParamMap.containsKey(varName)) {
            throw new SemanticAnalysisException("Variable not defined: " + varName, this);
        }
        // Type-check the expression
        Type expectedType = varAndParamMap.get(varName);
        Type actualType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (!expectedType.equals(actualType)) {
            throw new SemanticAnalysisException("Type mismatch in assignment to " + varName, this);
        }
    }
}
