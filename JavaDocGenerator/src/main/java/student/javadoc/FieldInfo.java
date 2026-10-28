package student.javadoc;

public class FieldInfo {
    private final String name;
    private final String type;
    private final String description;

    public FieldInfo(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
