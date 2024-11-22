package splat.parser.elements;

import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.FloatValue;
import splat.executor.values.IntValue;
import splat.executor.values.StringValue;
import splat.lexer.Token;

import java.util.Map;

public class LiteralExpr extends Expression {
    public LiteralExpr(Token token){
        super(token);
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        String value = getToken().getValue();

        if (value.matches("-?\\d+")) {
            return Type.INT; // Assuming an INT type exists for integers
        } else if (value.matches("-?\\d*\\.\\d+")) {
            return Type.FLOAT; // Assuming a FLOAT type exists for decimal numbers
        } else if (value.equals("true") || value.equals("false")) {
            return Type.BOOLEAN; // Assuming a BOOLEAN type exists for booleans
        } else {
            return Type.STRING; // Assuming a STRING type exists for other literals
        }
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        String value = getToken().getValue();

        // Integer literal
        if (value.matches("-?\\d+")) { // Matches integers including negatives
            return new IntValue(Integer.parseInt(value)); // Convert to IntValue
        }
        // Float literal
        else if (value.matches("-?\\d*\\.\\d+")) { // Matches floating-point numbers
            return new FloatValue(Float.parseFloat(value)); // Convert to FloatValue
        }
        // Boolean literal
        else if (value.equals("true") || value.equals("false")) {
            return new BooleanValue(Boolean.parseBoolean(value)); // Convert to BooleanValue
        }
        // String literal
        else {
            // Remove surrounding quotes if needed
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1); // Strip quotes
            }
            return new StringValue(value); // Convert to StringValue
        }
    }
}
