package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class JavaFileParser {

    public void parseFile(Path filePath) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(filePath);

        System.out.println("Файл: " + filePath);
 
        for (var classDeclaration : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
            System.out.println("Найден класс: " + classDeclaration.getNameAsString());

            for (var field : classDeclaration.getFields()) {
                System.out.println(" Поле: " + field.getVariables().get(0).getNameAsString()
             + " | тип: " + field.getElementType());
            }

            for (var method : classDeclaration.getMethods()) {
                System.out.println("  Метод: " + method.getNameAsString() + 
                " возвращает: " + method.getType());

                for (var parameter : method.getParameters()) {
                    System.out.println("   Параметр: " + parameter.getNameAsString() + 
                "| тип: " + parameter.getType());
                }
            }
        }
    }

    public List<ClassInfo> parseClasses(Path filePath) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(filePath);

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
