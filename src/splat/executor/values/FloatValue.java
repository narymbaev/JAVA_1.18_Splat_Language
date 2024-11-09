package splat.executor.values;

import splat.executor.Value;

public class FloatValue extends Value {
    private Float value;

    public FloatValue(float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }
}