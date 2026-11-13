package student.javadoc.Parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import student.javadoc.Extractor.CommentExtractor;
import student.javadoc.Info.ClassInfo;
import student.javadoc.Info.FieldInfo;
import student.javadoc.Info.JavaDocInfo;
import student.javadoc.Info.MethodInfo;

public class JavaFileParser {

    public List<ClassInfo> parseClasses(Path filePath) throws IOException {
        ParserConfiguration configuration = new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        CompilationUnit compilationUnit = new JavaParser(configuration).parse(filePath).getResult().orElseThrow();

        CommentExtractor commentExtractor = new CommentExtractor();

        List<ClassInfo> classes = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {

            String classDescription = commentExtractor.extractComment(classDeclaration).orElse("");
            ClassInfo.Builder builder = new ClassInfo.Builder(classDeclaration.getNameAsString());

            builder.description(classDescription);

            for (FieldDeclaration field : classDeclaration.getFields()) {
                String fieldType = field.getElementType().asString();

                for (var variable : field.getVariables()) {
                    String fieldName = variable.getNameAsString();
                    String fieldDescription = commentExtractor.extractComment(field).orElse("");

                    builder.addField(new FieldInfo(fieldName, fieldType, fieldDescription));
                }
            }

            for (MethodDeclaration method : classDeclaration.getMethods()) {
                String methodName = method.getNameAsString();
                String returnType = method.getType().asString();

                JavaDocInfo javadocInfo = commentExtractor.extractJavadoc(method);

                List<String> parameters = new ArrayList<>();

               for (Parameter parameter : method.getParameters()) {
                    parameters.add(parameter.getType().asString() + " " + parameter.getNameAsString());
                }

                builder.addMethod(new MethodInfo(methodName, returnType, javadocInfo.getDescription(), parameters, javadocInfo.getParameterDescriptions(), javadocInfo.getReturnDescription()));
            }

            classes.add(builder.build());
        }

        return classes;
    }
}
