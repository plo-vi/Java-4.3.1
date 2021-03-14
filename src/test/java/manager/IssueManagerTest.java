package manager;

import domain.Issue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CRUDIssueManagerTest {
    private final IssueRepository repository = new IssueRepository();
    private final IssueManager manager = new IssueManager(repository);

    private final Issue first = new Issue(1, "N1", 10, "A1", Set.of("L1"), "P1", "M1", "As1", true);
    private final Issue second = new Issue(2, "N1", 20, "A2", Set.of("L2", "L8"), "P2", "M2", "As2", true);
    private final Issue third = new Issue(3, "N3", 30, "A2", Set.of("L3"), "P3", "M3", "As3", true);
    private final Issue fourth = new Issue(4, "N4", 40, "A4", Set.of("L4"), "P3", "M4", "As4", true);
    private final Issue fifth = new Issue(5, "N5", 90,"A5", Set.of("L5"), "P5", "M4", "As5", true);
    private final Issue sixth = new Issue(6, "N6", 64,"A6", Set.of("L6"), "P6", "M6", "As5", true);
    private final Issue seventh = new Issue(7, "N7", 41,"A1", Set.of("L7"), "P1", "M1", "As1", false);
    private final Issue eighth = new Issue(8, "N8", 1,"A1", Set.of("L8"), "P1", "M1", "As1", false);

    @Nested
    public class Empty {
        @Test
        void shouldAddAndGetOpenedIssues() {
            manager.add(first);
            List<Issue> actual = manager.getOpenedIssues();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldGetClosedIssues() {
            List<Issue> actual = manager.getClosedIssues();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByLabel() {
            List<Issue> actual = manager.filterByLabel("L5");
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortNothing() {
            List<Issue> actual = manager.sortByDate();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldOpenIssue() {
            assertThrows(NotFoundException.class, () -> manager.openIssue(1));
        }

        @Test
        void shouldCloseIssue() {
            assertThrows(NotFoundException.class, () -> manager.closeIssue(6));
        }
    }

    @Nested
    public class SingleItem {

        @BeforeEach
        public void setUp() {
            manager.add(first);
        }

        @Test
        void shouldGetOpenedIssues() {
            List<Issue> actual = manager.getOpenedIssues();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldGetClosedIssues() {
            List<Issue> actual = manager.getClosedIssues();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }


        @Test
        void shouldFilterByAuthor() {
            Issue[] actual = manager.searchBy("A1");
            Issue[] expected = new Issue[]{first};
            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldFilterByAssignee() {
            Issue[] actual = manager.searchBy("As1");
            Issue[] expected = new Issue[]{first};
            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldSortTheOne() {
            List<Issue> actual = manager.sortByDate();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldCloseIssue() {
            manager.closeIssue(1);
            boolean actual = repository.findById(1).isUpdate();
            assertFalse(actual);
        }

        @Test
        void shouldOpenIssueNotExist() {
            assertThrows(NotFoundException.class, () -> manager.closeIssue(2));
        }
    }

    @Nested
    public class MultipleItems {

        @BeforeEach
        public void setUp() {
            manager.add(first);
            manager.add(second);
            manager.add(third);
            manager.add(fourth);
            manager.add(fifth);
            manager.add(sixth);
            manager.add(seventh);
            manager.add(eighth);
        }

        @Test
        public void shouldGetAll() {
            Issue[] actual = repository.findAll().toArray(new Issue[0]);
            Issue[] expected = new Issue[]{first, second, third, fourth, fifth, sixth, seventh, eighth};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldAddNewIssue() {
            Issue ninth = new Issue(9, "N4", 40,"A9", Set.of("L9"), "P9", "M9", "As9", true);
            manager.add(ninth);
            Issue[] actual = repository.findAll().toArray(new Issue[0]);
            Issue[] expected = new Issue[]{first, second, third, fourth, fifth, sixth, seventh, eighth, ninth};
            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldSortFromNew() {
            List<Issue> actual = manager.sortByDate();
            List<Issue> expected = List.of(fifth, sixth, seventh, fourth, third, second, first, eighth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortFromOld() {
            List<Issue> actual = manager.sortByDateReverse();
            List<Issue> expected = List.of(eighth, first, second, third, fourth, seventh, sixth, fifth);
            assertEquals(expected, actual);
        }

        // Searching

        @Test
        public void shouldFindByName() {
            Issue[] actual = manager.searchBy("N3");
            Issue[] expected = new Issue[]{third};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldFindByAuthor() {
            Issue[] actual = manager.searchBy("A1");
            Issue[] expected = new Issue[]{first, seventh, eighth};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldFindByProject() {
            Issue[] actual = manager.searchBy("P3");
            Issue[] expected = new Issue[]{third, fourth};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldFindByMilestone() {
            Issue[] actual = manager.searchBy("M4");
            Issue[] expected = new Issue[]{fourth, fifth};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldFindByAssignee() {
            Issue[] actual = manager.searchBy("As5");
            Issue[] expected = new Issue[]{fifth, sixth};
            assertArrayEquals(expected, actual);
        }

        @Test
        public void shouldNotFindIfNoExists() {
            Issue[] actual = manager.searchBy("imagination");
            Issue[] expected = new Issue[]{};
            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldFilterByLabel() {
            List<Issue> actual = manager.filterByLabel("L2");
            List<Issue> expected = List.of(second);
            assertEquals(expected, actual);
        }

        // Closed-opened tricks

        @Test
        public void shouldFindOpenedIssues() {
            List<Issue> actual = manager.getOpenedIssues();
            List<Issue> expected = List.of(first, second, third, fourth, fifth, sixth);
            assertEquals(expected, actual);
        }

        @Test
        public void shouldFindClosedIssues() {
            List<Issue> actual = manager.getClosedIssues();
            List<Issue> expected = List.of(seventh, eighth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFindClosedIssueById() {
            manager.closeIssue(8);
            boolean actual = repository.findById(8).isUpdate();
            assertFalse(actual);
        }

        @Test
        void shouldFindOpenedIssueById() {
            manager.openIssue(3);
            boolean actual = repository.findById(3).isUpdate();
            assertEquals(true, actual);
        }

        @Test
        void shouldCloseOpenedIssue() {
            manager.closeIssue(1);
            boolean actual = repository.findById(1).isUpdate();
            assertFalse(actual);
        }

        @Test
        void shouldOpenClosedIssue() {
            manager.closeIssue(8);
            boolean actual = repository.findById(1).isUpdate();
            assertEquals(true, actual);
        }
    }
}