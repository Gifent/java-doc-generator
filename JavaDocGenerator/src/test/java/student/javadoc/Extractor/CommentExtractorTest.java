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

}