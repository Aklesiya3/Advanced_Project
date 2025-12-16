package Chatapp;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleClient {

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    private JFrame frame;
    private JTextArea chatBox;
    private JTextField typeBox;
    private JButton sendBtn;

    public SimpleClient() {
        try {
            sock = new Socket("localhost", 5000);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());

            frame = new JFrame("Chat App");
            chatBox = new JTextArea();
            typeBox = new JTextField();
            sendBtn = new JButton("Send");

            chatBox.setEditable(false);
            chatBox.setFont(new Font("Verdana", Font.PLAIN, 14));

            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(chatBox), BorderLayout.CENTER);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(typeBox, BorderLayout.CENTER);
            panel.add(sendBtn, BorderLayout.EAST);
            frame.add(panel, BorderLayout.SOUTH);

            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            sendBtn.addActionListener(e -> sendMessage());
            typeBox.addActionListener(e -> sendMessage());

            new Thread(this::receiveMessages).start();

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }
    }

    private void sendMessage() {
        try {
            String msg = typeBox.getText().trim();
            if (!msg.isEmpty()) {
                out.writeUTF(msg);
                out.flush();
                chatBox.append("Me: " + msg + "\n");
                typeBox.setText("");
            }
        } catch (Exception e) {
            System.out.println("Message failed.");
        }
    }

    private void receiveMessages() {
        try {
            while (true) {
                chatBox.append(in.readUTF() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Connection lost.");
        }
    }

    public static void main(String[] args) {
        new SimpleClient();
    }
}
