package Controller;

import Model.Raquette;

/**
 * Le controlleur de raquette se charge de modifier la position en x de la eaquette dans la vue
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class RaquetteController {
    private Raquette raquette;

    /**
     * Constructeur
     * @param r Raquette
     */
    public RaquetteController(Raquette r){
        raquette=r;
    }

    /**
     * Setter
     * @param x int
     */
    public void setX(int x){
        raquette.setX(x);
    }
}

