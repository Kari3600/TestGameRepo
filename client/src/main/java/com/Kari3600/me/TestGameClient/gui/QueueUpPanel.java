package com.Kari3600.me.TestGameClient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class QueueUpPanel extends JPanel {

    private JLabel playerCountLabel;

    public void setPlayersCount(byte c) {
        playerCountLabel.setText("Players found: "+c+"/2");
    }

    public QueueUpPanel() {
        System.out.println("Created panel");
        setLayout(null);  // Use null layout for manual positioning of elements
        setBounds(440, 200, 400, 200);  // Centered position in the JFrame
        setBackground(Color.PINK);

        // Label to display the number of players in the queue
        playerCountLabel = new JLabel("Players found: 0/2");
        playerCountLabel.setBounds(120, 80, 200, 30);  // Position inside the panel
        playerCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(playerCountLabel);
    }

}
