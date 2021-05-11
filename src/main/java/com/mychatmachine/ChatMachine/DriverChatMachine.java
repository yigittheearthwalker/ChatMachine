package com.mychatmachine.ChatMachine;

import com.mychatmachine.server.ChatServer;

public class DriverChatMachine {
	public static void main(String[] args) {
		ChatServer chatServer = new ChatServer(80);
		chatServer.start();	
		
	}
}
