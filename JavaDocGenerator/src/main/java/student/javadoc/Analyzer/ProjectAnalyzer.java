package student.javadoc.Analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ProjectAnalyzer {

    public List<Path> findJavaFiles(Path sourcePath) throws IOException {
            if (!Files.exists(sourcePath)) {
                throw new IllegalArgumentException("Исходный каталог не существует: " + sourcePath);
            }
        
            if (!Files.isDirectory(sourcePath)) {
                throw new IllegalArgumentException("Указанный путь не является каталогом: " + sourcePath);
            }

            try (Stream<Path> paths = Files.walk(sourcePath)) {
                List<Path> javaFiles = paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).toList();

                if (javaFiles.isEmpty()) {
                    throw new IllegalArgumentException("В каталоге не найдено Java-файлов: " + sourcePath);
                }
            
                return javaFiles;
            }

        }
    }          

