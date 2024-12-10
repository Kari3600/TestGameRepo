package com.Kari3600.me.TestGameClient.gui;

import javax.swing.*;

import com.Kari3600.me.TestGameClient.Main;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginRequest;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResponse;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResult;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginTask;
import com.Kari3600.me.TestGameCommon.util.EncryptionUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginPanel extends JFrame {

    private boolean isLoggingIn = false;

    public LoginPanel() {
        // Set title and default close operation
        setTitle("Login Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
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

        // Log in button
        JButton loginButton = new JButton("Log in");

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isLoggingIn = true;
                TCPConnection conn = Main.getConnection();
                conn.sendPacketRequest(new PacketLoginRequest().setUsername(String.valueOf(usernameField.getText()))).thenAcceptAsync(packet -> {
                    if (!(packet instanceof PacketLoginTask)) {
                        System.out.println("Wrong packet");
                        return;
                    }
                    String salt = ((PacketLoginTask) packet).getSalt();
                    conn.sendPacketRequest(new PacketLoginResponse().setPassword(EncryptionUtil.encrypt(salt+EncryptionUtil.encrypt(String.valueOf(passwordField.getPassword()))))).thenAccept(resultPacket -> {
                        if (!(resultPacket instanceof PacketLoginResult)) {
                            System.out.println("Wrong packet");
                            return;
                        }
                        PacketLoginResult loginResult = (PacketLoginResult) resultPacket;
                        switch (loginResult.getStatus()) {
                            case 0:
                            new ClientPane();
                            dispose();
                            break;

                            case 1:
                            JOptionPane.showMessageDialog(loginButton, "User does not exist.", "Login Error", JOptionPane.ERROR_MESSAGE);
                            break;

                            case 2:
                            JOptionPane.showMessageDialog(loginButton, "Incorrect password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    });
                });
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        // Register label with clickable text
        JLabel registerLabel = new JLabel("Don't have an account yet? Register");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener for clickable effect
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterPanel(); // Open the registration form
                dispose(); // Close the login form
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerLabel, gbc);

        // Add main panel to frame
        add(mainPanel);

        // Make the frame visible
        setVisible(true);
    }
}
