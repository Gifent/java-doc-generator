package student.javadoc.Extractor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;

import student.javadoc.Info.JavaDocInfo;

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

    public JavaDocInfo extractJavadoc(Node node) {
        String description = "";
        Map<String, String> parameterDescriptions = new LinkedHashMap<>();
        String returnDescription = "";

        if (!(node instanceof NodeWithJavadoc<?> nodeWithJavadoc)) {
            return new JavaDocInfo(description, parameterDescriptions, returnDescription);
        }

        Optional<JavadocComment> comment = nodeWithJavadoc.getJavadocComment();

        if (comment.isEmpty()) {
            return new JavaDocInfo(description, parameterDescriptions, returnDescription);
        }

        Javadoc javadoc = comment.get().parse();

        description = javadoc.getDescription().toText();

        for (JavadocBlockTag tag : javadoc.getBlockTags()) {
            switch (tag.getType()) {
                case PARAM -> {
                    String parameterName = tag.getName().orElse("");
                    String parameterDescription = tag.getContent().toText();

                    parameterDescriptions.put(parameterName, parameterDescription);
                }

                case RETURN -> returnDescription = tag.getContent().toText();

                default -> {
                    
                }
            }
        }

        return new JavaDocInfo(description, parameterDescriptions, returnDescription);
    }
}
