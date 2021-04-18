package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date createDate;

    //RELATIONSHIP WITH GAMEPLAYERS
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GamePlayer> gamePlayers;//Set<GamePlayer> gamePlayers;

    //RELATIONSHIP WITH SCORE
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Score> scores;

    //region CONSTRUCTORS
    public Game() { this.createDate = new Date(); }
    //endregion

    //region GETTERS & SETTERS
    public Long getId() { return id; }

    public Date getCreateDate(){
        return createDate;
    }

    public List<GamePlayer> getGamePlayer(){
        return gamePlayers;
    }

    public List<Score> getScores() { return scores; }

    public void setGamePlayer(List<GamePlayer> gamePlayers){
        this.gamePlayers = gamePlayers;
    }

    public void setScores(List<Score> scores) { this.scores = scores; }
    //endregion

    //----------------------------------------GAME DTO----------------------------------------------------------------//
    public Map<String,Object> gameDTO(){
        Map<String,Object> dto =new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreateDate());
        dto.put("gamePlayers", this.getGamePlayer().stream().map(gamePlayer -> gamePlayer.gamePlayerDTO()).collect(toList()));
        dto.put("scores", this.getScores().stream().map(score -> score.scoreDTO()).collect(toList()));

        return dto;
    }
}