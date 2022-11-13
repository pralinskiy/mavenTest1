package graphics;

import client.Client;
import server.Server;
import server.Setup;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class SetupGUI extends JFrame{
    JButton buttonServer = new JButton("server");
    JButton buttonClient = new JButton("client");
    JPanel panelButtons = new JPanel(new FlowLayout());
    public Setup setup;

    public SetupGUI() {
        this.setSize(320,320);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.initButtons();

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.pack();
    }

    private void initButtons() {
        buttonClient.setFocusable(false);
        buttonServer.setFocusable(false);

        buttonClient.setPreferredSize(new Dimension(100,50));
        buttonServer.setPreferredSize(new Dimension(100,50));

        panelButtons.add(buttonClient);
        panelButtons.add(buttonServer);

        buttonClient.addActionListener(e -> {
            if(e.getSource() == buttonClient) {
                setup = new Setup(
                        JOptionPane.showInputDialog("введите IP"),
                        Integer.parseInt(JOptionPane.showInputDialog("введите port"))
                );
                try
                {
                    //this.dispose();
                    new Thread(() -> {
                        try {
                            new Client(setup);
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }).start();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        buttonServer.addActionListener(e -> {
            if(e.getSource() == buttonServer) {
                try
                {
                    new Thread(() -> {
                        new Server(JOptionPane.showInputDialog("введите port"));
                    }).start();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        this.add(panelButtons, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new SetupGUI();
    }
}
