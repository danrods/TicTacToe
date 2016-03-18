package com.data;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.Index;
import javax.persistence.*;
import java.util.List;

/**
 * Created by drodrigues on 2/7/16.
 */
@Entity(name="GameHistory")
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Game> games;

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGame(List<Game> games) {
        this.games = games;
    }
}
