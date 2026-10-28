package student.javadoc;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    private final String name;
    private final String description;
    private final List<FieldInfo> fields;
    private final List<MethodInfo> methods;

    private ClassInfo(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.fields = List.copyOf(builder.fields);
        this.methods = List.copyOf(builder.methods);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public static class Builder {

        private final String name;
        private String description = "";
        private final List<FieldInfo> fields = new ArrayList<>();
        private final List<MethodInfo> methods = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addField(FieldInfo field) {
            fields.add(field);
            return this;
        }

        public Builder addMethod(MethodInfo method) {
            methods.add(method);
            return this;
        }

        public ClassInfo build() {
            return new ClassInfo(this);
        }
    }
}
