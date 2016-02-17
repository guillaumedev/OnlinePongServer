package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import Model.Raquette;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;


public class Reception implements Runnable {

    private BufferedReader in;
    private String message = null, login = null;
    private AccepterConnexion ac;
    private Raquette raquette;
    private Socket socket;

    public Reception(BufferedReader in, String login, AccepterConnexion ac, Raquette r, Socket s){
        this.ac = ac;
        this.in = in;
        this.login = login;
        raquette=r;
        socket=s;
    }

    public synchronized void run() {

        while(socket.isConnected()){
            try {

                message = in.readLine();
                //System.out.println(login+" : "+message);
                //this.ac.notifierAll(login, message);
                if(message.equals("sendracketposition")){
                    String posX = in.readLine();
                    raquette.setX(Integer.parseInt(posX));
                    ac.notifierAllRaquette(login, posX);
                }

            } catch (IOException e) {
                try{
                    in.close();
                    socket.close();
                    ac.removeUser(login);
                    break;
                } catch(Exception ex){
                    System.out.println(ex);
                }

                e.printStackTrace();
            }
        }
    }

}