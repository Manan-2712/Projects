package io.javabrains.ipldashboard.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.ipldashboard.model.team;
import io.javabrains.ipldashboard.Repository.MatchRepository;
import io.javabrains.ipldashboard.Repository.TeamRepository;

@RestController
@CrossOrigin
public class teamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public teamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team")
    public Iterable<team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}")
    public team getTeam(@PathVariable String teamName) {
        team team = this.teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesbyTeam(teamName, 4));

        return team;
    }

}
