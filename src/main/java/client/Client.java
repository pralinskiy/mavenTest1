package client;

import connection.MessageType;
import server.Setup;
import connection.Connection;
import connection.Message;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.spec.ECField;

public final class Client {

    public Connection connection;
    ChatGUI chatGUI;


    FileReader fileReader;


    public Client(Setup setup) throws IOException, ClassNotFoundException {
        try {
            chatGUI = new ChatGUI(this);
            connectToServer(setup);
            startChat(this.connection);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.connection.send(new Message(MessageType.CONSOLE, e.getMessage()));
        }
    }
    private void connectToServer(Setup setup) throws IOException, ClassNotFoundException {
        try {
            connection = new Connection(new Socket(setup.adressIP, setup.port));

            Message messageNameChange = connection.receive();
            if(messageNameChange.messageType.equals(MessageType.NAME_CHANGE)) {
                responseNameChange();
            }

            //connection.send(new Message(MessageType.NAME_CHANGE, JOptionPane.showInputDialog("Введите имя")));
            //return connection.receive().messageType.equals(MessageType.ACCEPTED);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.connection.send(new Message(MessageType.CONSOLE, e.getMessage()));
        }

    }

    private void responseNameChange() throws IOException {
        try {
            connection.send(new Message(MessageType.NAME_CHANGE, JOptionPane.showInputDialog("введите имя")));
        } catch (Exception e) {
            e.printStackTrace();
            this.connection.send(new Message(MessageType.CONSOLE, e.getMessage()));
        }

    }

    private void startChat(Connection connection) throws IOException, ClassNotFoundException {
        try {
            while(!this.connection.socket.isClosed()) {
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
                else if(message.messageType.equals(MessageType.IMAGE)) {
                    this.fileReader.image = message.image;
                    //this.chatGUI.addImageToTextDialog(this.fileReader);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            this.connection.send(new Message(MessageType.CONSOLE, e.getMessage()));
        }

    }



    public ImageIcon getImageFromSystem() {
        this.fileReader = new FileReader(this.chatGUI);
        return fileReader.getImage();
    }

    public void printImage() {

    }

    //public static void main(String[] args) throws IOException, ClassNotFoundException {
    //    new Client(new Setup("localhost", 8081));
    //}
}
