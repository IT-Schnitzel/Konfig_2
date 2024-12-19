package org.example;

import org.eclipse.jgit.revwalk.RevCommit;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

public class GraphGenerator {

    // Метод для генерации DOT файла
    public void generateDotFile(String outputPath, RevCommit commit) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("digraph G {\n");

            // Создаем граф зависимости для коммитов
            writeCommitGraph(writer, commit);

            writer.write("}\n");
        }
    }

    // Метод для записи коммитов и их зависимостей в граф
    private void writeCommitGraph(BufferedWriter writer, RevCommit commit) throws IOException {
        writer.write("    \"" + commit.getName() + "\" [label=\"" + commit.getAuthorIdent().getName() + "\n"
                + commit.getAuthorIdent().getWhen() + "\"];\n");

        // Обрабатываем родителей
        for (RevCommit parent : commit.getParents()) {
            writer.write("    \"" + commit.getName() + "\" -> \"" + parent.getName() + "\";\n");
            writeCommitGraph(writer, parent);
        }
    }

    // Метод для генерации изображения графа с использованием PlantUML
    public void generateImageFromDot(String dotContent, File outputImageFile) throws IOException {
        // Преобразуем DOT в PlantUML формат
        String plantUMLContent = convertDotToPlantUML(dotContent);

        // Сохраняем изображение
        try (FileOutputStream outputStream = new FileOutputStream(outputImageFile)) {
            SourceStringReader reader = new SourceStringReader(plantUMLContent);
            reader.generateImage(outputStream);
        }
    }

    // Преобразуем DOT в формат PlantUML
    private String convertDotToPlantUML(String dotContent) {
        return "@startuml\n" + dotContent + "\n@enduml";
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Текущий PATH:");
        System.out.println(System.getenv("PATH"));


        try {
            Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe", "-V").start(); // Укажите полный путь
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Graphviz доступен");
            } else {
                System.out.println("Ошибка при запуске Graphviz");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }




        // Путь к репозиторию
        GitRepositoryParser parser = new GitRepositoryParser("D:\\Универ\\2_курс\\Java\\Hakaton");
        RevCommit lastCommit = parser.getLastCommit();

        // Генерация DOT содержимого
        GraphGenerator generator = new GraphGenerator();
        String dotContent = generator.generateDotContent(lastCommit);

        // Путь для сохранения изображения
        File outputImageFile = new File("graph.png");  // Убедитесь, что директория существует

        // Проверка и создание родительской директории, если она не существует
        File parentDir = outputImageFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();  // Создайте директорию, если она не существует
        }

        // Генерация и сохранение изображения
        generator.generateImageFromDot(dotContent, outputImageFile);

        System.out.println("Изображение сохранено в: " + outputImageFile.getAbsolutePath());
    }

    // Метод для получения DOT содержимого графа
    public String generateDotContent(RevCommit commit) throws IOException {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph G {\n");
        writeCommitGraph(dotContent, commit);  // Теперь используем StringBuilder
        dotContent.append("}\n");
        return dotContent.toString();
    }

    // Обновленный метод записи графа в StringBuilder
    private void writeCommitGraph(StringBuilder dotContent, RevCommit commit) throws IOException {
        dotContent.append("    \"" + commit.getName() + "\" [label=\"" + commit.getAuthorIdent().getName() + "\n"
                + commit.getAuthorIdent().getWhen() + "\"];\n");

        // Обрабатываем родителей
        for (RevCommit parent : commit.getParents()) {
            dotContent.append("    \"" + commit.getName() + "\" -> \"" + parent.getName() + "\";\n");
            writeCommitGraph(dotContent, parent);
        }
    }
}
