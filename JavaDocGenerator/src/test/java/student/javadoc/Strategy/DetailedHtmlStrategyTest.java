package student.javadoc.Strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import student.javadoc.Info.ClassInfo;
import student.javadoc.Info.FieldInfo;
import student.javadoc.Info.MethodInfo;

public class DetailedHtmlStrategyTest {

    private DetailedHtmlStrategy strategy;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        strategy = new DetailedHtmlStrategy();
    }

    private ClassInfo createClassInfo() {
        ClassInfo.Builder builder = new ClassInfo.Builder("User");

        builder.description("Класс пользователя.");

        builder.addField(
            new FieldInfo("name", "String", "Имя пользователя.")
        );

        builder.addMethod(
            new MethodInfo(
                "getName",
                "String",
                "Возвращает имя пользователя.",
                List.of("int id"),
                Map.of("id", "Идентификатор пользователя."),
                "Имя пользователя."
            )
        );

        return builder.build();
    }

    @Test
    void shouldCreateIndexFile() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        assertTrue(Files.exists(output.resolve("index.html")));
    }

    @Test
    void shouldAddClassNamesToIndex() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        String html = Files.readString(output.resolve("index.html"));

        assertTrue(html.contains("User"));
        assertTrue(html.contains("User.html"));
        assertTrue(html.contains("Вид: детализированный"));
    }

    @Test
    void shouldCreateClassPage() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        assertTrue(Files.exists(output.resolve("User.html")));
    }

    @Test
    void shouldAddClassDescription() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        String html = Files.readString(output.resolve("User.html"));

        assertTrue(html.contains("Класс пользователя."));
    }

    @Test
    void shouldAddFieldsToClassPage() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        String html = Files.readString(output.resolve("User.html"));

        assertTrue(html.contains("name"));
        assertTrue(html.contains("String"));
        assertTrue(html.contains("Имя пользователя."));
    }

    @Test
    void shouldAddMethodsAndParametersToClassPage() throws IOException {
        Path output = tempDir.resolve("documentation");

        strategy.generate(List.of(createClassInfo()), output);

        String html = Files.readString(output.resolve("User.html"));

        assertTrue(html.contains("getName"));
        assertTrue(html.contains("Возвращает имя пользователя."));
        assertTrue(html.contains("int id"));
        assertTrue(html.contains("Описание возвращаемого значения"));
    }
}