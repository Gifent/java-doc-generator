package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        testProjectAnalyzer();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к проекту: ");
        String sourceInput = scanner.nextLine();

        System.out.println("Введите путь для сохранения документации: ");
        String outputInput = scanner.nextLine();

        Path sourcePath = Path.of(sourceInput);
        Path outputPath = Path.of(outputInput);

        System.out.println("Путь к проекту: " + sourcePath.toAbsolutePath());
        System.out.println("Путь к документации: " + outputPath.toAbsolutePath());
    }

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
}
