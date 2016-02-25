package Model;

import java.awt.geom.Rectangle2D;


/**
 * La classe Brique sert a definir une brique du casse brique avec ses positions dans la matrice
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Brique {
	
	public static final int UPSIDE = 0;
	public static final int SIDE = 1;
	private Rectangle2D.Double brick;
	private int x, y, width, height, nbCoups;


    /**
     * Constructeur
     * @param posX position x dans la matrice
     * @param posY position y dans la matrice
     * @param nbCoups nombre de coups
     */
	public Brique(int posX, int posY, int nbCoups){
		width = 100;
		height = 30;
		this.x = posX;
		this.y = posY;
		this.nbCoups=nbCoups;
		brick = new Rectangle2D.Double(x, y, width, height);
	}

    /**
     * Getter
     * @return int
     */
	public int getX() {
		return x;
	}

    /**
     * Getter
     * @return int
     */
	public int getY() {
		return y;
	}

    /**
     * Getter
     * @return int
     */
	public int getWidth() {
		return width;
	}

    /**
     * Getter
     * @return int
     */
	public int getHeight() {
		return height;
	}

    /**
     * Converty les coordonnées de la brique en string
     * @return String
     */
	public String toString(){
		return x+" - "+y;
	}

    /**
     * Getter
     * @return int
     */
	public int getNbCoups() {
		return nbCoups;
	}

    /**
     * Met a jour le nombre de coups restants de la brique
     * @param nb int
     */
	public void setNbCoups(int nb) {
		nbCoups = nb;
	}

}
