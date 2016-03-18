package com.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by drodrigues on 2/10/16.
 */

@Entity(name="Player")
public class Player {

    @Id
    private String userId;

    private long totalWins;
    private long totalGamesPlayed;

    @OneToOne(cascade = CascadeType.ALL)
    private Game currentGame;

    @OneToOne(cascade = CascadeType.ALL)
    private GameHistory history;

    public Player(){

    }

    public Player(String userId, Game newGame){
        totalWins = 0;
        totalGamesPlayed = 0;
        this.userId = userId;
        this.currentGame = newGame;
        this.history = new GameHistory();
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }


    public void addWin(){
        this.totalWins++;
        this.totalGamesPlayed++;
    }

    public void addLoss(){
        this.totalGamesPlayed++;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(long totalWins) {
        this.totalWins = totalWins;
    }

    @Override
    public String toString() {
        return "Player{" +
                "currentGame=" + currentGame +
                ", totalGamesPlayed=" + totalGamesPlayed +
                ", totalWins=" + totalWins +
                ", userId='" + userId + '\'' +
                '}';
    }
}
