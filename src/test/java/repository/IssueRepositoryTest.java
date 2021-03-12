package repository;

import domain.Issue;
import manager.IssueManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IssueRepositoryTest {
    IssueRepository repository = new IssueRepository();

    @Test
    void saveAllReports() {
        repository.saveAll(List.of(new Issue(), new Issue()));
        assertEquals(2, repository.findAll().size());
    }
}