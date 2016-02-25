package Model;

import server.AccepterConnexion;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * La classe Terrain est celle qui définit le plateau de jeu
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Terrain{
	
    private Vector<Balle> Balls;
    private Brique[][] matrix;
    private int nbBrick=0;
    private ArrayList<Raquette> listRackets = new ArrayList<Raquette>();
    public static final int panelWidth = 1000;
	public static final int panelHeight = 800;
    private AccepterConnexion accepterConnexion;
    private Raquette lastHit=null;

    /**
     * Constructeur de terrain
     */
    public Terrain(){
        Balls = new Vector<Balle>();
        reloadBrick();
    }

    /**
     * Retourne toutes les raquettes du terrain
     * @return Arraylist de raquettes
     */
    public ArrayList<Raquette> getRackets(){
        return listRackets;
    }

    /**
     * Ajoute une raquette a la liste
     * @param r Raquette
     */
    public void addRacket(Raquette r){
        listRackets.add(r);
    }

    /**
     * On indique que la balle a bougé a l'instance d'accepterConnexion
     * @param b Balle
     */
    public void moveBall(Balle b){
        accepterConnexion.notifierAllBalle(b);
    }

    /**
     * On supprime la balle
     * @param balle Balle
     */
    public void removeBall(Balle balle) {
        Balls.remove(balle);
    }

    /**
     * On teste si la balle entre en collision avec une brique
     * @param posy
     * @param posx
     * @param newx
     * @param newy
     * @param ballsize
     * @return
     */
    public int setMatrixValue(double posy, double posx, double newx, double newy, double ballsize) {
        if(posy >= 0 && posx >= 0 && posx <= matrix[0].length && posy<=matrix.length && posx!=10){

            if(matrix[(int)posy][(int)posx] != null){
                double minx = ((int)posx * 100);
                double maxx = ((int)posx * 100)+100;
                double miny = ((int)posy * 30) + 80;
                double maxy = ((int)posy * 30) + 80 + 30;
                
                //AFFINER LA DETECTION DES COLLISIONS AVEC LES BRIQUES
                if((newx+ballsize/2 <= minx || maxx <= newx+ballsize/2) 
                        && (miny <= (newy+ballsize) && maxy >= (newy+ballsize) || (miny <= newy) && maxy >= (newy))){
                    hitBrick(posx, posy);
                    return Brique.SIDE;
                }
                if(((newx >= minx && newx <= maxx) || (newx+ballsize/2 >= minx && newx+ballsize/2 <= maxx)) && miny <= newy+ballsize){
                    hitBrick(posx, posy);
                    return Brique.UPSIDE;
                }                
            }
        }
        return -1;
    }

    /**
     * Supprime la brique indiquée
     * @param posx position x dans la matrice
     * @param posy position y dans la matrice
     */
    private void hitBrick(double posx, double posy) {
    	Brique b = matrix[(int)posy][(int)posx];
    	if(b.getNbCoups() <= 1){
    		matrix[(int)posy][(int)posx] = null;
            accepterConnexion.notifierBreackBrick((int)posy, (int)posx, b.getNbCoups()-1);

            //Si lastHit est différent de null, c'est ce joueur qui récup§re des points
            if(lastHit!=null){
                accepterConnexion.newPoint(lastHit);
            }
            nbBrick--;
            //Si il n'y a plus de briques on en remet et on notifie tous les joueurs
            if(nbBrick==0){
                reloadBrick();
                accepterConnexion.reloadBrick();

            }
    	}else{
            accepterConnexion.notifierBreackBrick((int)posy, (int)posx, b.getNbCoups()-1);
    		b.setNbCoups(b.getNbCoups()-1);
    	}
	}

    /**
     * Création de nos briques de manière aléatoire
     */
    private void reloadBrick(){
        Random r = new Random();
        int Low = 0;
        int High = 100;

        int lowNbCoups = 1;
        int highNbCoups = 4;
        matrix = new Brique[5][10];

        int defX=0;
        int defY=80;
        int defW=0;
        int defH=0;
        int row=0;
        int col=0;
        for(int i = 0;i<50;i++){
            int result = r.nextInt(High-Low) + Low;
            int nbCoups = r.nextInt(highNbCoups-lowNbCoups) + lowNbCoups;

            if(defW + defX > panelWidth){
                defX=0;
                defY+=defH;
                row++;
                col=0;
            }
            Brique brick = new Brique(defX, defY, nbCoups);
            defW=brick.getWidth();
            defH=brick.getHeight();
            defX+=defW;

            //if(result>30){
                matrix[row][col] = brick;
                nbBrick++;
            //} else {
              //  matrix[row][col] = null;
            //}
            col++;
            //nbBrick++;
        }
       // accepterConnexion.reloadBrick();
    }

    /**
     * Retourne la matrice contenant les briques
     * @return Brique
     */
    public Brique[][] getMatrix(){
        return matrix;
    }

    /**
     * Retourne la taille du terrain
     * @return int
     */
    public int getWidth(){
        return panelWidth;
    }

    /**
     * Retourne la hauteur du terrain
     * @return int
     */
    public int getHeight(){
        return panelHeight;
    }

    public void addAccepterConnexion(AccepterConnexion a){
        accepterConnexion=a;
    }

    /**
     * Retourne l'instance d'AccepterConnexion
     * @return AccepterConnexion
     */
    public AccepterConnexion getAccepterConnexion(){
        return this.accepterConnexion;
    }


    /**
     * On met a jour le dernier joueur ayant touché la balle
     * @param lastHit Raquette
     */
    public void setLastHit(Raquette lastHit) {
        this.lastHit = lastHit;
    }
}
