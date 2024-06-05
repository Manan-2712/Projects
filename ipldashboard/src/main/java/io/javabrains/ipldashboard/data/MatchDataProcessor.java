package io.javabrains.ipldashboard.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import io.javabrains.ipldashboard.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchinput) throws Exception {
        Match match = new Match();
        match.setId(Long.parseLong(matchinput.getId()));
        match.setCity(matchinput.getCity());
        match.setPlayerOfMatch(matchinput.getPlayer_of_match());
        match.setVenue(matchinput.getVenue());
        String dateString = matchinput.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        match.setDate(date);
        String firstInningTeam, secondInningTeam;
        if ("bat".equals(matchinput.getToss_decision())) {
            firstInningTeam = matchinput.getToss_winner();
            secondInningTeam = matchinput.getToss_winner().equals(matchinput.getTeam1()) ? matchinput.getTeam2()
                    : matchinput.getTeam1();

        } else {
            secondInningTeam = matchinput.getToss_winner();
            firstInningTeam = matchinput.getToss_winner().equals(matchinput.getTeam1()) ? matchinput.getTeam2()
                    : matchinput.getTeam1();

        }
        match.setTeam1(firstInningTeam);
        match.setTeam2(secondInningTeam);
        match.setTossWinner(matchinput.getToss_winner());
        match.setTossDecision(matchinput.getToss_decision());
        match.setMatchwinner(matchinput.getWinner());
        match.setResult(matchinput.getResult());
        match.setResultMargin(matchinput.getResult_margin());
        match.setUmpire1(matchinput.getUmpire1());
        match.setUmpire2(matchinput.getUmpire2());
        return match;

    }

}
