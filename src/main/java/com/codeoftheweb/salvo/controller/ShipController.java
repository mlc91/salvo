package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

import static com.codeoftheweb.salvo.controller.Util.isGuest;
import static com.codeoftheweb.salvo.controller.Util.makeMap;

@RestController
@RequestMapping("/api")
public class ShipController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    //----------------------------------------ADD SHIPS---------------------------------------------------------------//
    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable Long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication) {

        if(isGuest(authentication)){
            return new  ResponseEntity<>(makeMap("error","Not allowed"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if(gamePlayer == null){
            return new  ResponseEntity<>(makeMap("error","GamePlayer not found"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(makeMap("error","Player not belongs to this gamePlayer"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getShips().size() > 0){
            return new ResponseEntity<>(makeMap("error", "Ships are placed"), HttpStatus.FORBIDDEN);

        }else{
            if(ships.size() > 0){
                gamePlayer.addListShips(ships);
                gamePlayerRepository.save(gamePlayer);

                return new ResponseEntity<>(makeMap("OK", "Ships created successfully"), HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>(makeMap("error", "There is no ships"), HttpStatus.FORBIDDEN);
            }
        }
    }
}
