package splat.parser.elements.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import javax.sound.midi.SysexMessage;
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

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
//        System.out.println(expr.getToken().getValue());
        Value result = expr.evaluate(funcMap, varAndParamMap);

        // Print the result to the console
        System.out.print(result.getValue());
    }
}
