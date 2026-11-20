package student.javadoc.Integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import student.javadoc.Facade.DocumentationFacade;
import student.javadoc.Strategy.CompactHtmlStrategy;

public class CompactDocumentationIntegrationTest {

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
    void shouldGenerateCompactDocumentationForOneClass() throws IOException {
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

        DocumentationFacade facade = new DocumentationFacade(new CompactHtmlStrategy());

        facade.generateDocumentation(sourcePath, outputPath);

        assertTrue(Files.exists(outputPath.resolve("index.html")));
        assertTrue(Files.exists(outputPath.resolve("User.html")));

        String html = Files.readString(outputPath.resolve("User.html"));

        assertFalse(html.contains("Имя пользователя."));
        assertFalse(html.contains("Возвращает имя пользователя."));
    }
}