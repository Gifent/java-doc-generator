package student.javadoc.Facade;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import student.javadoc.Analyzer.ProjectAnalyzer;
import student.javadoc.Info.ClassInfo;
import student.javadoc.Parser.JavaFileParser;
import student.javadoc.Strategy.HtmlGeneretionStrategy;

public class DocumentationFacade {
    private final ProjectAnalyzer projectAnalyzer;
    private final JavaFileParser javaFileParser;
    private final HtmlGeneretionStrategy htmlGeneretionStrategy;

    public DocumentationFacade (HtmlGeneretionStrategy htmlGeneretionStrategy) {
        this.projectAnalyzer = new ProjectAnalyzer();
        this.javaFileParser = new JavaFileParser();
        this.htmlGeneretionStrategy = htmlGeneretionStrategy;
    }

    public void generateDocumentation (Path sourcePath, Path outputPath) throws IOException{
        List<Path> javaFiles = projectAnalyzer.findJavaFiles(sourcePath);

        List<ClassInfo> classes = new ArrayList<>();

        for (Path javaFile : javaFiles) {
            List<ClassInfo> parseClasses = javaFileParser.parseClasses(javaFile);
            classes.addAll(parseClasses);
        }

        htmlGeneretionStrategy.generate(classes, outputPath);

    }
}
