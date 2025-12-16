package Chatapp;
import java.io.*;
import java.net.*;

public class ChatHandler extends Thread {

    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;

    public ChatHandler(Socket client) {
        this.client = client;
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error initializing client streams.");
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while (true) {
                msg = in.readUTF();
                if (msg.equalsIgnoreCase("bye")) {
                    break;
                }
                ChatServer.broadcast(msg, this); // send to all clients
            }
            client.close();
            ChatServer.removeClient(this);
            System.out.println("A client disconnected.");
        } catch (IOException e) {
            ChatServer.removeClient(this);
            System.out.println("Client left unexpectedly.");
        }
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message to client.");
        }
    }
}
