package student.javadoc.Analyzer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ProjectAnalyzerTest {

    private ProjectAnalyzer analyzer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        analyzer = new ProjectAnalyzer();
    }

    @Test
    void shouldFindJavaFiles() throws IOException {
        Path javaFile = tempDir.resolve("Example.java");
        Files.writeString(javaFile, "public class Example {}");

        List<Path> result = analyzer.findJavaFiles(tempDir);

        assertEquals(1, result.size());
        assertEquals(javaFile, result.get(0));
    }

    @Test
    void shouldIgnoreNonJavaFiles() throws IOException {
        Files.writeString(tempDir.resolve("Example.java"), "class Example {}");
        Files.writeString(tempDir.resolve("notes.txt"), "Some text");
        Files.writeString(tempDir.resolve("data.csv"), "1,2,3");

        List<Path> result = analyzer.findJavaFiles(tempDir);

        assertEquals(1, result.size());
        assertTrue(result.get(0).toString().endsWith(".java"));
    }

    @Test
    void shouldFindJavaFilesInSubdirectories() throws IOException {
        Path subdirectory = Files.createDirectories(tempDir.resolve("subpackage"));
        Path javaFile = subdirectory.resolve("Example.java");

        Files.writeString(javaFile, "class Example {}");

        List<Path> result = analyzer.findJavaFiles(tempDir);

        assertEquals(1, result.size());
        assertEquals(javaFile, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenDirectoryDoesNotExist() {
        Path nonExistentDirectory = tempDir.resolve("unknown");

        assertThrows(
            IllegalArgumentException.class,
            () -> analyzer.findJavaFiles(nonExistentDirectory)
        );
    }

    @Test
    void shouldThrowExceptionWhenNoJavaFilesFound() throws IOException {
        Files.writeString(tempDir.resolve("notes.txt"), "Some text");

        assertThrows(
            IllegalArgumentException.class,
            () -> analyzer.findJavaFiles(tempDir)
        );
    }
}