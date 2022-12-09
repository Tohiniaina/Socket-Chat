package Serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Serveur.Conversation;

public class Serveur extends Thread {
    private boolean isActive=true;
    private int nombreClients=0;
    private List<Conversation> clients = new ArrayList<Conversation>();
    public static void main(String[] args) {
        new Serveur().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (isActive) {
                Socket socket = serverSocket.accept();
                ++nombreClients;
                Conversation conversation = new Conversation(socket,nombreClients,clients);
                clients.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}