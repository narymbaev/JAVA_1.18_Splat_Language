package splat.parser.elements.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncCall extends Statement {
    private String funcName;
    private Expression expr;
    private List<Expression> args;

    public FuncCall(Token token, Expression expr, List<Expression> args) {
        super(token);
        this.funcName = token.getValue();
        this.expr = expr;
        this.args = args;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!funcMap.containsKey(funcName)) {
            throw new SemanticAnalysisException("Function not defined: " + funcName, this);
        }

        FunctionDecl funcDecl = funcMap.get(funcName);

        // Check argument count
        if (args.size() != funcDecl.getFuncParams().size()) {
            throw new SemanticAnalysisException("Argument count mismatch for function " + funcName, this);
        }

        // Check argument types
        for (int i = 0; i < args.size(); i++) {
            Type expectedType = funcDecl.getFuncParams().get(i).getType();
            Type actualType = args.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if (!expectedType.equals(actualType)) {
                throw new SemanticAnalysisException("Type mismatch in argument " + (i + 1) + " of function " + funcName, this);
            }
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        if (!funcMap.containsKey(this.funcName)) {
            throw new RuntimeException("Function not defined: " + this.funcName);
        }

        FunctionDecl funcDecl = funcMap.get(this.funcName);

        // Set up a new variable map for the function
        Map<String, Value> newVarAndParamMap = new HashMap<String, Value>();

        // Map parameters to argument values
//        for (int i = 0; i < arguments.size(); i++) {
//            Value argValue = arguments.get(i).evaluate(varAndParamMap, funcMap);
//            String paramName = funcDecl.getFuncParams().get(i).getLabel();
//            newVarAndParamMap.put(paramName, argValue);
//        }

        // Execute function body
        for (Statement stmt : funcDecl.getStmts()) {
            stmt.execute(funcMap, newVarAndParamMap);
        }

    }
}
