package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import Model.Balle;
import Model.Raquette;
import Model.Terrain;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AccepterConnexion implements Runnable{

    private ServerSocket socketserver = null;
    private Socket socket = null;
    private ArrayList<Authentification> listUser;
    private Terrain terrain=null;

    public Thread authentificationThread;

    public AccepterConnexion(ServerSocket ss, Terrain t){
        socketserver = ss;
        listUser=new ArrayList<Authentification>();
        terrain=t;
        terrain.addAccepterConnexion(this);
    }

    public void notifierAll(String login){
        for(int i=0; i<listUser.size(); i++){
            if(!login.equals(listUser.get(i).getLogin())){
                listUser.get(i).newConnexion(login);
            }
        }
    }

    public void notifierAllBalle(Balle b){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).sendBall(b);
        }
    }

    public void notifierBreackBrick(int x, int y){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).notifierBreackBrick(x,y);
        }
    }

    public void notifierDepart(String l){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).notifierDepart(l);
        }
    }

    public void notifierAllRaquette(String login, String posX){
        for(int i=0; i<listUser.size(); i++){
            if(!login.equals(listUser.get(i).getLogin())){
                listUser.get(i).sendRaquette(login, posX);
            }

        }
    }

    public void newPoint(Raquette s){
        String login=null;
        for(int i=0; i<listUser.size(); i++){
            if(listUser.get(i).getRacket().equals(s)){
                login=listUser.get(i).getLogin();
            }
        }

        if(login!=null){
            for(int i=0; i<listUser.size(); i++){
                listUser.get(i).notifierNewPoint(login);
            }
        }
    }

    public synchronized void run() {

        try {
            while(true){

                socket = socketserver.accept();
                System.out.println("Un zéro veut se connecter  ");

                Authentification auth = new  Authentification(socket, this, terrain);

                authentificationThread = new Thread(auth);
                authentificationThread.start();
                //listUser.add(auth);

            }
        } catch (IOException e) {

            System.err.println("Erreur serveur");
        }

    }

    public void addUser(Authentification a){
        listUser.add(a);
    }

    public ArrayList<Authentification> getListUsers(){
        return this.listUser;
    }

    public void removeUser(String l){
        for (int i=0; i<listUser.size();i++){
            if(listUser.get(i).getLogin().equals(l)){
                listUser.remove(i);
                terrain.getRackets().remove(i);
                System.out.println("Utilisateur supprimé");
            }
        }

        for (int i=0; i<listUser.size();i++) {
            listUser.get(i).notifierDepart(l);
        }

        System.out.println(listUser.size());
    }


}