package server;

import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;

public class Authentification implements Runnable {

    private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String login = "zero";
    public Thread threadEmission;
    public Thread threadReception;
    private AccepterConnexion accepterConnexion;

    public Authentification(Socket s, AccepterConnexion a){
        socket = s;
        accepterConnexion=a;
    }

    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            out.println("Entrez votre login :");
            out.flush();
            login = in.readLine();


            out.println("connecte");
            System.out.println(login +" vient de se connecter ");
            out.flush();

            //threadEmission = new Thread(new Emission(out));
            //threadEmission.start();

            threadReception = new Thread(new Reception(in, login, this.accepterConnexion));
            threadReception.start();

        } catch (IOException e) {

            System.err.println(login+" ne répond pas !");
        }
    }
    public void sendMessage(String login, String str){
        out.println(login+": "+ str);
        out.flush();
    }
}