package io.javabrains.ipldashboard.data;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.javabrains.ipldashboard.model.team;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    @Autowired
    // private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    // public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    // this.jdbcTemplate = jdbcTemplate;
    // }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, team> teamData = new HashMap<>();

            entityManager.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new team((String) e[0], (long) e[1]))
                    .forEach(Team -> teamData.put(Team.getTeamName(), Team));

            entityManager.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        team team = teamData.get((String) e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                    });
            entityManager
                    .createQuery("select m.Matchwinner, count(*) from Match m group by m.Matchwinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        team team = teamData.get((String) e[0]);
                        if (team != null)
                            team.setTotalWins((long) e[1]);
                    });

            // jdbcTemplate.query("SELECT team1 ,team2 , date FROM Match",
            // (rs, row) -> " Team1 " + rs.getString(1) + " Team2 " + rs.getString(2) +
            // "Date " + rs.getString(3))
            // .forEach(str -> System.out.println(str));
            teamData.values().forEach(team -> entityManager.persist(team));
            teamData.values().forEach(team -> System.out.println(team));

            // jdbcTemplate.query("SELECT team1 ,team2 , date FROM Match",
            // (rs, row) -> " Team1 " + rs.getString(1) + " Team2 " + rs.getString(2) + "
            // Date "
            // + rs.getString(3))
            // .forEach(str -> System.out.println(str));
        }
    }
}
