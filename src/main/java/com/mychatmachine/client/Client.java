package com.mychatmachine.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.mychatmachine.server.ChatServer;

public class Client extends Thread{
	private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
 
    public Client(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
	
	public void run() {
		try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            printUsers();
 
            String userName = reader.readLine();
            server.userNames.add(userName);
 
            String serverMessage = "[ChatMachine]: Hoşgeldin " + userName + " :)";
            server.broadcastAll(serverMessage);
            String clientMessage;
 
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
 
            } while (!clientMessage.equals("bye"));
 
            server.removeUser(userName, this);
            socket.close();
 
            serverMessage = "[ChatMachine]: " + userName + " odadan çıktı...";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Error with the Client: " + ex.getMessage());
            ex.printStackTrace();
        }	
	}
	
    void printUsers() {
        if (server.userNames.size() > 0) {
            writer.println("Odadakiler: " + server.getUserNames());
        } else {
            writer.println("Odada kimse yok :(");
        }
        writer.println("[ChatMachine]: Adın nedir? \nbaşlamak için adını yaz...");
    }
    
    public void sendMessage(String message) {
        writer.println(message);
    }
}
