package domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Issue {
    private int id;
    private String name;
    private int date; //number of days an issue was updated ago
    private String author;
    private Set<String> label;
    private String project;
    private String milestone;
    private String assignee;
    private boolean update;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return id == issue.id && date == issue.date && update == issue.update && Objects.equals(name, issue.name) && Objects.equals(author, issue.author) && Objects.equals(label, issue.label) && Objects.equals(project, issue.project) && Objects.equals(milestone, issue.milestone) && Objects.equals(assignee, issue.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, author, label, project, milestone, assignee, update);
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", label=" + label +
                ", project='" + project + '\'' +
                ", milestone='" + milestone + '\'' +
                ", assignee='" + assignee + '\'' +
                ", update=" + update +
                '}';
    }
}
