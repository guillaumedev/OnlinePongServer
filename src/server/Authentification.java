package server;

import Model.Balle;
import Model.Raquette;
import Model.Terrain;

import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;

public class Authentification implements Runnable {

    private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String login = "zero";
    public Thread threadEmission;
    public Thread threadReception;
    private AccepterConnexion accepterConnexion;
    private Terrain terrain;
    private Raquette racket;

    public Authentification(Socket s, AccepterConnexion a, Terrain t){
        socket = s;
        accepterConnexion=a;
        terrain=t;
    }

    public void run() {


        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            out.println("Entrez votre login :");
            out.flush();
            login = in.readLine();


            out.println("connecte");
            System.out.println(login +" vient de se connecter ");
            out.flush();

            //threadEmission = new Thread(new Emission(out));
            //threadEmission.start();
            racket=new Raquette(terrain);
            terrain.addRacket(racket);

            threadReception = new Thread(new Reception(in, login, this.accepterConnexion, racket));
            threadReception.start();

        } catch (IOException e) {

            System.err.println(login+" ne répond pas !");
        }
    }
    public void sendMessage(String login, String str){
        out.println(login+": "+ str);
        out.flush();
    }

    public void sendBall(Balle b){
        out.println("balle");
        out.println(b.getNewx());
        out.println(b.getNewy());
        out.flush();
    }
}