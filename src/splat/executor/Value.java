package splat.executor;

import com.sun.org.apache.xpath.internal.operations.Bool;
import splat.executor.values.BooleanValue;
import splat.executor.values.FloatValue;
import splat.executor.values.IntValue;
import splat.executor.values.StringValue;
import splat.parser.elements.Type;

public abstract class Value {

    public abstract Object getValue();

    // Factory method to return a default value based on the type
    public static Value defaultValue(Type type) {
        switch (type.getValue()) {
            case "Integer": return new IntValue(0);
            case "Float": return new FloatValue(0.0F);
            case "Boolean": return new BooleanValue(false);
            case "String": return new StringValue("");
        };

        return null;

    }
}
