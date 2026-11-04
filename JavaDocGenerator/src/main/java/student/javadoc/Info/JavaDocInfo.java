package student.javadoc.Info;

import java.util.Map;

public class JavaDocInfo {

    private final String description;
    private final Map<String, String> parameterDescriptions;
    private final String returnDescription;

    public JavaDocInfo(String description, Map<String, String> parameterDescriptions, String returnDescription) {
        this.description = description;
        this.parameterDescriptions = Map.copyOf(parameterDescriptions);
        this.returnDescription = returnDescription;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getParameterDescriptions() {
        return parameterDescriptions;
    }

    public String getReturnDescription() {
        return returnDescription;
    }
}