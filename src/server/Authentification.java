package server;

import Model.Balle;
import Model.Brique;
import Model.Raquette;
import Model.Terrain;
import java.net.*;
import java.util.ArrayList;
import java.io.*;


/**
 * Classe qui est chargée de vérifier la nouvelle connexion, de tester le login et d'ajouter l'instance de ajouterConnexion a la liste globale
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Authentification implements Runnable {

    private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String login = "zero";
    public Thread threadReception;
    private AccepterConnexion accepterConnexion;
    private Terrain terrain;
    private Raquette racket;

    /**
     * Constructeur
     * @param s la socket
     * @param a l'instance d'accepterConnexion
     * @param t le terrain
     */
    public Authentification(Socket s, AccepterConnexion a, Terrain t){
        socket = s;
        accepterConnexion=a;
        terrain=t;
    }

    /**
     * tant que le login n'est pas disponible, on refuse l'entrée du joueur
     */
    public synchronized void run() {
        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

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
        } catch (IOException e) {

            System.err.println(login+" ne répond pas !");
        }
    }

    /**
     * On initialise le jeu pour le joueur, on envoi newConnexiona  tous les autres utilisateurs et on affiche les briques.
     */
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
        //On recharge les briques pour le joueur
        reloadBrick();
        System.out.println(login +" vient de se connecter ");

        //On envoie a tous les joueurs
        accepterConnexion.notifierAll(login);

        //On créé une nouvelle raquette et on l'ajoute au terrain
        racket=new Raquette(terrain);
        terrain.addRacket(racket);

        //On lance un thread de reception pour recevoir les informations de ce nouvel utilisateur
        threadReception = new Thread(new Reception(in, login, this.accepterConnexion, racket, socket));
        threadReception.start();

        accepterConnexion.addUser(this);
    }

    /**
     * On envoi la matrice avec les briques au nouveau joueur
     */
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

    /**
     * On notifie le joueur d'une nouvelle conexion
     * @param login String
     */
    public synchronized void newConnexion(String login){
        out.println("newConnexion");
        out.println(login);
        out.println(0);
        out.println("null");
        out.flush();
    }

    /**
     * On notifie le joueur que la balle a bougé
     * @param b Balle
     */
    public synchronized void sendBall(Balle b){
        out.println("balle");
        out.println(b.getNewx());
        out.println(b.getNewy());
        out.flush();
    }

    /**
     * On envoie la nouvelle position d'une raquette
     * @param l login du joueur qui a bougé
     * @param posX position en x de la raquette
     */
    public synchronized void sendRaquette(String l, String posX){
        out.println("moveRaquette");
        out.println(l);
        out.println(posX);
        out.flush();
    }

    /**
     * envoi au joueur le joueur qui a quitté la partie
     * @param l String
     */
    public synchronized void notifierDepart(String l){
        out.println("depart");
        out.println(l);
        out.flush();
    }

    /**
     * On envoi au joueur la brique cassée
     * @param x position x matrice
     * @param y position y matrice
     * @param n nbCoups restants
     */
    public synchronized void notifierBreackBrick(int x, int y, int n){
        out.println("breackBrick");
        out.println(x);
        out.println(y);
        out.println(n);
        out.flush();
    }

    /**
     * On envoi lorsqu'un joueur a de nouveaux points
     * @param l login de l'utilisateur
     * @param n nbPoints
     */
    public synchronized void notifierNewPoint(String l, int n){
        out.println("newPoint");
        out.println(l);
        out.println(n);
        out.flush();
    }

    public String getLogin(){
        return login;
    }

    public Raquette getRacket(){
        return this.racket;
    }
}