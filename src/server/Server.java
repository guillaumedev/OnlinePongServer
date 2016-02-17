package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */

import Model.Balle;
import Model.Terrain;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static ServerSocket ss = null;
    public static Thread t;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(2009);
            System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());

            Terrain terrain = new Terrain();

            t = new Thread(new AccepterConnexion(ss, terrain));
            t.start();

        } catch (IOException e) {
            System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
        }

    }
}