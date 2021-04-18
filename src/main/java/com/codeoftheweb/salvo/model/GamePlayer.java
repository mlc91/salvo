package com.codeoftheweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date joinDate;

    //RELATIONSHIP WITH PLAYERS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    //RELATIONSHIP WITH GAMES
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    //RELATIONSHIP WITH SHIPS
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships;

    //RELATIONSHIP WITH SALVOES
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Salvo> salvoes;

    //region CONSTRUCTORS
    public GamePlayer() {
        this.joinDate = new Date();
        this.ships = new HashSet<>();
        this.salvoes = new ArrayList<>();
    }

    public GamePlayer(Player player, Game game){
        this.player = player;
        this.game = game;
        this.joinDate = new Date();
    }
    //endregion

    //region GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public Date getJoinDate(){
        return joinDate;
    }

    public Player getPlayer(){
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public List<Salvo> getSalvoes() { return salvoes; }

    public Score getScore(){
        return player.getScores(this.game);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player){
        this.player = player;
    }
    //endregion

    //----------------------------------------ADD LIST SHIPS----------------------------------------------------------//
    public void addListShips(Set<Ship> ships) {
        ships.forEach(ship -> {
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }
    //-----------------------------------------GET OPPONENT-----------------------------------------------------------//
    public Optional<GamePlayer> getOpponent() {
        return this.getGame().getGamePlayer().stream().filter(gamePlayer -> gamePlayer.getId() != this.getId()).findFirst();
    }
    //----------------------------------------GAME PLAYER DTO---------------------------------------------------------//
    public Map<String,Object> gamePlayerDTO(){
        Map<String,Object> dto =new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO());

        return dto;
    }
}
