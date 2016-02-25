package Model;

/**
 * La classe Raquette permet de définir les raquettes des joueurs
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Raquette
{
   private int width, height;       
   private Terrain pan;
   private int posx;
   private int startx;
   private int starty;
    private int nbPoints;

    /**
     * Constructeur
     * @param terrain instance de terrain
     */
   public Raquette(Terrain terrain)
   {
	  pan = terrain;  
      width = 150;
      height = 30;
      startx = (int)(Math.random() * Terrain.panelWidth-width);
      starty = Terrain.panelHeight-height;
      
   }

    /**
     * Getter
     * @return int
     */
   public int getX(){
	   return this.posx;
   }

    /**
     * Setter
     * @param x int
     */
   public void setX(int x){
       this.posx = x;
   }


    /**
     * Retoure la taille de la raquette
     * @return int
     */
    public int getSize(){
	   return this.width;
   }


    /**
     * Retoure la Hauteur de la raquette
     * @return int
     */
	public int getHeight() {
		return this.height;
	}

    /**
     * Retoure la taille de la raquette
     * @return int
     */
	public int getWidth() {
		return this.width;
	}


    /**
     * Retoure le nombre de points du joueur
     * @return int
     */
    public int getNbPoints() {
        return nbPoints;
    }

    /**
     * Met a jour le nombre de points du joueur
     * @param nbPoints int
     */
    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }

}
