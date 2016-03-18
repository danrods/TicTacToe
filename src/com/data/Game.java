package com.data;



import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.*;
import javax.persistence.*;
import javax.persistence.Embedded;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by drodrigues on 2/7/16.
 */
@Entity(name="Game")
public class Game {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Board board;

    private Date datePlayed;


    public Game(){
        board = new Board();
        datePlayed = new GregorianCalendar().getTime();
    }


    public String getPlayerImg(){
        return board.getPlayerImg();
    }

    public String getComputerImg(){
        return board.getComputerImg();
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Game{" +
                "board=" + board +
                ", datePlayed=" + datePlayed +
                '}';
    }
}
