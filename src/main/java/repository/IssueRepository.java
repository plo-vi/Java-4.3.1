package repository;

import domain.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class IssueRepository {
    private List<Issue> reports = new ArrayList<>();

    public void save(Issue report) {
        reports.add(report);
    }

    public List<Issue> findAll() {
        return reports;
    }

    public Issue findById(int id) {
        for (Issue report : reports) {
            if (report.getId() == id) {
                return report;
            }
        }
        return null;
    }

    public void removeById(int id) {
        reports.removeIf(r -> r.getId() == id);
    }

    public void saveAll(List<Issue> reports) {
        this.reports.addAll(reports);
    }

    public void removeAll(List<Issue> reports) {
        this.reports.removeAll(reports);
    }
}
