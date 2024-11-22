package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.FloatValue;
import splat.executor.values.IntValue;
import splat.lexer.Token;

import java.util.Map;

public class UnaryExpr extends Expression {
    private String operator;
    private Expression expr;
    private Token token;

    public UnaryExpr(String operator, Expression expr, Token token) {
        super(token);
        this.operator = operator;
        this.expr = expr;
        this.token = token;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (exprType == null){
            return null;
        }

        if (operator.equals("-")) {
            if (exprType.equals(Type.INT) || exprType.equals(Type.FLOAT)) {
                return exprType; // Negation applies to int and float
            }
//            else {
//                throw new SemanticAnalysisException("Invalid type for unary operator '-'", this);
//            }
        } else if (operator.equals("!")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN; // Logical NOT applies to booleans
            }
//            else {
//                throw new SemanticAnalysisException("Invalid type for unary operator '!'", this);
//            }
        } else if (operator.equals("not")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN;
            }
        } else if (operator.equals("and")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN;
            }
        }
//        else {
//            throw new SemanticAnalysisException("Unknown unary operator: " + operator, this);
//        }
        return null;
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value innerValue = expr.evaluate(funcMap, varAndParamMap);

        // Apply the unary operator
        switch (operator) {
            case "-": // Negation for INT or FLOAT
                if (innerValue instanceof IntValue) {
                    return new IntValue(-((IntValue) innerValue).getValue());
                } else if (innerValue instanceof FloatValue) {
                    return new FloatValue(-((FloatValue) innerValue).getValue());
                }
                throw new RuntimeException("Invalid type for unary operator '-': " + innerValue);

            case "!": // Logical NOT for BOOLEAN
                if (innerValue instanceof BooleanValue) {
                    return new BooleanValue(!((BooleanValue) innerValue).getValue());
                }
                throw new RuntimeException("Invalid type for unary operator '!': " + innerValue);
            case "not":
                if (innerValue instanceof BooleanValue) {
                    return new BooleanValue(!((BooleanValue) innerValue).getValue());
                }
                throw new RuntimeException("Invalid type for unary operator 'not': " + innerValue);
            default:
                throw new RuntimeException("Unknown unary operator: " + operator);
        }
    }
}