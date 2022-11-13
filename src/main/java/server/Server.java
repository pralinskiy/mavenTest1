package server;

import connection.Connection;
import connection.Message;
import connection.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private HashMap<Connection, String> clients = new HashMap<>();
    public Setup setup = new Setup(8888);

    public Server(String port) {

        this.setup.port = Integer.parseInt(port);
        this.initServer();
    }
    private void initServer() {
        try
        {
            serverSocket = new ServerSocket(this.setup.port);

            enableConnections();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void enableConnections() throws IOException, ClassNotFoundException {
        while(true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket);
        }
    }

    private void sendMessageToAllUsers(Message message) throws IOException {
        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            client.getKey().send(new Message(MessageType.TEXT, /*client.getValue() +*/ message.message + "\n"));
        }
    }

    private void sendUsersListToAllUsers(Message message) throws IOException {
        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            client.getKey().send(new Message(MessageType.USER_HAS_JOINED, getUsersList()));
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

        public ServerThread(Socket socket) throws IOException {
            this.socketInThread = socket;
            id = Arrays.toString(socketInThread.getInetAddress().getAddress());
            name = id;
            connection = new Connection(this.socketInThread);
            clients.put(connection, name);
            //connection.send(new Message(MessageType.ACCEPTED));
            sendMessageToAllUsers(new Message(MessageType.TEXT, name + " has joined\n"));
            sendUsersListToAllUsers(new Message(MessageType.USERS_LIST_UPDATE, getUsersList()));
            this.start();
        }

        @Override
        public void run() {
            try
            {
                startClientsChat(connection);
            }
            catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

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
                    sendMessageToAllUsers(new Message(MessageType.TEXT, this.name + " has left"));
                    sendUsersListToAllUsers(new Message(MessageType.USERS_LIST_UPDATE, getUsersList()));
                    connection.send(new Message(MessageType.ACCEPTED));
                    this.connection.close();
                    break;
                }
                else if(message.messageType.equals(MessageType.NAME_CHANGE)) {
                    clients.replace(connection, clients.get(connection), message.message);
                    this.name = clients.get(connection);
                    sendUsersListToAllUsers(new Message(MessageType.USERS_LIST_UPDATE, getUsersList()));
                }

            }
        }


    }

}
