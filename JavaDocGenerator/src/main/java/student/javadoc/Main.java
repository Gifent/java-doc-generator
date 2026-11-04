package student.javadoc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import student.javadoc.Facade.DocumentationFacade;
import student.javadoc.Strategy.CompactHtmlStrategy;
import student.javadoc.Strategy.DetailedHtmlStrategy;
import student.javadoc.Strategy.HtmlGeneretionStrategy;

public class Main {
    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к проекту: ");
        String sourceInput = scanner.nextLine();

        System.out.println("Введите путь для сохранения документации: ");
        String outputInput = scanner.nextLine();

        Path sourcePath = Path.of(sourceInput);
        Path outputPath = Path.of(outputInput);

        System.out.println("Выбор вида документации: ");
        System.out.println("  1 - компактный");
        System.out.println("  2 - детализированный");
        String number = scanner.nextLine();

        switch (number) {
            case "1":
                HtmlGeneretionStrategy strategyCompact = new CompactHtmlStrategy();
                DocumentationFacade facade = new DocumentationFacade(strategyCompact);
                facade.generateDocumentation(sourcePath, outputPath);

                System.out.println("Результат: " + outputPath.toAbsolutePath());
                break;
            
            case "2":
                HtmlGeneretionStrategy strategyDetailed = new DetailedHtmlStrategy();
                DocumentationFacade facade1 = new DocumentationFacade(strategyDetailed);
                facade1.generateDocumentation(sourcePath, outputPath);

                System.out.println("Результат: " + outputPath.toAbsolutePath());
                break;
            default:
                break;
        }
        scanner.close();
    }
}
