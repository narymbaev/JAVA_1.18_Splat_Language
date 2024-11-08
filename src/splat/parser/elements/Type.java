package splat.parser.elements;

public class Type {
    public static final Type INT = new Type("Integer");
    public static final Type FLOAT = new Type("Float");
    public static final Type STRING = new Type("String");
    public static final Type BOOLEAN = new Type("Boolean");
    public static final Type BOOL = new Type("Bool");
    public static final Type VOID = new Type("Void");
    public static final Type NULL = new Type("Null");

    private String value;

    public Type(String value) {
        this.value = value;
    }
}
