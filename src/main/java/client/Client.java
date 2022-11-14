package client;

import connection.MessageType;
import server.Setup;
import connection.Connection;
import connection.Message;

import javax.swing.*;
import java.io.*;
import java.net.*;

public final class Client {

    public Connection connection;
    ChatGUI chatGUI;

    public Client(Setup setup) throws IOException, ClassNotFoundException {
        chatGUI = new ChatGUI(this);
        connectToServer(setup);
        startChat(this.connection);
    }
    private void connectToServer(Setup setup) throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket(setup.adressIP, setup.port));

        Message messageNameChange = connection.receive();
        if(messageNameChange.messageType.equals(MessageType.NAME_CHANGE)) {
            responseNameChange();
        }

        //connection.send(new Message(MessageType.NAME_CHANGE, JOptionPane.showInputDialog("Введите имя")));
        //return connection.receive().messageType.equals(MessageType.ACCEPTED);
    }

    private void responseNameChange() throws IOException {
        connection.send(new Message(MessageType.NAME_CHANGE, JOptionPane.showInputDialog("введите имя")));
    }

    private void startChat(Connection connection) throws IOException, ClassNotFoundException {

        while(!connection.socket.isClosed()) {
            Message message = this.connection.receive();

            if(message.messageType.equals(MessageType.TEXT)) {
                chatGUI.refreshDialog(message);
            }
            else if(message.messageType.equals(MessageType.USER_HAS_LEFT) ||
                    message.messageType.equals(MessageType.USER_HAS_JOINED) ||
                    message.messageType.equals(MessageType.USERS_LIST_UPDATE)) {
                chatGUI.refreshUsersList(message);
            }
            else if(message.messageType.equals(MessageType.ACCEPTED)) {
                this.connection.close();
            }
        }
    }



    //public static void main(String[] args) throws IOException, ClassNotFoundException {
    //    new Client(new Setup("localhost", 8081));
    //}
}
