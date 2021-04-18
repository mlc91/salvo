package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private long turn;

    //RELATIONSHIP WITH GAME PLAYERS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer")
    private GamePlayer gamePlayer;

    //COLLECTION OF SALVO LOCATION
    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> salvoLocations;

    //region CONSTRUCTORS
    public Salvo() {
        this.turn = 0;
        this.salvoLocations = new ArrayList<>();
    }

    public Salvo(long turn, GamePlayer gamePlayer, List<String> salvoLocations) {
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
    }
    //endregion

    //region GETTERS & SETTERS
    public long getTurn() { return turn; }

    public GamePlayer getGamePlayer() { return gamePlayer; }

    public List<String> getSalvoLocations() { return salvoLocations; }

    public void setId(Long id) { this.id = id; }

    public void setTurn(long turn) { this.turn = turn; }

    public void setSalvoLocations(List<String> salvoLocations) { this.salvoLocations = salvoLocations; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }
    //endregion

    //----------------------------------------GAME PLAYER DTO---------------------------------------------------------//
    public Map<String,Object> salvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations",this.getSalvoLocations());

        return dto;
    }

}
