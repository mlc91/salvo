package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private double score;

    private Date finishDate;

    //RELATIONSHIP WITH PLAYERS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    //RELATIONSHIP WITH GAMES
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    //region CONSTRUCTORS
    public Score () { }

    public Score(double score, Player player, Game game) {
        this.score = score;
        this.finishDate = new Date();
        this.player = player;
        this.game = game;
    }
    //endregion

    //region GETTERS & SETTERS
    public Long getId() { return id; }

    public double getScore() { return score; }

    public Date getFinishDate() { return finishDate; }

    public Player getPlayer() { return player; }

    public Game getGame() { return game; }

    public void setScore(double score) { this.score = score; }

    public void setPlayer(Player player) { this.player = player; }

    public void setGame(Game game) { this.game = game; }
    //endregion

    //----------------------------------------SCORE DTO---------------------------------------------------------------//
    public Map<String, Object> scoreDTO(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("finishDate", this.getFinishDate());
        dto.put("player", this.getPlayer().getId());
        dto.put("score", this.getScore());

        return dto;
    }

}
