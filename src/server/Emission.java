package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import Model.Raquette;
import Model.Terrain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Emission{

    private PrintWriter out;
    private String message = null, login = null;
    private Terrain terrain;
    private ArrayList<Authentification> listUser;

    public Emission(PrintWriter out, Terrain t, ArrayList<Authentification> l){
        this.out = out;
        terrain=t;
        listUser=l;
    }
}