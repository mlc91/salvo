package com.codeoftheweb.salvo.controller;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.controller.Util.makeMap;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //----------------------------------------GET PLAYERS-------------------------------------------------------------//
    @GetMapping("/players")
    private List<Object> getPlayers() {
        return playerRepository.findAll().stream().map(player -> player.playerDTO()).collect(Collectors.toList());
    }
    //----------------------------------------ADD PLAYER--------------------------------------------------------------//
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addPlayer(@RequestParam String email, @RequestParam String password){

        if (email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }
        if(playerRepository.findByEmail(email) != null){
            return new ResponseEntity<>(makeMap("error", "Name already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}