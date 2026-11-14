package student.javadoc.Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import student.javadoc.Info.ClassInfo;

public class JavaFileParserTest {

    private JavaFileParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new JavaFileParser();
    }

    private Path createJavaFile(String fileName, String source) throws IOException {
        Path file = tempDir.resolve(fileName);
        Files.writeString(file, source);
        return file;
    }

    @Test
    void shouldParseOneClass() throws IOException {
        Path file = createJavaFile(
            "Example.java",
            "public class Example {}"
        );

        List<ClassInfo> result = parser.parseClasses(file);

        assertEquals(1, result.size());
        assertEquals("Example", result.get(0).getName());
    }

    @Test
    void shouldExtractClassDescription() throws IOException {
        Path file = createJavaFile(
            "Example.java",
            """
            /**
             * Описание класса.
             */
            public class Example {}
            """
        );

        List<ClassInfo> result = parser.parseClasses(file);

        assertEquals("Описание класса.", result.get(0).getDescription().trim());
    }

    @Test
    void shouldParseFields() throws IOException {
        Path file = createJavaFile(
            "Example.java",
            """
            public class Example {
                /** Имя пользователя. */
                private String name;

                private int age;
            }
            """
        );

        List<ClassInfo> result = parser.parseClasses(file);
        var fields = result.get(0).getFields();

        assertEquals(2, fields.size());

        assertEquals("name", fields.get(0).getName());
        assertEquals("String", fields.get(0).getType());
        assertEquals("Имя пользователя.", fields.get(0).getDescription().trim());

        assertEquals("age", fields.get(1).getName());
        assertEquals("int", fields.get(1).getType());
    }

    @Test
    void shouldParseMethods() throws IOException {
        Path file = createJavaFile(
            "Example.java",
            """
            public class Example {
                public String getName() {
                    return "";
                }

                public int getAge() {
                    return 0;
                }
            }
            """
        );

        List<ClassInfo> result = parser.parseClasses(file);
        var methods = result.get(0).getMethods();

        assertEquals(2, methods.size());

        assertEquals("getName", methods.get(0).getName());
        assertEquals("String", methods.get(0).getType());

        assertEquals("getAge", methods.get(1).getName());
        assertEquals("int", methods.get(1).getType());
    }

    @Test
    void shouldParseMethodParameters() throws IOException {
        Path file = createJavaFile(
            "Example.java",
            """
            public class Example {
                public String getFullName(String firstName, int age) {
                    return "";
                }
            }
            """
        );

        List<ClassInfo> result = parser.parseClasses(file);
        var methods = result.get(0).getMethods();

        assertEquals(
            List.of("String firstName", "int age"),
            methods.get(0).getParameters()
        );
    }
}