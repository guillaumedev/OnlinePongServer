package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Raquette
{
   private int width, height;       
   private Terrain pan;
   
   private int posx;
   
   private int startx;
   private int starty;
   
   public Raquette(Terrain terrain)
   {
	  pan = terrain;  
      width = 150;
      height = 45;
      
      startx = (int)(Math.random() * Terrain.panelWidth-width);
      starty = Terrain.panelHeight-height;
      
   }
   
   public int getStartX(){
	   return startx;
   }
   
   public int getStartY(){
	   return starty;
   }

   
   public int getX(){
	   return this.posx;
   }
   
   public void setX(int x){
       this.posx = x;
   }
   
   
   public int getSize(){
	   return this.width;
   }


	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}

    public Terrain getTerrain(){
        return this.pan;
    }

}
