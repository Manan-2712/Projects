package io.javabrains.ipldashboard.Repository;

import org.springframework.data.repository.CrudRepository;

import io.javabrains.ipldashboard.model.team;

public interface TeamRepository extends CrudRepository<team, Long> {

    team findByTeamName(String teamName);

}
