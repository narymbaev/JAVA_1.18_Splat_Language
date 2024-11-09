package splat.executor.values;

import splat.executor.Value;

public class IntValue extends Value {
    private Integer value;

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}