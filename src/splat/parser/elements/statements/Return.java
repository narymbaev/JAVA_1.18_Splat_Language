package splat.parser.elements.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Return extends Statement {
    private Expression expr;

    public Return(Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type expectedType = varAndParamMap.get("0result");

        if (expectedType == null) {
            throw new SemanticAnalysisException("Return statement is not needed", this);
        }

        if (expr == null && !expectedType.getValue().equals("void")){
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        Type actualType;

        if (expr == null) {
            actualType = Type.VOID;
        } else {
            actualType = expr.analyzeAndGetType(funcMap, varAndParamMap);
        }

        if (!expectedType.equals(actualType)) {
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        varAndParamMap.put("1result", actualType);

    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        Value returnValue = null;

        if (expr != null) {
            returnValue = expr.evaluate(funcMap, varAndParamMap);
        }

        throw new ReturnFromCall(returnValue);
    }
}
