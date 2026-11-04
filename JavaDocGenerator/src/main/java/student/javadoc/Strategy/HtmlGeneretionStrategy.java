package student.javadoc.Strategy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import student.javadoc.Info.ClassInfo;

public interface HtmlGeneretionStrategy {
    
    void generate(List<ClassInfo> classes, Path outputPath) throws IOException;
    
}
