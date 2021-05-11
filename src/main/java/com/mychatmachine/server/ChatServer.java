package com.mychatmachine.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.mychatmachine.client.Client;


public class ChatServer{
	
    int serverPort;
    ServerSocket serverSocket = null;
    boolean running    = false;
    public List<String> userNames = new ArrayList<>();
    public List<Client> clients = new ArrayList<>();
    
    public ChatServer() {
		this(9000);
	}
    public ChatServer(int portNumber) {
		this.serverPort = portNumber;
	}

	
	public void start() {
        try (ServerSocket serverSocket = new ServerSocket(this.serverPort)){
    		this.running = true;
    		System.out.println("ChatMachine is running...");
    		while (running) {
        		Socket clientSocket = serverSocket.accept();
        		
        		Client client = new Client(clientSocket, this);
        		clients.add(client);
        		client.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}

    public void stop(){
        this.running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    public void broadcast(String message, Client excludeUser) {
        for (Client aUser : clients) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
    public void broadcastAll(String message) {
        for (Client aUser : clients) {
                aUser.sendMessage(message);
        }
    }
    
    public void removeUser(String userName, Client aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            clients.remove(aUser);
            System.out.println(userName + " odadan çıktı...");
        }
    }
	
    public List<String> getUserNames() {
        return this.userNames;
    }
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
