package student.javadoc.Facade;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import student.javadoc.Info.ClassInfo;
import student.javadoc.Strategy.HtmlGeneretionStrategy;

public class DocumentationFacadeTest {

    private DocumentationFacade facade;
    private RecordingStrategy strategy;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        strategy = new RecordingStrategy();
        facade = new DocumentationFacade(strategy);
    }

    private Path createSourceDirectory() throws IOException {
        Path source = tempDir.resolve("source");
        Files.createDirectories(source);
        return source;
    }

    private void createJavaFile(Path directory, String fileName, String content) throws IOException {
        Files.writeString(directory.resolve(fileName), content);
    }

    @Test
    void shouldGenerateDocumentationForOneClass() throws IOException {
        Path source = createSourceDirectory();

        createJavaFile(source, "Example.java", "public class Example {}");

        Path output = tempDir.resolve("output");

        facade.generateDocumentation(source, output);

        assertEquals(1, strategy.classes.size());
        assertEquals("Example", strategy.classes.get(0).getName());
    }

    @Test
    void shouldCollectClassesFromMultipleFiles() throws IOException {
        Path source = createSourceDirectory();

        createJavaFile(source, "First.java", "public class First {}");
        createJavaFile(source, "Second.java", "public class Second {}");

        facade.generateDocumentation(source, tempDir.resolve("output"));

        assertEquals(2, strategy.classes.size());

        assertTrue(strategy.classes.stream()
            .anyMatch(classInfo -> classInfo.getName().equals("First")));

        assertTrue(strategy.classes.stream()
            .anyMatch(classInfo -> classInfo.getName().equals("Second")));
    }

    @Test
    void shouldFindJavaFilesInSubdirectories() throws IOException {
        Path source = createSourceDirectory();
        Path subdirectory = Files.createDirectories(source.resolve("subpackage"));

        createJavaFile(subdirectory, "Example.java", "public class Example {}");

        facade.generateDocumentation(source, tempDir.resolve("output"));

        assertEquals(1, strategy.classes.size());
        assertEquals("Example", strategy.classes.get(0).getName());
    }

    @Test
    void shouldIgnoreNonJavaFiles() throws IOException {
        Path source = createSourceDirectory();

        createJavaFile(source, "Example.java", "public class Example {}");
        Files.writeString(source.resolve("notes.txt"), "Some text");

        facade.generateDocumentation(source, tempDir.resolve("output"));

        assertEquals(1, strategy.classes.size());
    }

    @Test
    void shouldThrowExceptionWhenNoJavaFilesFound() throws IOException {
        Path source = createSourceDirectory();

        assertThrows(
            IllegalArgumentException.class,
            () -> facade.generateDocumentation(source, tempDir.resolve("output"))
        );

        assertEquals(0, strategy.calls);
    }

    @Test
    void shouldPassCorrectOutputPathToStrategy() throws IOException {
        Path source = createSourceDirectory();
        createJavaFile(source, "Example.java", "public class Example {}");

        Path output = tempDir.resolve("documentation");

        facade.generateDocumentation(source, output);

        assertEquals(output, strategy.outputPath);
    }

    private static class RecordingStrategy implements HtmlGeneretionStrategy {

        private List<ClassInfo> classes;
        private Path outputPath;
        private int calls = 0;

        @Override
        public void generate(List<ClassInfo> classes, Path outputPath) {
            this.classes = classes;
            this.outputPath = outputPath;
            calls++;
        }
    }
}