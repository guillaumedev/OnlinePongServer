package Model;

import server.Emission;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.xml.bind.SchemaOutputResolver;

public class Balle extends Thread
{
   //private Ellipse2D.Double balle; 

   private boolean threadStarted;
   private int size;
   private double speed;       
   private double deltax, deltay;
   private Terrain pan;
   private double newx, newy, oldx, oldy;
   private double acceleration = 1.05;
   private PrintWriter out;
   
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
      
      //deltax = speed*(Math.sin(Math.toRadians((int)(Math.floor(Math.random()*360)))));
	  //deltay = speed*(Math.cos(Math.toRadians((int)(Math.floor(Math.random()*360)))));
      
      //System.out.println("deltax: "+ deltax +" deltay: "+ deltay);
      if ((deltax == 0) && (deltay == 0)) { deltax = 1; }
      if(startx <= size || starty <= size){
    	  startx = size*2;
    	  starty = size*2;
      }
       newx=startx;
       newy=starty;
    /*   try{
           out = new PrintWriter(socket.getOutputStream());
       } catch(Exception e){

       }*/
   }


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
          /*for (int i = 0; i < list.size(); i++) {
              //System.out.println(list.get(i));
              if (checkCollision(list.get(i))) {
                  break;
              }
              //list.get(i).move((int) newx+(list.get(i).getSize()-(size)));
          }*/

          if (checkCollisionList(list)){
              System.out.println("touche");
          }


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
          }

          breakBrick();

          pan.moveBall(this);

      }

   }


	private void breakBrick() {
		
		double posx =  newx/100;
		double posy =  (newy-80)/30;
		double posx2 =  (newx+size)/100;
		double posy2 =  (newy+size-80)/30;
		Brique[][] matrix = pan.getMatrix();
		int dir=-1;
		boolean changed=false;
		if((posy>=0 && posy < matrix.length)){
			dir = pan.setMatrixValue(posy, posx, newx, newy, size);
			if(!changed && dir != -1){ invertdelta(dir); changed=true; }
			dir = pan.setMatrixValue(posy, posx2, newx, newy, size);
			if(!changed && dir != -1){ invertdelta(dir);changed=true;}
		}
		
		if(posy2 < matrix.length){
			dir = pan.setMatrixValue(posy2, posx2, newx, newy, size);
			if(!changed && dir != -1){ invertdelta(dir);changed=true;}
			dir = pan.setMatrixValue(posy2, posx, newx, newy, size);
			if(!changed && dir != -1){ invertdelta(dir);changed=true;}
		}
		
	}


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
                    System.out.println("raquette trouvée");
                }
            }
        }

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

	public boolean checkCollision(Raquette racket) {
		
		if((newx+size > (racket.getX()) && newx+size < (racket.getX()+racket.getSize())
			|| newx > (racket.getX()) && newx < (racket.getX()+racket.getSize())) 
				&& newy+size >= pan.getHeight()-racket.getHeight() && newy+size < pan.getHeight()+size){	
			
			double relativeY = (racket.getX()+(racket.getSize()/2)) - newx - (size/2);
			double normalRelativeY = (relativeY/(racket.getSize()/2));
			double angle = (normalRelativeY * 70);
			deltax = speed*(-Math.sin(Math.toRadians(angle)));
			deltay = speed*(-Math.cos(Math.toRadians(angle)));
			newy = pan.getWidth()-racket.getHeight()-((newy+size)-pan.getHeight()-racket.getHeight()); 
			speed = speed*acceleration;
			return true;
		}
		return false;	
	}

	public double getSize() {
		return size;
	}

    public double getNewx() {
        return newx;
    }

    public void setNewx(double newx) {
        this.newx = newx;
    }

    public double getNewy() {
        return newy;
    }

    public void setNewy(double newy) {
        this.newy = newy;
    }

}
