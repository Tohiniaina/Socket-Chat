package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Conversation extends Thread {
    Socket socketClient;
    int numero;
    List<Conversation> clients;

    public Conversation(Socket socketClient, int numero, List<Conversation> clients) {
        this.socketClient = socketClient;
        this.numero = numero;
        this.clients = clients;
    }
    public void broadcastMessage(String message, Socket socket, int numClient) {
        try {
            for(Conversation client:clients) {
                if (client.socketClient!=socket ) {
                    if (client.numero == numClient || numClient == -1) {
                        PrintWriter printWriter = new PrintWriter(client.socketClient.getOutputStream(),true);
                        printWriter.println(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socketClient.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);

            PrintWriter pw = new PrintWriter(socketClient.getOutputStream(),true);
            String ipClient = socketClient.getRemoteSocketAddress().toString();
            pw.println("Bienvenue, vous etes le client "+numero);
            System.out.println("Connexion du client numero "+numero+", IP="+ipClient);

            while (true) {
                String req = br.readLine().toString();
                if (req.contains("=>")) {
                    String[] requestParams = req.split("=>");  
                    if (requestParams.length==2);
                    String message = "Client "+numero+" : "+requestParams[1];
                    int numeroClient = Integer.parseInt(requestParams[0]);
                    broadcastMessage(message,socketClient,numeroClient);
                } else {
                    String mess = "Client "+numero+" : "+req;
                    broadcastMessage(mess,socketClient,-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
