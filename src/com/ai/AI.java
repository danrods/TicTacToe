package com.ai;

import com.data.Cell;
import com.data.Line;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by drodrigues on 1/31/16.
 */

public class AI implements Serializable{


    public AI(){

    }


    public static final Cell findMove(Cell.CellType aiType, List<Cell> cellList){

        Cell cell = null;

        for(Cell mCell : cellList){
            //Cell mCell = cellMap.get(s);
            if(! mCell.isTaken()){
                mCell.takeCell(aiType);
                cell=mCell;
                break;
            }

        }

        return cell;
    }




}
