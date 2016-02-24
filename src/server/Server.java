package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */

import Model.Terrain;

import java.io.*;
import java.net.*;

//Classe de lancement de l'application, qui contient le main de l'application
public class Server {
    public static ServerSocket ss = null;
    public static Thread t;

    //La classe main permet de lancer l'application, elle va creer une socket server pour écouter le port spécifié (ici 2009)
    public static void main(String[] args) {
        try {
            ss = new ServerSocket(2009);
            System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());

            //Creation d'un nouveau terrain
            Terrain terrain = new Terrain();

            //On créé un nouveay thread dans lequel on passe une instance de accepter connexion et de terrain
            t = new Thread(new AccepterConnexion(ss, terrain));
            t.start();

        } catch (IOException e) {
            System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
        }
    }
}