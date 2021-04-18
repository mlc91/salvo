package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String type;

    //RELATIONSHIP WITH GAME PLAYERS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer")
    private GamePlayer gamePlayer;

    //COLLECTION OF SHIP LOCATION
    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    //region CONSTRUCTORS
    public Ship() { }

    public Ship(String type, GamePlayer gamePlayer, List<String> locations){
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }
    //endregion

    //region GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getLocations() { return locations; }

    public void setType(String shipType) {
        this.type = shipType;
    }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }
    //endregion

    //----------------------------------------SHIP DTO----------------------------------------------------------------//
    public Map<String,Object> shipDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", this.getType());
        dto.put("locations", this.getLocations());

        return dto;
    }

}
