package student.javadoc.Info;

import java.util.List;
import java.util.Map;

public class MethodInfo {
    
    private final String name;
    private final String type;
    private final String description;
    private final List<String> parameters;
    private final Map<String, String> parameterDescription;
    private final String returnDescription;

    public MethodInfo(String name, String type, String description, List<String> parameters, Map<String, String> parameterDescription, String returnDescription) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.parameters = List.copyOf(parameters);
        this.parameterDescription = Map.copyOf(parameterDescription);
        this.returnDescription = returnDescription;
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

    public List<String> getParameters() {
        return parameters;
    }

    public Map<String, String> getParameterDescription() {
        return parameterDescription;
    }

    public String getReturnDescription() {
        return returnDescription;
    }
}
