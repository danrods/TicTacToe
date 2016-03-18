package com.data;

import com.google.appengine.api.datastore.Key;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by drodrigues on 2/2/16.
 */
@Entity
public class Cell implements Serializable{

    private static final String BLANK = "/img/Solid_white.png";
    private static final String X = "/img/red_x.png";
    private static final String O = "/img/red_dot.png";


    public static enum CellType{
        X_Cell(X), O_Cell(O), Unused(BLANK);

        private String chosenType;


        CellType(String type){
            this.chosenType = type;
        }

        public String toString(){
            return chosenType;
        }

    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;

    private String id;

    private CellType type;

    private boolean isTaken;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Line> lines;

    public Cell(){

    }

    public Cell(int id) {
        this.id = id + "";
        this.isTaken = false;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public CellType getType() {
        return type;
    }

    public void takeCell(CellType type) {
        if(this.type != CellType.Unused){
            isTaken = true;
        }
        this.type = type;
    }

    public String getId() {
        return id;
    }


    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLines(List<Line> lines){
        this.lines = lines;
    }

    public List<Line> getLines(){
        return this.lines;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", isTaken=" + isTaken +
                '}';
    }
}
