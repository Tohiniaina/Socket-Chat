package Listen;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Ecoute implements ActionListener {
    JButton buttonConnecter;
    JButton sendMessage;
    JTextField textFieldHost;
    JTextField textFieldPort;
    JTextField mess;
    DefaultListModel<String> model;

    int etat = 0;
    PrintWriter pw;
    
    public JTextField getMess() {
        return mess;
    }

    public void setMess(JTextField mess) {
        this.mess = mess;
    }

    public JButton getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(JButton sendMessage) {
        this.sendMessage = sendMessage;
    }

    public DefaultListModel<String> getModel() {
        return model;
    }

    public void setModel(DefaultListModel<String> model) {
        this.model = model;
    }

    public JButton getButtonConnecter() {
        return buttonConnecter;
    }

    public void setButtonConnecter(JButton buttonConnecter) {
        this.buttonConnecter = buttonConnecter;
    }

    public JTextField getTextFieldHost() {
        return textFieldHost;
    }

    public void setTextFieldHost(JTextField textFieldHost) {
        this.textFieldHost = textFieldHost;
    }

    public JTextField getTextFieldPort() {
        return textFieldPort;
    }

    public void setTextFieldPort(JTextField textFieldPort) {
        this.textFieldPort = textFieldPort;
    }

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (this.buttonConnecter == btn && etat != 1) {
            this.etat = 1;
            String host = textFieldHost.getText();
            int port = Integer.parseInt(textFieldPort.getText());
            try {
                Socket socket = new Socket(host,port);
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                pw = new PrintWriter(socket.getOutputStream(), true);
                new Thread(()->{
                    try {
                        while(true) {
                            String response = bufferedReader.readLine();
                            model.addElement(response);
                        }
                    } catch (IOException erro) {
                        erro.printStackTrace();
                    }
                }).start();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
        if (this.sendMessage == btn) {
            String message = mess.getText();
            pw.println(message);
        }
    }

}
