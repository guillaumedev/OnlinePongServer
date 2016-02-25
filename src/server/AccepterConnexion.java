package server;

import Model.Balle;
import Model.Raquette;
import Model.Terrain;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * La classe AccepterConnexion est celle qui se charge d'accepter les connexions et d'envoyer a chaque thread les notifications
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */

public class AccepterConnexion implements Runnable{

    private ServerSocket socketserver = null;
    private Socket socket = null;
    private ArrayList<Authentification> listUser;
    private Terrain terrain=null;
    private boolean firstConnexion=true;

    public Thread authentificationThread;

    public AccepterConnexion(ServerSocket ss, Terrain t){
        socketserver = ss;
        listUser=new ArrayList<Authentification>();
        terrain=t;
        terrain.addAccepterConnexion(this);
    }

    public synchronized void run() {
        try {
            //Cette méthode boucle sans arret et se bloque sur le socketServer.accept a chaque fois en attendant une connexion
            while(true){
                socket = socketserver.accept();
                //Si une socket essaye de se connecter, alors on continue et on affiche un message
                System.out.println("Un utilisateur veut se connecter !");

                Authentification auth = new  Authentification(socket, this, terrain);
                //On fait en sorte que auth soit considéré comme un thread et on le demarre pour chaque utilisateur
                authentificationThread = new Thread(auth);
                authentificationThread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur");
        }
    }

    //Cette méthode indique a tous les threads des utilisateurs qu'une nouvelle personne vient de se connecter
    public void notifierAll(String login){
        for(int i=0; i<listUser.size(); i++){
            if(!login.equals(listUser.get(i).getLogin())){
                listUser.get(i).newConnexion(login);
            }
        }
    }

    //On apelle pour chaque thread la méthode sendBall avec la nouvelle position de la balle
    public void notifierAllBalle(Balle b){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).sendBall(b);
        }
    }

    //On apelle pour chaque thread la méthode reloadBrick avec la nouvelle liste de briques
    public void reloadBrick(){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).reloadBrick();
        }
    }

    //On apelle pour chaque thread la méthode notifierBreackBrick avec les coordonnées de la matrice de la brique cassée
    public void notifierBreackBrick(int x, int y, int n){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).notifierBreackBrick(x,y,n);
        }
    }

    //On apelle pour chaque thread la méthode sendRaquette en indiquant les nouvelles coordonnées d'une raquette pour un joueur donné
    public void notifierAllRaquette(String login, String posX){
        for(int i=0; i<listUser.size(); i++){
            if(!login.equals(listUser.get(i).getLogin())){
                listUser.get(i).sendRaquette(login, posX);
            }

        }
    }

    //Cette méthode pelle pour chaque thread la méthode notifierNewPoint en indiquant le nombre de point du joueur qui gagne des points
    public void newPoint(Raquette s){
        String login=null;
        int nbPoints=0;

        s.setNbPoints(s.getNbPoints()+1*listUser.size());

        for(int i=0; i<listUser.size(); i++){
            if(listUser.get(i).getRacket().equals(s)){
                login=listUser.get(i).getLogin();
                nbPoints=listUser.get(i).getRacket().getNbPoints();
            }
        }

        if(login!=null){
            for(int i=0; i<listUser.size(); i++){
                listUser.get(i).notifierNewPoint(login, nbPoints);
            }
        }
    }

    //On ajoute un utilisateur (instance d'authentification) dans la liste des users. Si c'est le premier user on lance la balle.
    public void addUser(Authentification a){
        if(firstConnexion){
            Balle nextBall = new Balle(terrain);
            nextBall.start();
            firstConnexion=false;
        }
        listUser.add(a);
    }

    //retourne la liste des utilisateurs
    public ArrayList<Authentification> getListUsers(){
        return this.listUser;
    }

    //Supprimer un utilisateur de la liste
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