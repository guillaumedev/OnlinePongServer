package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import Controller.RaquetteController;
import Model.Raquette;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;


public class Reception implements Runnable {

    private BufferedReader in;
    private String message = null, login = null;
    private AccepterConnexion ac;
    private RaquetteController raquetteController;
    private Socket socket;

    public Reception(BufferedReader in, String login, AccepterConnexion ac, Raquette r, Socket s){
        this.ac = ac;
        this.in = in;
        this.login = login;
        raquetteController=new RaquetteController(r);
        socket=s;
    }

    public synchronized void run() {

        while(socket.isConnected()){
            try {

                message = in.readLine();
                if(message==null){
                   userLeft();
                    break;
                }
                else if(message.equals("sendracketposition")){
                    String posX = in.readLine();
                    raquetteController.setX(Integer.parseInt(posX));
                    ac.notifierAllRaquette(login, posX);
                }

            } catch (IOException e) {
                userLeft();
                e.printStackTrace();
                break;
            }
        }
    }

    public void userLeft(){
        try{
            in.close();
            socket.close();
            ac.removeUser(login);
        } catch(Exception ex){
            System.out.println(ex);
        }
    }

}