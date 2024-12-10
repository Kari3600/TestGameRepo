package com.Kari3600.me.TestGameClient.gui;

import javax.swing.*;

import com.Kari3600.me.TestGameClient.Main;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterRequest;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterResult;
import com.Kari3600.me.TestGameCommon.util.EncryptionUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class RegisterPanel extends JFrame {

    public RegisterPanel() {
        // Set title and default close operation
        setTitle("Registration Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the frame

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(confirmPasswordField, gbc);

        // Register button
        JButton registerButton = new JButton("Register");

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!passwordField.getPassword().equals(confirmPasswordField.getPassword())) {
                    JOptionPane.showMessageDialog(registerButton, "Passwords must match.", "Register Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                TCPConnection conn = Main.getConnection();
                conn.sendPacketRequest(new PacketRegisterRequest().setUsername(String.valueOf(usernameField.getText())).setPassword(EncryptionUtil.encrypt(String.valueOf(passwordField.getPassword())))).thenAccept(resultPacket -> {
                    if (!(resultPacket instanceof PacketRegisterResult)) {
                        System.out.println("Wrong packet");
                        return;
                    }
                    PacketRegisterResult registerResult = (PacketRegisterResult) resultPacket;
                    switch (registerResult.getStatus()) {
                        case 0:
                        JOptionPane.showMessageDialog(registerButton, "Register successful, now log in.", "Register", JOptionPane.INFORMATION_MESSAGE);
                        new LoginPanel(); // Open the registration form
                        dispose(); // Close the login form
                        break;

                        case 1:
                        JOptionPane.showMessageDialog(registerButton, "User already exists.", "Register Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                });
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerButton, gbc);

        // Add main panel to frame
        add(mainPanel);

        // Make the frame visible
        setVisible(true);
    }
}
