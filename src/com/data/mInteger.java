package com.data;

import com.google.appengine.api.datastore.Key;

import javax.persistence.*;

/**
 * Created by drodrigues on 2/11/16.
 */
@Entity
public class mInteger {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key id;

    private int integer;

    public mInteger(){

    }

    public mInteger(int i){
        this.integer = i;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    @Override
    public String toString() {
        return "" +integer;
    }
}
