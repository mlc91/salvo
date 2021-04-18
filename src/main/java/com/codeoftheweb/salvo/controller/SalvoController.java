package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.codeoftheweb.salvo.controller.Util.isGuest;
import static com.codeoftheweb.salvo.controller.Util.makeMap;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    SalvoRepository salvoRepository;
    //----------------------------------------STORE SALVOES-----------------------------------------------------------//
    @PostMapping ("games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> storeSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {
        ResponseEntity<Map<String, Object>> responseEntity;

        if(isGuest(authentication)){
            responseEntity = new ResponseEntity<>(makeMap("error","Is no current user logged in"), HttpStatus.UNAUTHORIZED);

        }
        Player player = playerRepository.findByEmail(authentication.getName());
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);

        if (gamePlayer.isEmpty()) {
            responseEntity = new ResponseEntity<>(makeMap("error", "GamePlayer not found"), HttpStatus.NOT_FOUND);
        }

        if (gamePlayer.get().getPlayer().getId() != player.getId()) {
            responseEntity = new ResponseEntity<>(makeMap("error", "Player not belongs to this gamePlayer"), HttpStatus.UNAUTHORIZED);
        }
        Optional<GamePlayer> gpOpponent = gamePlayer.get().getOpponent();

        if (gpOpponent.get().getId() == null) {
            responseEntity = new ResponseEntity<>(makeMap("error", "Wait for your opponent"), HttpStatus.FORBIDDEN);
        }
        if (gamePlayer.get().getSalvoes().size() > gpOpponent.get().getSalvoes().size()) {
            responseEntity = new ResponseEntity<>(makeMap("error", "The user already has submitted a salvo"), HttpStatus.FORBIDDEN);
        } else {
            salvo.setTurn(gamePlayer.get().getSalvoes().size() + 1);
            salvo.setGamePlayer(gamePlayer.get());
            salvoRepository.save(salvo);

            responseEntity = new ResponseEntity<>(makeMap("OK", "Salvo added successfully"), HttpStatus.CREATED);
        }

        return responseEntity;
    }
}