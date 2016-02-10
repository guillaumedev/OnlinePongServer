package server;

/**
 * Created by guillaumebrosse on 21/01/2016.
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AccepterConnexion implements Runnable{

    private ServerSocket socketserver = null;
    private Socket socket = null;
    private ArrayList<Authentification> listUser;

    public Thread authentificationThread;

    public AccepterConnexion(ServerSocket ss){
        socketserver = ss;
        listUser=new ArrayList<Authentification>();
    }

    public void notifierAll(String login, String str){
        for(int i=0; i<listUser.size(); i++){
            listUser.get(i).sendMessage(login, str);
        }
    }
    public void run() {

        try {
            while(true){

                socket = socketserver.accept();
                System.out.println("Un zéro veut se connecter  ");

                Authentification auth = new  Authentification(socket, this);

                authentificationThread = new Thread(auth);
                authentificationThread.start();
                listUser.add(auth);

            }
        } catch (IOException e) {

            System.err.println("Erreur serveur");
        }

    }

    public ArrayList<Authentification> getListUsers(){
        return this.listUser;
    }
}