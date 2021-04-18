package com.codeoftheweb.salvo.controller;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.controller.Util.isGuest;
import static com.codeoftheweb.salvo.controller.Util.makeMap;

@RestController
@RequestMapping("/api")
public class ApplicationController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    ScoreRepository scoreRepository;
    //----------------------------------------GAME VIEW---------------------------------------------------------------//
    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gameView(@PathVariable Long gamePlayerId, Authentication authentication) {
        if(isGuest(authentication)){
            return new  ResponseEntity<>(makeMap("error","Not allowed"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if(player ==  null){
            return new  ResponseEntity<>(makeMap("error","There is no player with this ID"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer ==  null ){
            return new  ResponseEntity<>(makeMap("error","There is no gamePlayer with this ID"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getPlayer().getId() != player.getId()){
            return new  ResponseEntity<>(makeMap("error", "The player and the gamePlayer have a different ID"),HttpStatus.CONFLICT);
        }

        Map<String,  Object>  dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();

        Optional<GamePlayer> opponent = gamePlayer.getOpponent();

        if (opponent.isPresent()) {
            hits.put("self", hitsAndSink(gamePlayer, opponent.get()));
            hits.put("opponent", hitsAndSink(opponent.get(), gamePlayer));
        } else {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created",  gamePlayer.getGame().getCreateDate());
        dto.put("gameState", gameState(gamePlayer));

        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayer()
                .stream()
                .map(gamePlayer1 -> gamePlayer1.gamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships",  gamePlayer.getShips()
                .stream()
                .map(ship -> ship.shipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes",  gamePlayer.getGame().getGamePlayer()
                .stream()
                .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                        .stream()
                        .map(salvo -> salvo.salvoDTO()))
                .collect(Collectors.toList()));
        dto.put("hits", hits);

        return  new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //----------------------------------------HITS AND SINK-----------------------------------------------------------//
    //This function returns a list of a Map whit the current information in the game:
    // turn, hitLocations, damages (by turn and type of ship) and the missed shots
    private List<Map> hitsAndSink(GamePlayer self, GamePlayer opponent){

        List<Map> totalMap = new ArrayList<>();

        List<String> carrierLocation = findShipLocations(self, "carrier");
        List<String> battleshipLocation = findShipLocations(self, "battleship");
        List<String> submarineLocation = findShipLocations(self, "submarine");
        List<String> destroyerLocation = findShipLocations(self, "destroyer");
        List<String> patrolboatLocation = findShipLocations(self, "patrolboat");

        int carrierDamage = 0;
        int battleshipDamage = 0;
        int submarineDamage = 0;
        int destroyerDamage = 0;
        int patrolboatDamage = 0;

        //itero por lista de salvos luego por posiciones
        for (Salvo salvo : opponent.getSalvoes()) {

            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Object> map2 = new LinkedHashMap<>();

            int carrierHitsInTurn = 0;
            int battleshipHitsInTurn = 0;
            int submarineHitsInTurn = 0;
            int destroyerHitsInTurn = 0;
            int patrolboatHitsInTurn = 0;

            ArrayList<String> hitCellsList = new ArrayList<>();

            int missedShots = salvo.getSalvoLocations().size();

            for (String location : salvo.getSalvoLocations()) {

                if (carrierLocation.contains(location)) {
                    carrierDamage++;
                    carrierHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (battleshipLocation.contains(location)) {
                    battleshipDamage++;
                    battleshipHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (submarineLocation.contains(location)) {
                    submarineDamage++;
                    submarineHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (destroyerLocation.contains(location)) {
                    destroyerDamage++;
                    destroyerHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (patrolboatLocation.contains(location)) {
                    patrolboatDamage++;
                    patrolboatHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }
            }

            map2.put("carrierHits", carrierHitsInTurn);
            map2.put("battleshipHits", battleshipHitsInTurn);
            map2.put("submarineHits", submarineHitsInTurn);
            map2.put("destroyerHits", destroyerHitsInTurn);
            map2.put("patrolboatHits", patrolboatHitsInTurn);

            map2.put("carrier", carrierDamage);
            map2.put("battleship", battleshipDamage);
            map2.put("submarine", submarineDamage);
            map2.put("destroyer", destroyerDamage);
            map2.put("patrolboat", patrolboatDamage);

            map.put("turn", salvo.getTurn());
            map.put("hitLocations", hitCellsList);
            map.put("damages", map2);
            map.put("missed", missedShots);

            totalMap.add(map);
        }
        return totalMap;
    }
    //----------------------------------------FIND SHIP LOCATIONS-----------------------------------------------------//
    //This function returns a List with all locations of a ship type given
    public List<String> findShipLocations(GamePlayer gamePlayer, String type) {
        Optional<Ship> response;
        response = gamePlayer.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst();
        if (response.isEmpty()) {
            return new ArrayList<String>();
        }
        return response.get().getLocations();
    }
    //----------------------------------------GET CURRENT TURN--------------------------------------------------------//
    private int getCurrentTurn(GamePlayer self, GamePlayer opponent){
        int selfSalvoes = self.getSalvoes().size();
        int opponentSalvoes = opponent.getSalvoes().size();

        int sumSalvoes = selfSalvoes + opponentSalvoes;

        return (sumSalvoes % 2 == 0)? (sumSalvoes/2 + 1) : ((int)sumSalvoes/2 + 1);
    }
    //----------------------------------------GAME STATE--------------------------------------------------------------//
    public String gameState(GamePlayer self){

        if(self.getShips().size() == 0){
            return "PLACESHIPS";
        }
        if(self.getOpponent().isEmpty()) {
            return "WAITINGFOROPP";
        }

        if(self.getSalvoes().size() > self.getOpponent().get().getSalvoes().size()){
            return "WAIT";
        }
        if(self.getOpponent().get().getShips().size() == 0){
            return "WAIT";
        }
        if(self.getSalvoes().size() < self.getOpponent().get().getSalvoes().size()){
            return "PLAY";
        }

        int turn = getCurrentTurn(self, self.getOpponent().get());

        if(self.getSalvoes().size() == self.getOpponent().get().getSalvoes().size()){
            if(getIfAllSunk(self.getOpponent().get(), self) && getIfAllSunk(self, self.getOpponent().get())){
                scoreRepository.save(new Score(0.5, self.getPlayer(), self.getGame()));
                return "TIE";
            }
            if(getIfAllSunk(self.getOpponent().get(), self)){
                scoreRepository.save(new Score(1.0, self.getPlayer(), self.getGame()));
                return "WON";
            }
            if(getIfAllSunk(self, self.getOpponent().get())){
                scoreRepository.save(new Score(0.0, self.getPlayer(), self.getGame()));
                return "LOST";
            }
            if(self.getSalvoes().size() != turn && self.getJoinDate().before(self.getOpponent().get().getJoinDate())){
                return "PLAY";
            }
        }
        return "WAIT";
    }
    //----------------------------------------GET IF ALL SUNK---------------------------------------------------------//
    //This function returns "true" if all ships of the current player are sunk, checking with the salvoes of the opponent
    private Boolean getIfAllSunk (GamePlayer self, GamePlayer opponent) {
        if (!opponent.getShips().isEmpty() && !self.getSalvoes().isEmpty()) {
            return opponent.getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList()).containsAll(self.getShips().stream()
                    .flatMap(ship -> ship.getLocations().stream()).collect(Collectors.toList()));
        }
        return false;
    }
}
