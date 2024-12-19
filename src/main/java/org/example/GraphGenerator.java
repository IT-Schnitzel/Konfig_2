package org.example;

import org.eclipse.jgit.revwalk.RevCommit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphGenerator {

    public void generateDotFile(String outputPath, RevCommit commit) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("digraph G {\n");

            // Создаем граф зависимости для коммитов
            writeCommitGraph(writer, commit);

            writer.write("}\n");
        }
    }

    private void writeCommitGraph(BufferedWriter writer, RevCommit commit) throws IOException {
        writer.write("    \"" + commit.getName() + "\" [label=\"" + commit.getAuthorIdent().getName() + "\n"
                + commit.getAuthorIdent().getWhen() + "\"];\n");

        // Обрабатываем родителей
        for (RevCommit parent : commit.getParents()) {
            writer.write("    \"" + commit.getName() + "\" -> \"" + parent.getName() + "\";\n");
            writeCommitGraph(writer, parent);
        }
    }

    public static void main(String[] args) throws Exception {
        GitRepositoryParser parser = new GitRepositoryParser("/path/to/repo");
        RevCommit lastCommit = parser.getLastCommit();

        GraphGenerator generator = new GraphGenerator();
        generator.generateDotFile("graph.dot", lastCommit);
    }
}

