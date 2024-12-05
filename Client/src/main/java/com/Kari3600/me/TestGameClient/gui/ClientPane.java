package com.Kari3600.me.TestGameClient.gui;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JFrame;

import com.Kari3600.me.TestGameClient.Main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class ClientPane extends JLayeredPane {

    private QueueUpPanel panel = null;

    public void popQueueUpFrame() {
        if (panel == null) {
            panel = new QueueUpPanel();
        }
        add(panel, JLayeredPane.PALETTE_LAYER);
        setComponentZOrder(panel, 0);
        revalidate();
        repaint();
    }

    public void queueUpPlayers(byte count) {
        if (panel == null) {
            popQueueUpFrame();
        }
        panel.setPlayersCount(count);
    }

    public ClientPane() {

        JFrame frame = new JFrame("Test Game Client");
        frame.setResizable(false);
        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1280, 720);
        frame.setContentPane(this);

        JButton button = new JButton("Start");
        button.setBounds(540, 300, 200, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Main.joinGame();
            }
        });

        add(button);

        frame.setVisible(true);
    }
}
