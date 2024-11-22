package splat.parser.elements.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.*;
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
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        if (!funcMap.containsKey(this.funcName)) {
            throw new RuntimeException("Function not defined: " + this.funcName);
        }

        expr.evaluate(funcMap, varAndParamMap);

//        FunctionDecl funcDecl = funcMap.get(this.funcName);
//
//        // Set up a new variable map for the function
//        Map<String, Value> localVarAndParamMap = new HashMap<>();
//
//        // Step 3: Evaluate arguments and map to function parameters
//        List<VariableDecl> parameters = funcDecl.getFuncParams();
////        if (arguments.size() != parameters.size()) {
////            throw new RuntimeException("Argument count mismatch for function: " + functionName);
////        }
//
//        List<VariableDecl> funcLocalVars = funcDecl.getFuncLocalVars();
//        for (VariableDecl localVar : funcLocalVars) {
//            localVarAndParamMap.put(localVar.getLabel().toString(), Value.defaultValue(localVar.getType()));
//        }
//
//        for (VariableDecl localVar : parameters) {
//            localVarAndParamMap.put(localVar.getLabel().toString(), Value.defaultValue(localVar.getType()));
//        }
//
////        for (int i = 0; i < arguments.size(); i++) {
////            Value argValue = arguments.get(i).evaluate(funcMap, varAndParamMap); // Evaluate the argument
////            localVarAndParamMap.put(parameters.get(i).getLabel().toString(), argValue); // Map parameter to value
////        }
//
//        // Map parameters to argument values
////        for (int i = 0; i < arguments.size(); i++) {
////            Value argValue = arguments.get(i).evaluate(varAndParamMap, funcMap);
////            String paramName = funcDecl.getFuncParams().get(i).getLabel();
////            newVarAndParamMap.put(paramName, argValue);
////        }
//
//        // Execute function body
//        for (Statement stmt : funcDecl.getStmts()) {
//            System.out.println("start");
//            stmt.execute(funcMap, localVarAndParamMap);
//            System.out.println("end");
//        }

    }
}
