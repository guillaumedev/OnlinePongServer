package Model;

import server.AccepterConnexion;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Terrain{
	
    private Vector<Balle> Balls;
    private Brique[][] matrix;
    private int nbBrick=0;
    private ArrayList<Raquette> listRackets = new ArrayList<Raquette>();
    public static final int panelWidth = 1000;
	public static final int panelHeight = 800;
    private AccepterConnexion accepterConnexion;
    private Raquette lastHit=null;


    private static final String SIDES = null;
    
    public Terrain(){
        Balls = new Vector<Balle>();
        reloadBrick();
    }
    
    public ArrayList<Raquette> getRackets(){
        return listRackets;
    }

    public void addRacket(Raquette r){
        listRackets.add(r);
       /* if(listRackets.size()==1){
            Balle nextBall = new Balle(this);
            nextBall.start();
        }*/
    }

    public void moveBall(Balle b){
        accepterConnexion.notifierAllBalle(b);
    }

    public void removeBall(Balle balle) {
        Balls.remove(balle);
    }

    
    public int countBalls() {
        return Balls.size();
    }
    

    public int setMatrixValue(double posy, double posx, double newx, double newy, double ballsize) {
        if(posy >= 0 && posx >= 0 && posx <= matrix[0].length && posy<=matrix.length && posx!=10){

            if(matrix[(int)posy][(int)posx] != null){
                //System.out.println("DELETED: ["+(int)posy+"]["+(int)posx+"]");
                double minx = ((int)posx * 100);
                double maxx = ((int)posx * 100)+100;
                double miny = ((int)posy * 30) + 80;
                double maxy = ((int)posy * 30) + 80 + 30;
                newx=(int)newx;
                newy=(int)newy;
                ballsize=(int)ballsize;
                //System.out.println(minx+" "+maxx+" / "+ miny+" "+ maxy);
                //System.out.println(newx+" / "+newy);
                
                //AFFINER LA DETECTION DES COLLISIONS AVEC LES BRIQUES
                if((newx+ballsize/2 <= minx || maxx <= newx+ballsize/2) 
                        && (miny <= (newy+ballsize) && maxy >= (newy+ballsize) || (miny <= newy) && maxy >= (newy))){
                    //System.out.println("SIDE");
                    hitBrick(posx, posy);
                    return Brique.SIDE;
                }
                if(((newx >= minx && newx <= maxx) || (newx+ballsize/2 >= minx && newx+ballsize/2 <= maxx)) && miny <= newy+ballsize){
                    //System.out.println("TOP");
                    hitBrick(posx, posy);
                    return Brique.UPSIDE;
                }                
            }
        }
        return -1;
    }
    
    private void hitBrick(double posx, double posy) {
    	Brique b = matrix[(int)posy][(int)posx];
    	if(b.getNbCoups() <= 1){
    		matrix[(int)posy][(int)posx] = null;
            accepterConnexion.notifierBreackBrick((int)posy, (int)posx, b.getNbCoups()-1);
            if(lastHit!=null){
                accepterConnexion.newPoint(lastHit);
            }
            nbBrick--;
            if(nbBrick==0){
                reloadBrick();
                accepterConnexion.reloadBrick();

            }
    	}else{
            accepterConnexion.notifierBreackBrick((int)posy, (int)posx, b.getNbCoups()-1);
    		b.setNbCoups(b.getNbCoups()-1);
    	}
	}

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

            if(result>30){
                matrix[row][col] = brick;
                nbBrick++;
            } else {
                matrix[row][col] = null;
            }
            col++;
            //nbBrick++;
        }
       // accepterConnexion.reloadBrick();
    }

    public Brique[][] getMatrix(){
        return matrix;
    }
    
    public int getWidth(){
        return panelWidth;
    }
    
    public int getHeight(){
        return panelHeight;
    }

    public void addAccepterConnexion(AccepterConnexion a){
        accepterConnexion=a;
    }

    public AccepterConnexion getAccepterConnexion(){
        return this.accepterConnexion;
    }

    public Raquette getLastHit() {
        return lastHit;
    }

    public void setLastHit(Raquette lastHit) {
        this.lastHit = lastHit;
    }


    
}
