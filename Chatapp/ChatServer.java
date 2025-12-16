package Chatapp;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {

    private static ArrayList<ChatHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("New client connected.");
                ChatHandler handler = new ChatHandler(clientSocket);
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Send message to all clients except sender
    public static void broadcast(String msg, ChatHandler sender) {
        for (ChatHandler client : clients) {
            if (client != sender) {
                client.sendMessage(msg);
            }
        }
    }

    public static void removeClient(ChatHandler client) {
        clients.remove(client);
    }
}
