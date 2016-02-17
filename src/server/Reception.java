package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import Model.Raquette;

import java.io.BufferedReader;
import java.io.IOException;


public class Reception implements Runnable {

    private BufferedReader in;
    private String message = null, login = null;
    private AccepterConnexion ac;
    private Raquette raquette;

    public Reception(BufferedReader in, String login, AccepterConnexion ac, Raquette r){
        this.ac = ac;
        this.in = in;
        this.login = login;
        raquette=r;
    }

    public void run() {

        while(true){
            try {

                message = in.readLine();
                System.out.println(login+" : "+message);
                //this.ac.notifierAll(login, message);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}