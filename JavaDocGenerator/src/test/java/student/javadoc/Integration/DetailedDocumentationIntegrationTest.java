package student.javadoc.Integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import student.javadoc.Facade.DocumentationFacade;
import student.javadoc.Strategy.DetailedHtmlStrategy;

public class DetailedDocumentationIntegrationTest {

    @TempDir
    Path tempDir;

    private Path sourcePath;
    private Path outputPath;

    @BeforeEach
    void setUp() throws IOException {
        sourcePath = Files.createDirectories(tempDir.resolve("source"));
        outputPath = tempDir.resolve("documentation");
    }

    @Test
    void shouldGenerateDetailedDocumentationForOneClass() throws IOException {
        String sourceCode = """
                /**
                 * Класс пользователя.
                 */
                public class User {

                    /** Имя пользователя. */
                    private String name;

                    /**
                     * Возвращает имя пользователя.
                     * @return имя пользователя
                     */
                    public String getName() {
                        return name;
                    }
                }
                """;

        Files.writeString(sourcePath.resolve("User.java"), sourceCode);

        DocumentationFacade facade = new DocumentationFacade(new DetailedHtmlStrategy());

        facade.generateDocumentation(sourcePath, outputPath);

        assertTrue(Files.exists(outputPath.resolve("index.html")));
        assertTrue(Files.exists(outputPath.resolve("User.html")));

        String html = Files.readString(outputPath.resolve("User.html"));

        assertTrue(html.contains("Класс пользователя."));
        assertTrue(html.contains("Имя пользователя."));
        assertTrue(html.contains("getName"));
        assertTrue(html.contains("Возвращает имя пользователя."));
    }

    @Test
    void shouldGenerateDocumentationForMultipleClasses() throws IOException {
        Files.writeString(sourcePath.resolve("User.java"), "public class User {}");
        Files.writeString(sourcePath.resolve("Product.java"), "public class Product {}");

        DocumentationFacade facade = new DocumentationFacade(new DetailedHtmlStrategy());

        facade.generateDocumentation(sourcePath, outputPath);

        assertTrue(Files.exists(outputPath.resolve("index.html")));
        assertTrue(Files.exists(outputPath.resolve("User.html")));
        assertTrue(Files.exists(outputPath.resolve("Product.html")));

        String indexHtml = Files.readString(outputPath.resolve("index.html"));

        assertTrue(indexHtml.contains("User"));
        assertTrue(indexHtml.contains("Product"));
    }

    @Test
    void shouldFindJavaFilesInSubdirectories() throws IOException {
        Path subdirectory = Files.createDirectories(sourcePath.resolve("models"));

        String sourceCode = """
                /**
                 * Класс пользователя.
                 */
            public class User {}
        """;

        Files.writeString(subdirectory.resolve("User.java"), sourceCode);

        DocumentationFacade facade = new DocumentationFacade(new DetailedHtmlStrategy());

        facade.generateDocumentation(sourcePath, outputPath);

        assertTrue(Files.exists(outputPath.resolve("index.html")));
        assertTrue(Files.exists(outputPath.resolve("User.html")));

        String html = Files.readString(outputPath.resolve("User.html"));

        assertTrue(html.contains("Класс пользователя."));
    }

    @Test
    void shouldThrowExceptionWhenNoJavaFilesFound() {
        DocumentationFacade facade = new DocumentationFacade(new DetailedHtmlStrategy());

        assertThrows(IllegalArgumentException.class, () -> facade.generateDocumentation(sourcePath, outputPath));

        assertFalse(Files.exists(outputPath));
    }
}