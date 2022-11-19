package server;

import connection.Connection;
import connection.Message;
import connection.MessageType;
import graphics.SetupGUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private HashMap<Connection, String> clients = new HashMap<>();
    public Setup setup = new Setup(8888);


    SetupGUI setupGUI;


    ArrayList<String> chatAllMessages = new ArrayList<String>();


    public Server(String port, SetupGUI setupGUI) {

        this.setupGUI = setupGUI;
        this.setup.port = Integer.parseInt(port);

        this.initServer();
    }
    private void initServer() {
        try
        {
            serverSocket = new ServerSocket(this.setup.port);
            setupGUI.consoleLog("Port has opened! ");
            enableConnections();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void enableConnections() throws IOException, ClassNotFoundException {
        setupGUI.consoleLog("Server has started");
        while(true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket);
        }
    }

    private void sendMessageToAllUsers(Message message) throws IOException {
        setupGUI.consoleLog("All users got -> " + message.message);
        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            client.getKey().send(new Message(MessageType.TEXT, /*client.getValue() +*/ message.message + "\n"));
        }
        chatAllMessages.add(message.message);
    }

    private void sendUsersListToAllUsers() throws IOException {
        setupGUI.consoleLog("User list updated");
        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            client.getKey().send(new Message(MessageType.USERS_LIST_UPDATE, getUsersList()));
        }
    }

    private String getUsersList() {
        String usersList = "users list: \n";

        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            usersList += client.getValue() + client.getKey().socket.getInetAddress().toString() + '\n';
        }

        return usersList;
    }

    class ServerThread extends Thread {
        Socket socketInThread;
        String name;
        String id;
        Connection connection;

        public ServerThread(Socket socket) throws IOException, ClassNotFoundException {
            this.socketInThread = socket;
            id = Arrays.toString(socketInThread.getInetAddress().getAddress());
            connection = new Connection(this.socketInThread);
            setupGUI.consoleLog("Client " + id + " has connected");
            //connection.send(new Message(MessageType.ACCEPTED));
            sendMessageToAllUsers(new Message(MessageType.TEXT, '\n' + sendRequestNameChange() + " has joined\n"));
            connection.send(new Message(MessageType.TEXT, getChatAllMessages()));
            clients.put(connection, name);
            sendUsersListToAllUsers();
            this.start();
        }

        @Override
        public void run() {
            try
            {
                startClientsChat(connection);
            }
            catch (IOException | ClassNotFoundException e) { setupGUI.consoleLog(e.getMessage()); }

        }

        private void startClientsChat(Connection connection) throws IOException, ClassNotFoundException {

            while(true) {
                Message message = this.connection.receive();

                if(message.messageType.equals(MessageType.TEXT)) {
                    sendMessageToAllUsers(new Message(MessageType.TEXT, this.name + ": " + message.message));
                }
                else if(message.messageType.equals(MessageType.USER_HAS_LEFT)) {
                    clients.remove(connection);
                    //connection.close();
                    //this.connection.close();
                    setupGUI.consoleLog("Client " + id + " has disconnected");
                    sendMessageToAllUsers(new Message(MessageType.TEXT, '\n' + this.name + " has left\n"));
                    sendUsersListToAllUsers();
                    connection.send(new Message(MessageType.ACCEPTED));
                    this.connection.close();
                    break;
                }
                else if(message.messageType.equals(MessageType.NAME_CHANGE)) {
                    clients.replace(connection, clients.get(connection), message.message);
                    this.name = clients.get(connection);
                    sendUsersListToAllUsers();
                }

            }
        }

        private String sendRequestNameChange() throws IOException, ClassNotFoundException {
            connection.send(new Message(MessageType.NAME_CHANGE));
            Message messageWithName = connection.receive();
            if(messageWithName.messageType.equals(MessageType.NAME_CHANGE)) {
                this.name = messageWithName.message;
            }
            return this.name;
        }

        private String getChatAllMessages() {
            String allMessages = "";
            for(String message : chatAllMessages) {
                allMessages += message + '\n';
            }
            return allMessages;
        }
    }

}
