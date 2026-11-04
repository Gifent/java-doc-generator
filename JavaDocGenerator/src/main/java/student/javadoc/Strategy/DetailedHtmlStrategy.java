package student.javadoc.Strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import student.javadoc.Info.ClassInfo;
import student.javadoc.Info.FieldInfo;
import student.javadoc.Info.MethodInfo;

public class DetailedHtmlStrategy implements HtmlGeneretionStrategy {
    
    @Override 
    public void generate(List<ClassInfo> classes, Path outputPath) throws IOException {
            Files.createDirectories(outputPath);
            String indexhtml = generateIndex(classes);

            Files.writeString(outputPath.resolve("index.html"), indexhtml);

            for (ClassInfo classInfo : classes) {
                String classHtml = generateClassPage(classInfo);
                Files.writeString(outputPath.resolve(classInfo.getName() + ".html"), classHtml);
            }
        }

    private String generateIndex(List<ClassInfo> classes) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Документация</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>Документация проекта</h1>");
        html.append("<p>Вид: детализированный</p>");
        html.append("<h2>Список классов</h2>");
        html.append("<ul>");

        for (ClassInfo classInfo: classes) {
            html.append("<li>");
            html.append("<a href=\"").append(classInfo.getName()).append(".html\">");
            html.append(classInfo.getName());
            html.append("</a>");
            html.append("</li>");
        }

        html.append("</ul>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private String generateClassPage(ClassInfo classInfo) {
        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<head>");
        html.append("<title>").append(classInfo.getName()).append("</title>");
        html.append("</head>");
        html.append("<body>");

        html.append("<h1>").append(classInfo.getName()).append("</h1>");
        html.append("<p>").append(classInfo.getDescription()).append("</p>");

        html.append("<h2>Поля</h2>");
        html.append("<ul>");
        for (FieldInfo field : classInfo.getFields()) {
            html.append("<li>");
            html.append("<strong>").append(field.getName()).append("</strong>");
            html.append(" (type: ").append(field.getType()).append(")");
            html.append("<p>").append(field.getDescription()).append("</p>");
            html.append("</li>");
        } 
        html.append("</ul>");

        html.append("<h2>Методы</h2>");
        html.append("<ul>");
        for (MethodInfo method : classInfo.getMethods()) {
            html.append("<li>");
            html.append("<h3>").append(method.getName()).append("</h3>");
            html.append("<p>Тип возвращаемого значения: ").append(method.getType()).append("</p>");
            html.append("<p>").append(method.getDescription()).append("</p>");
            html.append("<h4>Параметры</h4>");
            html.append("<ul>");
            for (String parameter : method.getParameters()) {
                html.append("<li>").append(parameter).append("</li>");
            }
            html.append("</ul>");
            html.append("<p>Описание возвращаемого значения: ").append(method.getReturnDescription()).append("</p>");
            html.append("</li>");
        }
        html.append("</ul>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
