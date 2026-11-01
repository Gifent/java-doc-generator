package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface HtmlGeneretionStrategy {
    
    void generate(List<ClassInfo> classes, Path outputPath) 
        throws IOException;
    

}
