package com.data;

import com.google.appengine.api.datastore.Key;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by drodrigues on 2/2/16.
 */
@Entity
public class Line implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<mInteger> cells;

    public Line(){

    }

    public Line(List<mInteger> cells){
        this.cells = new ArrayList<>();
        for(mInteger i : cells){
            this.cells.add(new mInteger(i.getInteger()));
        }

    }
    public Line(Integer... cells){
        this.cells = new ArrayList<mInteger>();
        for(Integer c : cells){
            this.cells.add(new mInteger(c));
        }
    }

    public boolean isWin(Cell.CellType type, List<Cell> cellList){

        for(mInteger i : cells){
            //Cell c = cellList.get(i.getInteger());
            Cell cell = null;
            for(Cell c : cellList){
                if(c.getId().equals(i.toString())){
                    cell = c;
                    break;
                }
            }
            if(cell != null){
                if(! cell.isTaken()){
                    return false;
                }
                else if(cell.getType() != type){ //Cell is taken
                    return false;
                }
            }


        }


        return true;
    }

    @Override
    public String toString() {
        return "Line{" +
                "cells=" + cells +
                '}';
    }

    public Line clone(){
        Line newLine = new Line(cells);
        return newLine;
    }
}
