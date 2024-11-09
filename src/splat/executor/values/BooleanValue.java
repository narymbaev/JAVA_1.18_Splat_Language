package splat.executor.values;

import splat.executor.Value;

public class BooleanValue extends Value {
    private Boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}
