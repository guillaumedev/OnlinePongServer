package server;

import Model.Balle;
import Model.Brique;
import Model.Raquette;
import Model.Terrain;

import java.net.*;
import java.util.ArrayList;
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
    private int numberOfClients = 0;

    public Authentification(Socket s, AccepterConnexion a, Terrain t){
        socket = s;
        accepterConnexion=a;
        terrain=t;
    }

    public synchronized void run() {


        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            //out.println("Entrez votre login :");
            //out.flush();
            login = in.readLine();

            boolean alreadyExists=false;
            for(int i=0;i<accepterConnexion.getListUsers().size();i++){
                if(accepterConnexion.getListUsers().get(i).getLogin().equals(login)){
                    alreadyExists=true;
                }
            }

            if(!alreadyExists){
                System.out.println("Ce login est disponible.");
                out.println("loginOk");
                out.flush();
                intializeGameForPlayer();
            } else {
                System.out.println("Ce login n'est pas disponible.");
                out.println("loginExist");
                out.flush();
                run();
            }


            /*out.println("connecte");
            out.flush();*/

            /*ArrayList<Authentification> listUsers = accepterConnexion.getListUsers();
            for(int i=0;i<listUsers.size();i++){
                if(!listUsers.get(i).getLogin().equals(login)){
                    out.println("newConnexion");
                    out.println(listUsers.get(i).getLogin());
                    out.flush();
                }
            }

            out.println("createMatrice");
            Brique[][] b=terrain.getMatrix();
            out.println(b.length);
            out.println(b[0].length);
            out.flush();
            for(int i=0;i<terrain.getMatrix().length;i++){
                for(int j=0;j<terrain.getMatrix()[i].length;j++){
                    out.println("newCoord");
                    out.println(i);
                    out.println(j);
                    out.flush();
                    if(b[i][j]!=null) {
                        out.println(b[i][j].getX());
                        out.println(b[i][j].getY());
                    } else {
                        out.println("brickRemoved");
                        out.println("brickRemoved");
                    }
                    out.flush();
                }
            }


            System.out.println(login +" vient de se connecter ");


            accepterConnexion.notifierAll(login);

            //threadEmission = new Thread(new Emission(out));
            //threadEmission.start();
            racket=new Raquette(terrain);
            terrain.addRacket(racket);

            threadReception = new Thread(new Reception(in, login, this.accepterConnexion, racket, socket));
            threadReception.start();

            accepterConnexion.addUser(this);*/

        } catch (IOException e) {

            System.err.println(login+" ne répond pas !");
        }
    }

    private void intializeGameForPlayer(){
        ArrayList<Authentification> listUsers = accepterConnexion.getListUsers();
        for(int i=0;i<listUsers.size();i++){
            if(!listUsers.get(i).getLogin().equals(login)){
                out.println("newConnexion");
                out.println(listUsers.get(i).getLogin());
                out.println(listUsers.get(i).getRacket().getNbPoints());
                out.println(listUsers.get(i).getRacket().getX());
                out.flush();
            }
        }

        reloadBrick();


        System.out.println(login +" vient de se connecter ");


        accepterConnexion.notifierAll(login);

        //threadEmission = new Thread(new Emission(out));
        //threadEmission.start();
        racket=new Raquette(terrain);
        terrain.addRacket(racket);

        threadReception = new Thread(new Reception(in, login, this.accepterConnexion, racket, socket));
        threadReception.start();

        accepterConnexion.addUser(this);
    }

    public void reloadBrick(){
        out.println("createMatrice");
        Brique[][] b=terrain.getMatrix();
        out.println(b.length);
        out.println(b[0].length);
        out.flush();
        for(int i=0;i<terrain.getMatrix().length;i++){
            for(int j=0;j<terrain.getMatrix()[i].length;j++){
                out.println("newCoord");
                out.println(i);
                out.println(j);
                out.flush();
                if(b[i][j]!=null) {
                    out.println(b[i][j].getX());
                    out.println(b[i][j].getY());
                    out.println(b[i][j].getNbCoups());
                } else {
                    out.println("brickRemoved");
                    out.println("brickRemoved");
                    out.println("brickRemoved");
                }
                out.flush();
            }
        }
    }

    public void sendMessage(String login, String str){
        out.println(login+": "+ str);
        out.flush();
    }

    public synchronized void newConnexion(String login){
        out.println("newConnexion");
        out.println(login);
        out.println(0);
        out.println("null");
        out.flush();
    }

    public synchronized void sendBall(Balle b){
        out.println("balle");
        out.println(b.getNewx());
        out.println(b.getNewy());
        out.flush();
    }

    public synchronized void sendRaquette(String l, String posX){
        out.println("moveRaquette");
        out.println(l);
        out.println(posX);
        out.flush();
    }

    public synchronized void notifierDepart(String l){
        out.println("depart");
        out.println(l);
        out.flush();
    }

    public synchronized void notifierBreackBrick(int x, int y, int n){
        out.println("breackBrick");
        out.println(x);
        out.println(y);
        out.println(n);
        out.flush();
    }

    public synchronized void notifierNewPoint(String l, int n){
        out.println("newPoint");
        out.println(l);
        out.println(n);
        out.flush();
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String l){
        this.login=l;
    }

    public Raquette getRacket(){
        return this.racket;
    }
}