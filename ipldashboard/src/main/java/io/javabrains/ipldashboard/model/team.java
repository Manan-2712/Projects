package io.javabrains.ipldashboard.model;

import java.util.List;
import java.util.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class team {
    public team() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public String teamName;
    private long totalMatches;
    private long totalWins;
    @Transient
    private List<Match> matches;

    public team(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

    public long getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(long totalWins) {
        this.totalWins = totalWins;
    }

    @Override
    public String toString() {
        return "team teamName=" + teamName + ", totalMatches=" + totalMatches + ", totalWins="
                + totalWins + "]";
    }

}
