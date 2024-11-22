package splat.parser.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;

public class FunctionCallExpr extends Expression {
    private String functionName;
    private List<Expression> arguments;
    private List<Statement> localVars;
    private Token token;

    public FunctionCallExpr(String functionName, List<Expression> arguments, Token token) {
        super(token);
        this.functionName = functionName;
        this.arguments = arguments;
        this.localVars = localVars;
        this.token = token;
    }
    // Getters and other methods...

    public Token getToken() {
        return token;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public List<Statement> getLocalVars() {
        return localVars;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        FunctionDecl function = funcMap.get(functionName);

        if (function == null) {
//            throw new SemanticAnalysisException("Undefined function: " + functionName, this);
            return null;
        }

        // Check if argument types match parameter types
        List<Type> paramTypes = function.getParameterTypes();
        if (arguments.size() != paramTypes.size()) {
//            throw new SemanticAnalysisException("Argument count mismatch in function call: " + functionName, this);
            return null;
        }

        for (int i = 0; i < arguments.size(); i++) {
            Type argType = arguments.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if (!argType.equals(paramTypes.get(i))) {
//                throw new SemanticAnalysisException("Argument type mismatch in function call: " + functionName, this);
                return null;
            }
        }

        return function.getReturnType();
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        FunctionDecl function = funcMap.get(functionName);

        if (function == null) {
            throw new RuntimeException("Undefined function: " + functionName);
        }

        // Step 2: Set up a new map for the function's execution context
        Map<String, Value> localVarAndParamMap = new HashMap<>();

        // Step 3: Evaluate arguments and map to function parameters
        List<VariableDecl> parameters = function.getFuncParams();
        if (arguments.size() != parameters.size()) {
            throw new RuntimeException("Argument count mismatch for function: " + functionName);
        }

        List<VariableDecl> funcLocalVars = function.getFuncLocalVars();
        for (VariableDecl localVar : funcLocalVars) {
            localVarAndParamMap.put(localVar.getLabel().toString(), Value.defaultValue(localVar.getType()));
        }

        for (int i = 0; i < arguments.size(); i++) {
            Value argValue = arguments.get(i).evaluate(funcMap, varAndParamMap); // Evaluate the argument
            localVarAndParamMap.put(parameters.get(i).getLabel().toString(), argValue); // Map parameter to value
        }

        // Step 4: Execute the function body
        try {
            for (Statement stmt : function.getStmts()) {
                stmt.execute(funcMap, localVarAndParamMap);
            }

            // If no return statement is executed, return default value
            return Value.defaultValue(function.getReturnType());
        } catch (ReturnFromCall returnFromCall) {
            // Retrieve and return the function's return value
            return returnFromCall.getReturnVal();
        }
    }
}