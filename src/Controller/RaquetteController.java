package Controller;

import Model.Raquette;

/**
 * Created by guillaumebrosse on 24/02/2016.
 */
public class RaquetteController {
    private Raquette raquette;
    public RaquetteController(Raquette r){
        raquette=r;
    }

    public void setX(int x){
        raquette.setX(x);
    }
}

