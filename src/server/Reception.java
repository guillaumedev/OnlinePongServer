package server;

import Controller.RaquetteController;
import Model.Raquette;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Classe qui est chargée de recevoir les évènements et socket du client
 * @author Antoine Lebel, Guillaume Brosse, Clément LeBiez & Nicolas Belleme
 */
public class Reception implements Runnable {

    private BufferedReader in;
    private String message = null, login = null;
    private AccepterConnexion ac;
    private RaquetteController raquetteController;
    private Socket socket;

    /**
     * Constructeur de la classe
     * @param in instance de BufferedReader
     * @param login login de l'utilisateur
     * @param ac INstance d'accepterConnexion
     * @param r instance de raquette
     * @param s instance de socket
     */
    public Reception(BufferedReader in, String login, AccepterConnexion ac, Raquette r, Socket s){
        this.ac = ac;
        this.in = in;
        this.login = login;
        raquetteController=new RaquetteController(r);
        socket=s;
    }

    /**
     * tant que la socket est connectée, on lit les messages envoyés par le client
     */
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

    /**
     * Si un utilisateur s'en va, on ferme la socket et le bufferedReader et on notifie accepterConnexion qu'un utilisateur est parti
     */
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