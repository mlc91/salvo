package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id; //Tiene que estar pegado porque es el ID que exige Entity
    private String email;
    private String password;

    //RELATIONSHIP WITH GAMEPLAYERS
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GamePlayer> gamePlayers;//Set<GamePlayer> gamePlayers;

    //RELATIONSHIP WITH SCORE
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Score> scores;

    //region CONSTRUCTORS
    public Player() {
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }
    //endregion

    //region GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<GamePlayer> getGamePlayer() {
        return gamePlayers;
    }

    public Score getScores(Game game) {
        return scores.stream().filter(score -> score.getGame().getId() == game.getId()).findFirst().orElse(null);
    }

    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }

    public void setGamePlayer(List<GamePlayer> gamePlayers){
        this.gamePlayers = gamePlayers;
    }

    public void setScores(List<Score> scores) { this.scores = scores; }

    public void setPassword(String password) { this.password = password; }
    //endregion

    //----------------------------------------PLAYER DTO--------------------------------------------------------------//
    public Map<String,Object> playerDTO(){
        Map<String,Object> dto =new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());

        return dto;
    }
}