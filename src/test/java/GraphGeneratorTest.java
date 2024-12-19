import org.eclipse.jgit.revwalk.RevCommit;
import org.example.GitRepositoryParser;
import org.example.GraphGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GraphGeneratorTest {

    @Test
    public void testGenerateDotFile() throws Exception {
        GitRepositoryParser parser = new GitRepositoryParser("D:\\Универ\\2_курс\\Java\\Hakaton");
        RevCommit lastCommit = parser.getLastCommit();

        GraphGenerator generator = new GraphGenerator();
        generator.generateDotFile("graph.dot", lastCommit);

        File file = new File("graph.dot");
        assertTrue(file.exists());
    }
}
