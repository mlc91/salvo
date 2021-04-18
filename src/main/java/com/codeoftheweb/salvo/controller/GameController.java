package com.codeoftheweb.salvo.controller;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.controller.Util.isGuest;
import static com.codeoftheweb.salvo.controller.Util.makeMap;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    //----------------------------------------GET GAMES---------------------------------------------------------------//
    @GetMapping("/games")
    private Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if(!isGuest(authentication)){
            dto.put("player", playerRepository.findByEmail(authentication.getName()).playerDTO());
        } else{
            dto.put("player", "Guest");
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.gameDTO()).collect(Collectors.toList()));

        return dto;
    }
    //----------------------------------------CREATE GAME-------------------------------------------------------------//
    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication){

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unauthorized player"), HttpStatus.UNAUTHORIZED);

        } else {
            Game game = gameRepository.save(new Game());
            Player player = playerRepository.findByEmail(authentication.getName());

            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));

            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }
    //----------------------------------------JOIN GAME---------------------------------------------------------------//
    @RequestMapping("/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {
        Optional<Game> game = gameRepository.findById(gameId);

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unauthorized player"), HttpStatus.UNAUTHORIZED);
        }
        else if (game.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);

        }
        else if (game.get().getGamePlayer().size() == 2){

            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        } else {
            Player player = playerRepository.findByEmail(authentication.getName());
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game.get()));

            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }
}