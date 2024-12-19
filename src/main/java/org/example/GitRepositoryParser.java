package org.example;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import java.io.File;
import java.io.IOException;

public class GitRepositoryParser {

    private Repository repository;

    public GitRepositoryParser(String repoPath) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repository = builder.setGitDir(new File(repoPath + "/.git")).readEnvironment().findGitDir().build();
    }

    public RevCommit getLastCommit() throws IOException {
        Iterable<RevCommit> commits = null;
        try {
            commits = new Git(repository).log().call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return commits.iterator().next(); // Получаем последний коммит
    }

    public static void main(String[] args) throws Exception {
        GitRepositoryParser parser = new GitRepositoryParser("/path/to/repo");
        RevCommit lastCommit = parser.getLastCommit();
        System.out.println("Last Commit Hash: " + lastCommit.getName());
    }
}
