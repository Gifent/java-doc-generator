package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;

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
 
        for (var classDeclaration : compilationUnit.findAll(
            ClassOrInterfaceDeclaration.class)) {
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
}
