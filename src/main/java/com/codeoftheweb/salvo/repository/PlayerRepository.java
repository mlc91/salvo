package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//Rest Repository toma una instancia de clase y crea un JSON
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(@Param("email") String email);

}

