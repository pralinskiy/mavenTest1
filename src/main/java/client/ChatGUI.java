package client;

import connection.Message;
import connection.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.security.Key;

public class ChatGUI extends JFrame implements KeyListener {
    private static final int GUI_WIDTH = 500;
    private static final int GUI_HEIGHT = 500;

    JTextArea textOutput = new JTextArea();
    public JTextField textInput = new JTextField();
    JTextArea usersList = new JTextArea();
    JPanel panelSouth = new JPanel(new GridLayout(2, 1));
    JPanel panelSouthHelp = new JPanel(new FlowLayout());
    JButton buttonSendMessage = new JButton("send");
    JButton buttonChangeName = new JButton("name");
    JButton buttonStopChatting = new JButton("stop");
    Client client;

    public ChatGUI(Client client) {
        super(client.toString());
        this.client = client;
        this.initChat();
    }
    public ChatGUI() {
        super("tesst");
        this.initChat();
    }
    private void initChat() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setResizable(true);

        this.initBorders();
        this.initActionListeners();

        this.setVisible(true);
    }
    private void initBorders() {
        textOutput.setLineWrap(true);
        textOutput.setBackground(Color.green);
        textOutput.setEditable(false);

        usersList.setSize(200, this.HEIGHT);
        usersList.setLineWrap(true);
        usersList.setBackground(Color.YELLOW);
        usersList.setEditable(false);

        panelSouth.add(textInput);
        panelSouthHelp.add(buttonSendMessage);
        panelSouthHelp.add(buttonChangeName);
        panelSouthHelp.add(buttonStopChatting);
        panelSouth.add(panelSouthHelp);

        this.add(new JScrollPane(textOutput), BorderLayout.CENTER);
        this.add(new JScrollPane(usersList), BorderLayout.EAST);
        this.add(panelSouth, BorderLayout.SOUTH);
    }

    private void initActionListeners() {
        buttonSendMessage.addActionListener(e -> {
            if(e.getSource() == buttonSendMessage) {
                try {
                    client.connection.send(new Message(MessageType.TEXT, textInput.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonChangeName.addActionListener(e -> {
            if(e.getSource() == buttonChangeName) {
                try {
                    client.connection.send(new Message(MessageType.NAME_CHANGE, JOptionPane.showInputDialog("¬ведите им€")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonStopChatting.addActionListener(e -> {
            if(e.getSource() == buttonStopChatting) {
                try {
                    client.connection.send(new Message(MessageType.USER_HAS_LEFT));
                    //client.connection.close();
                    this.dispose();
                    //System.exit(-13);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        textInput.addKeyListener(this);
    }

    public void refreshDialog(Message message) {
        this.textOutput.append(message.message);
    }

    public void refreshUsersList(Message message) {
        this.usersList.setText(message.message);
    }

    //public static void main(String[] args) throws IOException {
    //    new ChatGUI();
    //}

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                client.connection.send(new Message(MessageType.TEXT, textInput.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) { }
}
