package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;



public class Main {

    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{

        testClassInfo();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к проекту: ");
        String sourceInput = scanner.nextLine();

        System.out.println("Введите путь для сохранения документации: ");
        String outputInput = scanner.nextLine();

        Path sourcePath = Path.of(sourceInput);
        Path outputPath = Path.of(outputInput);

        System.out.println("Путь к проекту: " + sourcePath.toAbsolutePath());
        System.out.println("Путь к документации: " + outputPath.toAbsolutePath());

        scanner.close();
    }

    /**
     * asdasd
     * asdasd
     * asdasd
     */
    private static void testProjectAnalyzer() {
        ProjectAnalyzer analyzer = new ProjectAnalyzer();
        Path sourcePath = Path.of("D:/TestProject");

        try {
            List<Path> javaFiles = analyzer.findJavaFiles(sourcePath);

            for (Path file : javaFiles) {
                System.out.println(file);
            }

            System.out.println("Всего файлов: " + javaFiles.size());

        } catch (IOException e) {
            System.out.println("Ошибка чтения файлов: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * 
     * @throws IOException
     */
    private static void testJavaFileParser() throws IOException {
        JavaFileParser parser = new JavaFileParser();

        Path filePath = Path.of(
                "D:\\JavaDocGenerator\\src\\main\\java\\student\\javadoc/Main.java"
        );

        parser.parseFile(filePath);
    }

    /**
     * 
     * @throws IOException
     */
    private static void testCommentExtractor() throws IOException {

        Path filePath = Path.of(
                "D:\\\\JavaDocGenerator\\\\src\\\\main\\\\java\\\\student\\\\javadoc/Main.java"
        );

        CompilationUnit unit = StaticJavaParser.parse(filePath);
        CommentExtractor extractor = new CommentExtractor();

        for (var method : unit.findAll(MethodDeclaration.class)) {

            System.out.println("Метод: " + method.getNameAsString());

            var comment = extractor.extractComment(method);

            if (comment.isPresent()) {
                System.out.println("Описание: " + comment.get());
            } else {
                System.out.println("JavaDoc-комментарий отсутствует");
            }
        }
    }

    public static void testClassField() throws IOException {

        Path filePath = Path.of(
                "D:\\\\JavaDocGenerator\\\\src\\\\main\\\\java\\\\student\\\\javadoc/Main.java"
        );

        CompilationUnit unit = StaticJavaParser.parse(filePath);
        CommentExtractor comment = new CommentExtractor();

        for (var classDeclaration : unit.findAll(ClassOrInterfaceDeclaration.class)) {
            System.out.println("Класс: " + classDeclaration.getNameAsString());
            System.out.println("Описание: " + comment.extractComment(classDeclaration).orElse("Отсутствует"));

            for (var field : classDeclaration.getFields()) {
                System.out.println("Поле: " + field.getVariables().get(0).getNameAsString());
                System.out.println("Описание: " + comment.extractComment(field).orElse("Отсутствует"));

            }
        }
    } 

    public static void testClassInfo() {
        ClassInfo calculator = new ClassInfo.Builder("Calculator")
        .description("Выполняет математические операции")
        .addField(new FieldInfo("result", "int", "Результат"))
        .addMethod(new MethodInfo("add", "int", "Выводит результат", List.of("int a", "int b")))
        .build();

        System.out.println("Класс: " + calculator.getName());
        System.out.println("Описание: " + calculator.getDescription());

        for (FieldInfo field : calculator.getFields()) {
            System.out.println("Поле: " + field.getName() + ", тип " + field.getType());
        }

        for (MethodInfo method : calculator.getMethods()) {
            System.out.println("Метод: " + method.getName() + ", возвращает " + method.getType());
            System.out.println("Параметры: " + method.getParameters());
        }




    }
}
