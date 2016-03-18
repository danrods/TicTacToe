package com.data;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Created by drodrigues on 1/31/16.
 */
public final class EMF {
    private static final EntityManagerFactory emfInstance =
            Persistence.createEntityManagerFactory("transactions-optional");

    private EMF() {}

    public static EntityManagerFactory get() {
        return emfInstance;
    }
}