package Model;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * La classe Balle est celle qui gère la physique de la bale et ses deplacements
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Balle extends Thread
{
   private boolean threadStarted;
   private int size;
   private double speed;       
   private double deltax, deltay;
   private Terrain pan;
   private double newx, newy, oldx, oldy;
   private double acceleration = 1.05;
   private PrintWriter out;

    /**
     * Constructeur de Balle
     * @param panel Prend un terrain en paramètre
     */
   public Balle(Terrain panel)
   {
	  pan = panel;
      threadStarted = true;   
      size = 20;
      speed = 3;

      int startx = (int)(Math.random() * pan.getWidth()-(size*2));

      int starty = (int)(Math.random() * pan.getHeight()/2-(size*2));

      deltax = speed * ((int) (Math.random() * 10) >= 5 ? -1 : 1);
      deltay = speed * ((int) (Math.random() * 10) >= 5 ? -1 : 1);

      if ((deltax == 0) && (deltay == 0)) { deltax = 1; }
      if(startx <= size || starty <= size){
    	  startx = size*2;
    	  starty = size*2;
      }
       newx=startx;
       newy=starty;
   }

    /**
     * A chaque boucle on test si la balle est entrée en collision avec quelque chose
     */
    public synchronized void run()
    {
      while(threadStarted) {
          try {
              // To free up processor time
              Thread.sleep(10);
          } catch (InterruptedException e) {
              System.out.println("crash");
          }

          oldx = this.newx;
          oldy = this.newy;
          newx = oldx + deltax;
          if (newx < 0) {
              deltax = -deltax;
              newx = -newx;
          }
          if (newx + size > pan.getWidth()) {
              deltax = -deltax;
              newx = pan.getWidth() - ((newx + size) - pan.getWidth()) - size;
          }

          ArrayList<Raquette> list = pan.getRackets();

          checkCollisionList(list);

          newy = oldy + deltay;
          //test loose
          if (newy < 0) {
              deltay = -deltay;
              newy = -newy;
          }

          if (newy+size >= pan.getHeight()) {
              pan.removeBall(this);
              threadStarted = false;
              Balle nextBall = new Balle(pan);
              nextBall.start();
              pan.setLastHit(null);
          }

          breakBrick();
          pan.moveBall(this);

      }

   }


    /**
     * Code appelé pour tester si une brique est cassée
     */
	private void breakBrick() {
		
		double posx =  newx/100;
		double posy =  (newy-80)/30;
		double posx2 =  (newx+size)/100;
		double posy2 =  (newy+size-80)/30;
		Brique[][] matrix = pan.getMatrix();
		int dir=-1;
		boolean changed=false;
        boolean brickHit=false;


		if((posy>=0 && posy < matrix.length)){
			dir = pan.setMatrixValue(posy, posx, newx, newy, size);
			if(!changed && dir != -1){
                invertdelta(dir);
                changed=true;
                brickHit=true;
            }

            if(!brickHit) {
                dir = pan.setMatrixValue(posy, posx2, newx, newy, size);
                if (!changed && dir != -1) {
                    invertdelta(dir);
                    changed = true;
                    brickHit=true;
                }
            }
		}

		if(posy2 < matrix.length){
            if(!brickHit){
                dir = pan.setMatrixValue(posy2, posx2, newx, newy, size);
                if(!changed && dir != -1){
                    invertdelta(dir);
                    changed=true;
                    brickHit=true;}
            }

            if(!brickHit){
                dir = pan.setMatrixValue(posy2, posx, newx, newy, size);
                if(!changed && dir != -1){
                    invertdelta(dir);
                    changed=true;
                    brickHit=true;
                }
            }

		}
		
	}


    /**
     * Inverse la direction de la balle
     * @param dir int
     */
	private void invertdelta(int dir) {
		switch(dir){
			case Brique.UPSIDE :
				deltay = -deltay;
				break;
			case Brique.SIDE :
				deltax = -deltax;
				break;
		}
	}

    /**
     * Teste si la balle a rencontré un mur et définit quelle raquette est la plus proche de la balle (en son centre)
     * @param listRacket Arraylist de raquette
     * @return boolean
     */
    public boolean checkCollisionList(ArrayList<Raquette> listRacket) {
        double diff=200;
        Raquette selectedRacket=null;
        for (int i = 0; i < listRacket.size(); i++) {

            Raquette racket = listRacket.get(i);

            if ((newx + size > (racket.getX()) && newx + size < (racket.getX() + racket.getSize())
                    || newx > (racket.getX()) && newx < (racket.getX() + racket.getSize()))
                    && newy + size >= pan.getHeight() - racket.getHeight() && newy + size < pan.getHeight() + size) {
                if (diff > newx - (racket.getX() + racket.getWidth() / 2)) {
                    selectedRacket = racket;
                    diff=newx - (racket.getX() + racket.getWidth() / 2);
                    pan.setLastHit(selectedRacket);
                }
            }
        }

        //Si il n'y a pas de raquette courante, on essaie de trouver la quelle est la plus proche du centre
        if(selectedRacket!=null) {
            double relativeY = (selectedRacket.getX() + (selectedRacket.getSize() / 2)) - newx - (size / 2);
            double normalRelativeY = (relativeY / (selectedRacket.getSize() / 2));
            double angle = (normalRelativeY * 70);
            deltax = speed * (-Math.sin(Math.toRadians(angle)));
            deltay = speed * (-Math.cos(Math.toRadians(angle)));
            newy = pan.getWidth() - selectedRacket.getHeight() - ((newy + size) - pan.getHeight() - selectedRacket.getHeight());
            speed = speed * acceleration;

            pan.getAccepterConnexion().newPoint(selectedRacket);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter
     * @return double
     */
    public double getNewx() {
        return newx;
    }

    /**
     * getter
     * @return double
     */
    public double getNewy() {
        return newy;
    }
}
