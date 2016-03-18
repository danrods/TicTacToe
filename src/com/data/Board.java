package com.data;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ai.AI;
import  com.data.Cell.CellType;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

import javax.jdo.annotations.Persistent;
import javax.persistence.*;

/**
 * Created by drodrigues on 2/2/16.
 */
@Entity
public class Board implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key id;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cell> cellList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CellType playerType;

    @Enumerated(EnumType.STRING)
    private CellType computerType;

    @Transient
    private static final Logger log = Logger.getLogger(Board.class.getName());

    public Board(){

        Cell one = new Cell(1);
        Cell two = new Cell(2);
        Cell three = new Cell(3);
        Cell four = new Cell(4);
        Cell five = new Cell(5);
        Cell six = new Cell(6);
        Cell seven = new Cell(7);
        Cell eight = new Cell(8);
        Cell nine = new Cell(9);

        Line rowOne = new Line(1, 2, 3);
        Line rowTwo = new Line(4, 5, 6);
        Line rowThree = new Line(7, 8, 9);
        Line colOne = new Line(1, 4, 7);
        Line colTwo = new Line(2, 5, 8);
        Line colThree = new Line(3, 6, 9);
        Line diagOne =  new Line(1, 5, 9);
        Line diagTwo = new Line(7, 5, 3);

        cellList.addAll(Arrays.asList(one,two,three,four,five,six,seven,eight,nine));

        one.setLines(Arrays.asList(rowOne.clone(),colOne.clone(),diagOne.clone()));
        two.setLines(Arrays.asList(rowOne.clone(), colTwo.clone()));
        three.setLines(Arrays.asList(rowOne.clone(), colThree.clone(), diagTwo.clone()));
        four.setLines(Arrays.asList(rowTwo.clone(), colOne.clone()));
        five.setLines(Arrays.asList(rowTwo.clone(), colTwo.clone(), diagOne.clone(), diagTwo.clone()));
        six.setLines(Arrays.asList(rowTwo.clone(), colThree.clone()));
        seven.setLines(Arrays.asList(rowThree.clone(), colOne.clone(), diagTwo.clone()));
        eight.setLines(Arrays.asList(rowThree.clone(), colTwo.clone()));
        nine.setLines(Arrays.asList(rowThree.clone(), colThree.clone(), diagOne.clone()));

        if(Math.random() < 0.5){
            playerType = CellType.X_Cell;
            computerType = CellType.O_Cell;
        }
        else{
            playerType = CellType.O_Cell;
            computerType = CellType.X_Cell;
        }

    }


    public JSONObject makeMove(int cellNumber){

        Cell cell = null;
        for(Cell c : cellList){
            if(c.getId().equals(cellNumber + "")){
               cell = c;
                break;
            }
        }

        if(cell.isTaken()) return null;

        cell.takeCell(playerType);

        int winStatus = isGameOver(playerType, cell);
        //if(win) log.log(Level.INFO, "Player Won!");

        JSONObject obj = new JSONObject();

        try {
            obj.put("img", playerType.toString());
            obj.put("win", winStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public int isGameOver(CellType type, Cell cell){
        boolean isWin = isWin(type, cell);
        if(isWin){
           return 1;
        }

        boolean isTie = isTie();
        if(isTie){
            return 2;
        }

        return 0;
    }

    public boolean isWin(CellType type, Cell cell){
        boolean isWin = false;
       //Cell cell = cellList.get(cellNumber);
        List<Line> winLines = cell.getLines();

        for(Line l : winLines){
             //log.log(Level.INFO, "Checking line : " + l);
            if(l.isWin(type, cellList)){
                isWin = true;
                break;
            }
        }

        return isWin;
    }

    public boolean isTie(){
        for(Cell c : cellList){
            if(! c.isTaken()){
                return false;
            }
        }
        return true;
    }

    public JSONObject findMove(){

        Cell cell = AI.findMove(computerType, cellList);

        if(cell == null){
            return null;
        }

        String sid = cell.getId();
        String img = computerType.toString();
        int winStatus = isGameOver(computerType, cell);

        JSONObject obj = new JSONObject();

        try {
            obj.put("id", sid);
            obj.put("img", computerType.toString());
            obj.put("win", winStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String json = "{\"id\":\"" +sid + "\", \"img\":\"" + img + "\", \"win\":\"" + winStatus +"\"}";

        //log.log(Level.INFO, "Returning JSON : " + json);
        return obj;
    }

    public String getPlayerImg(){
        return playerType.toString();
    }

    public void setPlayerImg(String img){

    }
    public void setPlayerImage(CellType type){
        this.playerType = type;
    }

    public String getComputerImg(){
        return computerType.toString();
    }

    public void setComputerImg(String img){

    }
    public void setComputerImg(CellType type){
        this.computerType = type;
    }

    @Override
    public String toString() {
        return "Board { Player : " + playerType + " \t Computer : " + computerType + "}";
    }


}
