package student.javadoc;

import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;

public class CommentExtractor {
    
    public Optional<String> extractComment(Node node) {
        if (node instanceof NodeWithJavadoc <?> nodeWithJavadoc) {
            Optional<JavadocComment> comment = nodeWithJavadoc.getJavadocComment();

            if (comment.isPresent()) {
                return Optional.of(comment.get().parse().toText());
            }
        }

        return Optional.empty();
    }

}
