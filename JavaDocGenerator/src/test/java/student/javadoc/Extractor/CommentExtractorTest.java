package student.javadoc.Extractor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;

public class CommentExtractorTest {

    private CommentExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new CommentExtractor();
    }

    @Test
    void shouldExtractClassComment() {
        var node = StaticJavaParser.parse("""
                    /** Описание класса. */
                    public class Example {}
                """).getClassByName("Example").orElseThrow();

        var result = extractor.extractComment(node);

        assertTrue(result.isPresent());
        assertEquals("Описание класса.", result.get().trim());
    }

    @Test
    void shouldReturnEmptyWhenClassHasNoComment() {
        var node = StaticJavaParser.parse("""
                    public class Example {}
                """).getClassByName("Example").orElseThrow();

        var result = extractor.extractComment(node);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyForNodeWithoutJavadocSupport() {
        BinaryExpr node = StaticJavaParser.parseExpression("1 + 2")
                .asBinaryExpr();

        var result = extractor.extractComment(node);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExtractMultilineComment() {
        var node = StaticJavaParser.parse("""
                    /**
                     * Первая строка.
                     * Вторая строка.
                     */
                    public class Example {}
                """).getClassByName("Example").orElseThrow();

        var result = extractor.extractComment(node);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains("Первая строка."));
        assertTrue(result.get().contains("Вторая строка."));
    }

    @Test
    void shouldExtractMethodComment() {
        CompilationUnit unit = StaticJavaParser.parse("""
                    public class Example {
                        /** Возвращает имя. */
                        public String getName() {
                            return "Alex";
                        }
                    }
                """);

        var method = unit.getClassByName("Example").orElseThrow().getMethodsByName("getName").get(0);

        var result = extractor.extractComment(method);

        assertTrue(result.isPresent());
        assertEquals("Возвращает имя.", result.get().trim());
    }

    @Test
    void shouldExtractJavadocDescription() {
        var node = StaticJavaParser.parse("""
                    /**
                     * Описание класса.
                     */
                    public class Example {}
                """).getClassByName("Example").orElseThrow();

        var result = extractor.extractJavadoc(node);

        assertEquals("Описание класса.", result.getDescription().trim());
    }

    @Test
    void shouldExtractMultipleParameterDescriptions() {
        var unit = StaticJavaParser.parse("""
                    public class Example {
                        /**
                         * Складывает два числа.
                         * @param a первое число
                         * @param b второе число
                         */
                        public int sum(int a, int b) {
                            return a + b;
                        }
                    }
                """);

        var method = unit.getClassByName("Example").orElseThrow()
                .getMethodsByName("sum").get(0);

        var result = extractor.extractJavadoc(method);

        assertEquals("первое число", result.getParameterDescriptions().get("a").trim());
        assertEquals("второе число", result.getParameterDescriptions().get("b").trim());
        assertEquals(2, result.getParameterDescriptions().size());
    }

    @Test
    void shouldExtractReturnDescription() {
        var unit = StaticJavaParser.parse("""
                    public class Example {
                        /**
                         * Возвращает имя.
                         * @return имя пользователя
                         */
                        public String getName() {
                            return "Alex";
                        }
                    }
                """);

        var method = unit.getClassByName("Example").orElseThrow()
                .getMethodsByName("getName").get(0);

        var result = extractor.extractJavadoc(method);

        assertEquals("имя пользователя", result.getReturnDescription().trim());
    }

    @Test
    void shouldReturnEmptyJavadocInfoWhenNodeHasNoComment() {
        var node = StaticJavaParser.parse("""
                    public class Example {}
                """).getClassByName("Example").orElseThrow();

        var result = extractor.extractJavadoc(node);

        assertEquals("", result.getDescription());
        assertTrue(result.getParameterDescriptions().isEmpty());
        assertEquals("", result.getReturnDescription());
    }

}