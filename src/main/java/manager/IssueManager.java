package manager;

import domain.Issue;
import lombok.SneakyThrows;
import repository.IssueRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IssueManager {
    private IssueRepository repository;

    public IssueManager(IssueRepository repository) {
        this.repository = repository;
    }

    public void add (Issue issue) {
        repository.save(issue);
    }

    // Find issues with parameters

    public List<Issue> getAll() {
        return repository.findAll();
    }

    public List<Issue> getOpenedIssues() {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (issue.isUpdate()) {
                result.add(issue);
            }
        }
        return result;
    }

    public List<Issue> getClosedIssues() {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (!issue.isUpdate()) {
                result.add(issue);
            }
        }
        return result;
    }

    public boolean matches(Issue issue, String search) {
        if (issue.getName().equalsIgnoreCase(search)) {
            return true;
        }
        if (issue.getAuthor().equalsIgnoreCase(search)) {
            return true;
        }
        if (issue.getProject().equalsIgnoreCase(search)) {
            return true;
        }
        if (issue.getMilestone().equalsIgnoreCase(search)) {
            return true;
        }
        if (issue.getAssignee().equalsIgnoreCase(search)) {
            return true;
        }
        return false;
    }

    // Sorting

    private class IssueComparatorByDate implements java.util.Comparator<Issue> {
        @Override
        public int compare(Issue i1, Issue i2) {
            return i2.getDate() - i1.getDate();
        }

        @Override
        public Comparator<Issue> reversed() {
            return null;
        }
    }

    public  List<Issue> sortByDate(){
        List<Issue> result = this.getAll();
        result.sort(new IssueComparatorByDate());
        return result;
    }

    public List<Issue> sortByDateReverse() {
        List<Issue> result = this.getAll();
        result.sort(new IssueComparatorByDate().reversed());
        return result;
    }

    public void removeById(int id) {
        repository.removeById(id);
    }

    public Issue[] searchBy(String text) {
        Issue[] result = new Issue[0];
        for (Issue issue: repository.findAll()) {
            if (matches(issue, text)) {
                Issue[] tmp = new Issue[result.length + 1];
                System.arraycopy(result, 0, tmp, 0, result.length);
                tmp[tmp.length - 1] = issue;
                result = tmp;
            }
        }
        return result;
    }

    // Is opened?

    @SneakyThrows
    public void openIssue(int id) {
        Issue issue = repository.findById(id);
        if (issue == null) {
            throw new NotFoundException("Issue with id: " + id + " not found");
        } else if (!issue.isUpdate()) {
            issue.setUpdate(true);
        }
    }

    @SneakyThrows
    public void closeIssue(int id) {
        Issue issue = repository.findById(id);
        if (issue == null) {
            throw new NotFoundException("Issue with id: " + id + " not found");
        } else if (issue.isUpdate()) {
            issue.setUpdate(false);
        }
    }
}